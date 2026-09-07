package com.FlightReservationSystem.service;

import com.FlightReservationSystem.entity.Booking;
import com.FlightReservationSystem.entity.Flight;
import com.FlightReservationSystem.repository.BookingRepository;
import com.FlightReservationSystem.repository.FlightRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FlightStatusService {
    
    private final FlightRepository flightRepository;
    private final BookingRepository bookingRepository;
    
    @Scheduled(fixedRate = 60000) // Run every minute
    public void updateFlightStatuses() {
        LocalDateTime now = LocalDateTime.now();
        List<Flight> flights = flightRepository.findAll();
        
        for (Flight flight : flights) {
            if (flight.getStatus() == Flight.FlightStatus.SCHEDULED && 
                flight.getArrivalTime().isBefore(now)) {
                flight.setStatus(Flight.FlightStatus.COMPLETED);
                flightRepository.save(flight);
                log.info("Flight {} marked as COMPLETED", flight.getFlightNumber());
                
                // Update all confirmed bookings for this flight to completed
                List<Booking> confirmedBookings = bookingRepository.findByFlightAndStatus(
                    flight, Booking.BookingStatus.CONFIRMED);
                for (Booking booking : confirmedBookings) {
                    booking.setStatus(Booking.BookingStatus.COMPLETED);
                    bookingRepository.save(booking);
                    log.info("Booking {} marked as COMPLETED", booking.getBookingReference());
                }
            }
        }
    }
}