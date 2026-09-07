package com.FlightReservationSystem.service;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.FlightReservationSystem.entity.Booking;
import com.FlightReservationSystem.repository.BookingRepository;

@ExtendWith(MockitoExtension.class)
class BookingCleanupServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private BookingCleanupService bookingCleanupService;

    private Booking expiredBooking;

    @BeforeEach
    void setUp() {
        expiredBooking = new Booking();
        expiredBooking.setId(1L);
        expiredBooking.setBookingReference("BK12345678");
        expiredBooking.setStatus(Booking.BookingStatus.PENDING);
        expiredBooking.setBookingDate(LocalDateTime.now().minusMinutes(5));
    }

    @Test
    void testCancelExpiredPendingBookingsWithExpiredBookings() {
        List<Booking> expiredBookings = Arrays.asList(expiredBooking);
        
        when(bookingRepository.findByStatusAndBookingDateBefore(
            eq(Booking.BookingStatus.PENDING), any(LocalDateTime.class)))
            .thenReturn(expiredBookings);
        when(bookingRepository.save(any(Booking.class))).thenReturn(expiredBooking);

        bookingCleanupService.cancelExpiredPendingBookings();

        verify(bookingRepository).findByStatusAndBookingDateBefore(
            eq(Booking.BookingStatus.PENDING), any(LocalDateTime.class));
        verify(bookingRepository).save(expiredBooking);
    }

    @Test
    void testCancelExpiredPendingBookingsWithNoExpiredBookings() {
        when(bookingRepository.findByStatusAndBookingDateBefore(
            eq(Booking.BookingStatus.PENDING), any(LocalDateTime.class)))
            .thenReturn(Collections.emptyList());

        bookingCleanupService.cancelExpiredPendingBookings();

        verify(bookingRepository).findByStatusAndBookingDateBefore(
            eq(Booking.BookingStatus.PENDING), any(LocalDateTime.class));
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void testCancelExpiredPendingBookingsMultipleBookings() {
        Booking booking1 = new Booking();
        booking1.setBookingReference("BK11111111");
        booking1.setStatus(Booking.BookingStatus.PENDING);
        
        Booking booking2 = new Booking();
        booking2.setBookingReference("BK22222222");
        booking2.setStatus(Booking.BookingStatus.PENDING);
        
        List<Booking> expiredBookings = Arrays.asList(booking1, booking2);
        
        when(bookingRepository.findByStatusAndBookingDateBefore(
            eq(Booking.BookingStatus.PENDING), any(LocalDateTime.class)))
            .thenReturn(expiredBookings);
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking1, booking2);

        bookingCleanupService.cancelExpiredPendingBookings();

        verify(bookingRepository, times(2)).save(any(Booking.class));
    }
}