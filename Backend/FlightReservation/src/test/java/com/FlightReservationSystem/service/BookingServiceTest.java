package com.FlightReservationSystem.service;

import com.FlightReservationSystem.dto.BookingRequest;
import com.FlightReservationSystem.dto.PassengerInfo;
import com.FlightReservationSystem.entity.Booking;
import com.FlightReservationSystem.entity.Flight;
import com.FlightReservationSystem.entity.User;
import com.FlightReservationSystem.repository.BookingRepository;
import com.FlightReservationSystem.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private FlightService flightService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PaymentService paymentService;
    @Mock
    private EmailService emailService;

    @InjectMocks
    private BookingService bookingService;

    private User user;
    private Flight flight;
    private Booking booking;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("test@example.com");

        flight = new Flight();
        flight.setId(1L);
        flight.setAvailableSeats(100);
        flight.setAvailableEconomySeats(80);
        flight.setAvailableBusinessSeats(20);
        flight.setPrice(BigDecimal.valueOf(100));
        flight.setEconomyPrice(BigDecimal.valueOf(100));
        flight.setBusinessPrice(BigDecimal.valueOf(250));
        flight.setSource("New York");
        flight.setDestination("London");
        flight.setDepartureTime(LocalDateTime.now().plusDays(1));

        booking = new Booking();
        booking.setId(1L);
        booking.setBookingReference("BK12345678");
        booking.setUser(user);
        booking.setFlight(flight);
        booking.setTotalAmount(BigDecimal.valueOf(200));
        booking.setSeatClass(Booking.SeatClass.ECONOMY);
        booking.setStatus(Booking.BookingStatus.PENDING);
        booking.setPaymentStatus(Booking.PaymentStatus.PENDING);
        booking.setNumberOfPassengers(2);
    }

    @Test
    void testBookFlight_Success_Economy() {
        // Arrange
        BookingRequest request = new BookingRequest();
        request.setFlightId(1L);
        request.setSeatClass("ECONOMY");
        request.setPassengers(List.of(
            createPassengerInfo("Alice", "30", "Female", "A1"),
            createPassengerInfo("Bob", "35", "Male", "A2")
        ));

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(flightService.getFlightById(anyLong())).thenReturn(flight);
        when(paymentService.createOrder(any(BigDecimal.class), anyString())).thenReturn("order_123");
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        // Act
        Booking result = bookingService.bookFlight(request, "testuser");

        // Assert
        assertNotNull(result);
        assertEquals("BK12345678", result.getBookingReference());
        assertEquals(user, result.getUser());
        assertEquals(flight, result.getFlight());
        assertEquals(BigDecimal.valueOf(200), result.getTotalAmount());
        assertEquals(2, result.getNumberOfPassengers());
        assertEquals(Booking.BookingStatus.PENDING, result.getStatus());
        assertEquals(Booking.PaymentStatus.PENDING, result.getPaymentStatus());
        assertEquals("order_123", result.getRazorpayOrderId());
        verify(paymentService, times(1)).createOrder(any(BigDecimal.class), anyString());
        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    void testBookFlight_NotEnoughSeats() {
        // Arrange
        BookingRequest request = new BookingRequest();
        request.setFlightId(1L);
        request.setSeatClass("BUSINESS");
        request.setPassengers(List.of(
            createPassengerInfo("Alice", "30", "Female", "B1"),
            createPassengerInfo("Bob", "35", "Male", "B2"),
            createPassengerInfo("Charlie", "40", "Male", "B3")
        ));

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(flightService.getFlightById(anyLong())).thenReturn(flight);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
            bookingService.bookFlight(request, "testuser")
        );
        assertEquals("Not enough business seats available", exception.getMessage());
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void testGetUserBookings_Success() {
        // Arrange
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(bookingRepository.findByUserOrderByBookingDateDesc(any(User.class))).thenReturn(List.of(booking));

        // Act
        List<Booking> result = bookingService.getUserBookings("testuser");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("BK12345678", result.get(0).getBookingReference());
    }

    @Test
    void testCancelBooking_Success() {
        // Arrange
        when(bookingRepository.findByBookingReference(anyString())).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        // Act
        Booking result = bookingService.cancelBooking("BK12345678", "testuser");

        // Assert
        assertNotNull(result);
        assertEquals(Booking.BookingStatus.CANCELLED, result.getStatus());
        assertEquals(Booking.PaymentStatus.REFUNDED, result.getPaymentStatus());
        assertEquals(BigDecimal.valueOf(200), result.getRefundAmount());
        assertEquals(82, flight.getAvailableEconomySeats());
        verify(flightService, times(1)).saveFlight(any(Flight.class));
        verify(emailService, times(1)).sendCancellationConfirmation(anyString(), anyString(), anyString());
        verify(bookingRepository, times(1)).save(any(Booking.class));
    }
    
    @Test
    void testCancelBooking_AlreadyCancelled() {
        // Arrange
        booking.setStatus(Booking.BookingStatus.CANCELLED);
        when(bookingRepository.findByBookingReference(anyString())).thenReturn(Optional.of(booking));
    
        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
            bookingService.cancelBooking("BK12345678", "testuser")
        );
        assertEquals("Booking already cancelled", exception.getMessage());
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void testVerifyPayment_Success() {
        // Arrange
        when(bookingRepository.findByBookingReference(anyString())).thenReturn(Optional.of(booking));
        when(paymentService.verifyPayment(anyString(), anyString(), anyString())).thenReturn(true);
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        // Act
        Booking result = bookingService.verifyPayment("BK12345678", "order_123", "pay_123", "sig_123");

        // Assert
        assertNotNull(result);
        assertEquals(Booking.BookingStatus.CONFIRMED, result.getStatus());
        assertEquals(Booking.PaymentStatus.PAID, result.getPaymentStatus());
        assertEquals(78, flight.getAvailableEconomySeats()); // 80 - 2 passengers
        verify(flightService, times(1)).saveFlight(any(Flight.class));
        verify(emailService, times(1)).sendBookingConfirmation(anyString(), anyString(), anyString());
        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    void testVerifyPayment_Failure() {
        // Arrange
        when(bookingRepository.findByBookingReference(anyString())).thenReturn(Optional.of(booking));
        when(paymentService.verifyPayment(anyString(), anyString(), anyString())).thenReturn(false);
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
            bookingService.verifyPayment("BK12345678", "order_123", "pay_123", "sig_123")
        );
        assertEquals("Payment verification failed", exception.getMessage());
        verify(bookingRepository, times(1)).save(any(Booking.class));
        assertEquals(Booking.PaymentStatus.FAILED, booking.getPaymentStatus());
        assertEquals(Booking.BookingStatus.CANCELLED, booking.getStatus());
    }
    
    @Test
    void testGetBookingByReference_Success() {
        // Arrange
        booking.setStatus(Booking.BookingStatus.CONFIRMED);
        when(bookingRepository.findByBookingReference(anyString())).thenReturn(Optional.of(booking));

        // Act
        Booking result = bookingService.getBookingByReference("BK12345678", "testuser");

        // Assert
        assertNotNull(result);
        assertEquals("BK12345678", result.getBookingReference());
    }

    @Test
    void testGetBookingByReference_UnauthorizedAccess() {
        // Arrange
        when(bookingRepository.findByBookingReference(anyString())).thenReturn(Optional.of(booking));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
            bookingService.getBookingByReference("BK12345678", "anotheruser")
        );
        assertEquals("Unauthorized access to booking", exception.getMessage());
    }
    
    @Test
    void testGetAllBookings() {
        // Arrange
        when(bookingRepository.findAll()).thenReturn(List.of(booking));
    
        // Act
        List<Booking> result = bookingService.getAllBookings();
    
        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("BK12345678", result.get(0).getBookingReference());
        verify(bookingRepository, times(1)).findAll();
    }
    
    // Helper method to create a PassengerInfo object
    private PassengerInfo createPassengerInfo(String name, String age, String gender, String seatPreference) {
        PassengerInfo passengerInfo = new PassengerInfo();
        passengerInfo.setName(name);
        passengerInfo.setAge(age);
        passengerInfo.setGender(gender);
        passengerInfo.setSeatPreference(seatPreference);
        return passengerInfo;
    }
}