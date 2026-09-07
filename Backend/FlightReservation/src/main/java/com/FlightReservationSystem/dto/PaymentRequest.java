package com.FlightReservationSystem.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PaymentRequest {
    @NotBlank(message = "Order ID is required")
    private String orderId;
    
    @NotBlank(message = "Payment ID is required")
    private String paymentId;
    
    @NotBlank(message = "Signature is required")
    private String signature;
    
    @NotBlank(message = "Booking reference is required")
    private String bookingReference;
}