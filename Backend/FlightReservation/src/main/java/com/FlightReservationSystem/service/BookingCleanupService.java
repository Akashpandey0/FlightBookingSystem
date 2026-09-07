package com.FlightReservationSystem.service;

import com.FlightReservationSystem.entity.Booking;
import com.FlightReservationSystem.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingCleanupService {
    
    private final BookingRepository bookingRepository;
    
    @Scheduled(fixedRate = 30000) // Run every 30 seconds
    @Transactional
    public void cancelExpiredPendingBookings() {
        LocalDateTime cutoffTime = LocalDateTime.now().minusMinutes(1); // 1 minute timeout
        
        List<Booking> expiredBookings = bookingRepository.findByStatusAndBookingDateBefore(
            Booking.BookingStatus.PENDING, cutoffTime);
        
        for (Booking booking : expiredBookings) {
            booking.setStatus(Booking.BookingStatus.CANCELLED);
            booking.setPaymentStatus(Booking.PaymentStatus.FAILED);
            bookingRepository.save(booking);
            
            log.info("Auto-cancelled expired pending booking: {}", booking.getBookingReference());
        }
        
        if (!expiredBookings.isEmpty()) {
            log.info("Auto-cancelled {} expired pending bookings", expiredBookings.size());
        }
    }
}