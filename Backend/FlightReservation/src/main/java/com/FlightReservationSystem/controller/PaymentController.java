package com.FlightReservationSystem.controller;

import com.FlightReservationSystem.dto.PaymentRequest;
import com.FlightReservationSystem.entity.Booking;
import com.FlightReservationSystem.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class PaymentController {
    
    private final BookingService bookingService;
    
    @PostMapping("/verify")
    public ResponseEntity<Booking> verifyPayment( @RequestBody PaymentRequest request) {
        try {
            Booking booking = bookingService.verifyPayment(
                request.getBookingReference(),
                request.getOrderId(),
                request.getPaymentId(),
                request.getSignature()
            );
            return ResponseEntity.ok(booking);
        } catch (Exception e) {
            throw new RuntimeException("Payment verification failed: " + e.getMessage());
        }
    }
}