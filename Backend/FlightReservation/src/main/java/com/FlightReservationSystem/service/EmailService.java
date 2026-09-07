package com.FlightReservationSystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;
    
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    public void sendVerificationOtp(String to, String otp) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("harsh.work1910@gmail.com");
            message.setTo(to);
            message.setSubject("Email Verification OTP - Flight Booking System");
            message.setText("Dear User,\n\n" +
                    "Thank you for registering with Flight Booking System!\n\n" +
                    "Your email verification OTP is: " + otp + "\n\n" +
                    "This OTP will expire in 10 minutes.\n\n" +
                    "Best regards,\n" +
                    "Flight Booking Team");
            
            mailSender.send(message);
            log.info("Verification OTP sent to: {}", to);
        } catch (Exception e) {
            log.error("Failed to send verification OTP to {}: {}", to, e.getMessage());
            e.printStackTrace();
        }
    }

    @Async
    public void sendBookingConfirmation(String to, String bookingReference, String flightDetails) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("harsh.work1910@gmail.com");
            message.setTo(to);
            message.setSubject("Flight Booking Confirmation - " + bookingReference);
            message.setText("Your flight has been successfully booked!\n\n" +
                    "Booking Reference: " + bookingReference + "\n" +
                    "Flight Details: " + flightDetails + "\n\n" +
                    "Thank you for choosing our service!");
            
            mailSender.send(message);
            log.info("Booking confirmation sent to: {}", to);
        } catch (Exception e) {
            log.error("Failed to send booking confirmation to {}: {}", to, e.getMessage());
        }
    }
    
    @Async
    public void sendCancellationConfirmation(String to, String bookingReference, String refundAmount) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("harsh.work1910@gmail.com");
            message.setTo(to);
            message.setSubject("Booking Cancelled - Refund Processed - " + bookingReference);
            message.setText("Your booking has been successfully cancelled.\n\n" +
                    "Booking Reference: " + bookingReference + "\n" +
                    "Refund Amount: $" + refundAmount + "\n\n" +
                    "The refund amount has been processed and will be credited to your account within 5-7 business days.\n\n" +
                    "Thank you for using our service!");
            
            mailSender.send(message);
            log.info("Cancellation confirmation sent to: {}", to);
        } catch (Exception e) {
            log.error("Failed to send cancellation confirmation to {}: {}", to, e.getMessage());
        }
    }
}