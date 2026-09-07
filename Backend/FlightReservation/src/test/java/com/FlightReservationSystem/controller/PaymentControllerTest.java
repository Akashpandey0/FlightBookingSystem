package com.FlightReservationSystem.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import com.FlightReservationSystem.dto.PaymentRequest;
import com.FlightReservationSystem.entity.Booking;
import com.FlightReservationSystem.service.BookingService;

class PaymentControllerTest {

    @Mock
    private BookingService bookingService;

    @InjectMocks
    private PaymentController paymentController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testVerifyPayment() {
        PaymentRequest request = new PaymentRequest();
        request.setBookingReference("BR123");
        request.setOrderId("ORD456");
        request.setPaymentId("PAY789");
        request.setSignature("signature123");

        Booking booking = new Booking();
        booking.setBookingReference("BR123");

        when(bookingService.verifyPayment(
                request.getBookingReference(),
                request.getOrderId(),
                request.getPaymentId(),
                request.getSignature()
        )).thenReturn(booking);

        ResponseEntity<Booking> response = paymentController.verifyPayment(request);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("BR123", response.getBody().getBookingReference());
    }
}
