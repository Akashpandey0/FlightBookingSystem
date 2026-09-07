package com.FlightReservationSystem.service;

import com.FlightReservationSystem.dto.BookingRequest;
import com.FlightReservationSystem.entity.Booking;
import com.FlightReservationSystem.entity.Flight;
import com.FlightReservationSystem.entity.User;
import com.FlightReservationSystem.repository.BookingRepository;
import com.FlightReservationSystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingService {
    
    private final BookingRepository bookingRepository;
    private final FlightService flightService;
    private final UserRepository userRepository;
    private final PaymentService paymentService;
    private final EmailService emailService;

    
    @Transactional
    public Booking bookFlight(BookingRequest request, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        Flight flight = flightService.getFlightById(request.getFlightId());
        
        // Check seat availability based on class
        boolean isEconomy = "ECONOMY".equals(request.getSeatClass());
        int availableSeats = isEconomy ? 
            (flight.getAvailableEconomySeats() != null ? flight.getAvailableEconomySeats() : flight.getAvailableSeats()) : 
            (flight.getAvailableBusinessSeats() != null ? flight.getAvailableBusinessSeats() : 0);
        
        if (availableSeats < request.getPassengers().size()) {
            throw new RuntimeException("Not enough " + request.getSeatClass().toLowerCase() + " seats available");
        }
        
        Booking booking = new Booking();
        booking.setBookingReference(generateBookingReference());
        booking.setUser(user);
        booking.setFlight(flight);
        booking.setNumberOfPassengers(request.getPassengers().size());
        // Calculate price based on seat class
        BigDecimal seatPrice = isEconomy ? 
            (flight.getEconomyPrice() != null ? flight.getEconomyPrice() : flight.getPrice()) : 
            (flight.getBusinessPrice() != null ? flight.getBusinessPrice() : flight.getPrice().multiply(BigDecimal.valueOf(2)));
        booking.setTotalAmount(seatPrice.multiply(BigDecimal.valueOf(request.getPassengers().size())));
        
        // Convert passenger list to JSON string for storage
        StringBuilder passengerDetails = new StringBuilder();
        for (int i = 0; i < request.getPassengers().size(); i++) {
            var passenger = request.getPassengers().get(i);
            passengerDetails.append("Passenger ").append(i + 1).append(": ")
                    .append(passenger.getName()).append(", Age: ").append(passenger.getAge())
                    .append(", Gender: ").append(passenger.getGender());
            if (passenger.getSeatPreference() != null) {
                passengerDetails.append(", Seat: ").append(passenger.getSeatPreference());
            }
            if (i < request.getPassengers().size() - 1) {
                passengerDetails.append("; ");
            }
        }
        booking.setPassengerDetails(passengerDetails.toString());
        booking.setSeatClass(Booking.SeatClass.valueOf(request.getSeatClass()));
        
        booking.setStatus(Booking.BookingStatus.PENDING);
        booking.setPaymentStatus(Booking.PaymentStatus.PENDING);
        
        try {
            String orderId = paymentService.createOrder(booking.getTotalAmount(), booking.getBookingReference());
            booking.setRazorpayOrderId(orderId);
        } catch (Exception e) {
            log.error("Failed to create payment order: ", e);
            // Set a default mock order ID if payment service fails
            booking.setRazorpayOrderId("order_mock_" + System.currentTimeMillis());
        }
        
        // Don't reduce seats until payment is confirmed
        Booking savedBooking = bookingRepository.save(booking);
        log.info("Pending booking created: {}", savedBooking.getBookingReference());
        return savedBooking;
    }
    
    public List<Booking> getUserBookings(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return bookingRepository.findByUserOrderByBookingDateDesc(user);
    }
    
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }
    
    @Transactional
    public Booking cancelBooking(String bookingReference, String username) {
        Booking booking = bookingRepository.findByBookingReference(bookingReference)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        
        if (!booking.getUser().getUsername().equals(username)) {
            throw new RuntimeException("Unauthorized to cancel this booking");
        }
        
        if (booking.getStatus() == Booking.BookingStatus.CANCELLED) {
            throw new RuntimeException("Booking already cancelled");
        }
        
        booking.setStatus(Booking.BookingStatus.CANCELLED);
        booking.setPaymentStatus(Booking.PaymentStatus.REFUNDED);
        booking.setRefundAmount(booking.getTotalAmount()); // Full refund
        booking.setRefundDate(LocalDateTime.now());
        
        Flight flight = booking.getFlight();
        if (booking.getSeatClass() == Booking.SeatClass.ECONOMY) {
            int currentEconomySeats = flight.getAvailableEconomySeats() != null ? flight.getAvailableEconomySeats() : 0;
            flight.setAvailableEconomySeats(currentEconomySeats + booking.getNumberOfPassengers());
        } else {
            int currentBusinessSeats = flight.getAvailableBusinessSeats() != null ? flight.getAvailableBusinessSeats() : 0;
            flight.setAvailableBusinessSeats(currentBusinessSeats + booking.getNumberOfPassengers());
        }
        // Update legacy field for backward compatibility
        int totalAvailable = (flight.getAvailableEconomySeats() != null ? flight.getAvailableEconomySeats() : 0) + 
                           (flight.getAvailableBusinessSeats() != null ? flight.getAvailableBusinessSeats() : 0);
        flight.setAvailableSeats(totalAvailable);
        
        // Save the updated flight
        flightService.saveFlight(flight);
        
        Booking cancelledBooking = bookingRepository.save(booking);
        
        // Send cancellation confirmation email
        emailService.sendCancellationConfirmation(
            booking.getUser().getEmail(),
            bookingReference,
            booking.getRefundAmount().toString()
        );
        
        log.info("Booking cancelled with refund amount: $" + booking.getRefundAmount());
        return cancelledBooking;
    }
    
    @Transactional
    public Booking verifyPayment(String bookingReference, String orderId, String paymentId, String signature) {
        Booking booking = bookingRepository.findByBookingReference(bookingReference)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        
        if (paymentService.verifyPayment(orderId, paymentId, signature)) {
            booking.setPaymentStatus(Booking.PaymentStatus.PAID);
            booking.setStatus(Booking.BookingStatus.CONFIRMED);
            
            // Reduce available seats only after successful payment
            Flight flight = booking.getFlight();
            if (booking.getSeatClass() == Booking.SeatClass.ECONOMY) {
                int currentEconomySeats = flight.getAvailableEconomySeats() != null ? flight.getAvailableEconomySeats() : flight.getAvailableSeats();
                flight.setAvailableEconomySeats(currentEconomySeats - booking.getNumberOfPassengers());
            } else {
                int currentBusinessSeats = flight.getAvailableBusinessSeats() != null ? flight.getAvailableBusinessSeats() : 0;
                flight.setAvailableBusinessSeats(currentBusinessSeats - booking.getNumberOfPassengers());
            }
            // Update legacy field for backward compatibility
            int totalAvailable = (flight.getAvailableEconomySeats() != null ? flight.getAvailableEconomySeats() : 0) + 
                               (flight.getAvailableBusinessSeats() != null ? flight.getAvailableBusinessSeats() : 0);
            flight.setAvailableSeats(totalAvailable);
            
            // Save the updated flight
            flightService.saveFlight(flight);
            
            Booking updatedBooking = bookingRepository.save(booking);
            
            // Send booking confirmation email
            String flightDetails = String.format("%s to %s on %s", 
                flight.getSource(), flight.getDestination(), 
                flight.getDepartureTime());
            emailService.sendBookingConfirmation(
                booking.getUser().getEmail(), 
                bookingReference, 
                flightDetails
            );
            
            log.info("Payment verified and booking confirmed: {}", bookingReference);
            return updatedBooking;
        } else {
            booking.setPaymentStatus(Booking.PaymentStatus.FAILED);
            booking.setStatus(Booking.BookingStatus.CANCELLED);
            bookingRepository.save(booking);
            throw new RuntimeException("Payment verification failed");
        }
    }
    
    public Booking getBookingByReference(String bookingReference, String username) {
        Booking booking = bookingRepository.findByBookingReference(bookingReference)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        
        if (!booking.getUser().getUsername().equals(username)) {
            throw new RuntimeException("Unauthorized access to booking");
        }
        
        if (booking.getStatus() != Booking.BookingStatus.CONFIRMED) {
            throw new RuntimeException("Ticket can only be downloaded for confirmed bookings");
        }
        
        return booking;
    }
    
    private String generateBookingReference() {
        return "BK" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}