package com.FlightReservationSystem.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MockEmailServiceTest {

    private MockEmailService mockEmailService;

    @BeforeEach
    void setUp() {
        mockEmailService = new MockEmailService();
    }

    @Test
    void testSendVerificationOtp() {
        // Should not throw exception
        mockEmailService.sendVerificationOtp("test@gmail.com", "123456");
    }

    @Test
    void testSendBookingConfirmation() {
        // Should not throw exception
        mockEmailService.sendBookingConfirmation("test@gmail.com", "BK12345", "Flight details");
    }

    @Test
    void testSendCancellationConfirmation() {
        // Should not throw exception
        mockEmailService.sendCancellationConfirmation("test@gmail.com", "BK12345", "100.00");
    }
}