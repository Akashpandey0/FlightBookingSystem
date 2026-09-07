package com.FlightReservationSystem.service;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailService emailService;

    @BeforeEach
    void setUp() {
        emailService = new EmailService(mailSender);
    }

    @Test
    void testSendVerificationOtp() {
        doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        emailService.sendVerificationOtp("test@gmail.com", "123456");

        verify(mailSender).send(any(SimpleMailMessage.class));
    }

    @Test
    void testSendBookingConfirmation() {
        doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        emailService.sendBookingConfirmation("test@gmail.com", "BK12345", "Flight details");

        verify(mailSender).send(any(SimpleMailMessage.class));
    }

    @Test
    void testSendCancellationConfirmation() {
        doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        emailService.sendCancellationConfirmation("test@gmail.com", "BK12345", "100.00");

        verify(mailSender).send(any(SimpleMailMessage.class));
    }

    @Test
    void testSendVerificationOtpException() {
        doThrow(new RuntimeException("Mail server error")).when(mailSender).send(any(SimpleMailMessage.class));

        // Should not throw exception, just log error
        emailService.sendVerificationOtp("test@gmail.com", "123456");

        verify(mailSender).send(any(SimpleMailMessage.class));
    }

    @Test
    void testSendBookingConfirmationException() {
        doThrow(new RuntimeException("Mail server error")).when(mailSender).send(any(SimpleMailMessage.class));

        // Should not throw exception, just log error
        emailService.sendBookingConfirmation("test@gmail.com", "BK12345", "Flight details");

        verify(mailSender).send(any(SimpleMailMessage.class));
    }

    @Test
    void testSendCancellationConfirmationException() {
        doThrow(new RuntimeException("Mail server error")).when(mailSender).send(any(SimpleMailMessage.class));

        // Should not throw exception, just log error
        emailService.sendCancellationConfirmation("test@gmail.com", "BK12345", "100.00");

        verify(mailSender).send(any(SimpleMailMessage.class));
    }
}