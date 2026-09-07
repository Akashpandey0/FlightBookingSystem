package com.FlightReservationSystem.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private RazorpayClient razorpayClient;

    @InjectMocks
    private PaymentService paymentService;

    @BeforeEach
    void setUp() throws Exception {
        paymentService = new PaymentService();
        // Use reflection to set the mock client
        java.lang.reflect.Field clientField = PaymentService.class.getDeclaredField("razorpayClient");
        clientField.setAccessible(true);
        clientField.set(paymentService, razorpayClient);
    }

    @Test
    void testCreateOrderSuccess() throws Exception {
        Order mockOrder = mock(Order.class);
        when(mockOrder.get("id")).thenReturn("order_123");
        when(razorpayClient.orders).thenReturn(mock(com.razorpay.Orders.class));
        when(razorpayClient.orders.create(any())).thenReturn(mockOrder);

        String result = paymentService.createOrder(BigDecimal.valueOf(100), "BK123");

        assertEquals("order_123", result);
    }

    @Test
    void testVerifyPaymentSuccess() {
        try (var mockedUtils = mockStatic(Utils.class)) {
            mockedUtils.when(() -> Utils.verifyPaymentSignature(any(), anyString()))
                      .thenReturn(true);

            boolean result = paymentService.verifyPayment("order_123", "payment_123", "signature_123");

            assertTrue(result);
        }
    }

    @Test
    void testVerifyPaymentFailure() {
        try (var mockedUtils = mockStatic(Utils.class)) {
            mockedUtils.when(() -> Utils.verifyPaymentSignature(any(), anyString()))
                      .thenReturn(false);

            boolean result = paymentService.verifyPayment("order_123", "payment_123", "signature_123");

            assertFalse(result);
        }
    }

    @Test
    void testCreateOrderException() throws Exception {
        when(razorpayClient.orders).thenReturn(mock(com.razorpay.Orders.class));
        when(razorpayClient.orders.create(any())).thenThrow(new RazorpayException("API Error"));

        assertThrows(RuntimeException.class, () -> 
            paymentService.createOrder(BigDecimal.valueOf(100), "BK123"));
    }

    @Test
    void testVerifyPaymentException() {
        try (var mockedUtils = mockStatic(Utils.class)) {
            mockedUtils.when(() -> Utils.verifyPaymentSignature(any(), anyString()))
                      .thenThrow(new RuntimeException("Signature verification failed"));

            boolean result = paymentService.verifyPayment("order_123", "payment_123", "signature_123");

            assertFalse(result);
        }
    }
}