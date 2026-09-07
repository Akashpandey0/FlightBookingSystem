package com.FlightReservationSystem.service;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@ConditionalOnMissingBean(name = "javaMailSender")
@Slf4j
public class MockEmailService extends EmailService {
    
    public MockEmailService() {
        super(null);
    }

    @Override
    public void sendVerificationOtp(String to, String otp) {
        log.info("=== EMAIL VERIFICATION OTP ===");
        log.info("To: {}", to);
        log.info("OTP: {}", otp);
        log.info("Enter this OTP in the verification form");
        log.info("==============================");
        System.out.println("\n=== EMAIL VERIFICATION OTP ===");
        System.out.println("To: " + to);
        System.out.println("OTP: " + otp);
        System.out.println("Enter this OTP in the verification form");
        System.out.println("==============================\n");
    }

    @Override
    public void sendBookingConfirmation(String to, String bookingReference, String flightDetails) {
        log.info("MOCK: Booking confirmation to {}: {} - {}", to, bookingReference, flightDetails);
        System.out.println("Booking confirmed: " + bookingReference + " for " + to);
    }
    
    @Override
    public void sendCancellationConfirmation(String to, String bookingReference, String refundAmount) {
        log.info("MOCK: Cancellation confirmation to {}: {} - Refund: ${}", to, bookingReference, refundAmount);
        System.out.println("\n=== BOOKING CANCELLED ===");
        System.out.println("To: " + to);
        System.out.println("Booking Reference: " + bookingReference);
        System.out.println("Refund Amount: $" + refundAmount);
        System.out.println("Refund will be credited within 5-7 business days");
        System.out.println("========================\n");
    }
}