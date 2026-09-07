package com.FlightReservationSystem.service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Slf4j
public class PaymentService {
    
    @Value("${razorpay.key.id}")
    private String keyId;
    
    @Value("${razorpay.key.secret}")
    private String keySecret;
    
    public String createOrder(BigDecimal amount, String bookingReference) throws Exception {
        try {
            // Ensure minimum amount (₹1 = 100 paise)
            int amountInPaise = amount.multiply(BigDecimal.valueOf(100)).intValue();
            if (amountInPaise < 100) {
                amountInPaise = 100; // Set minimum ₹1
            }
            
            RazorpayClient razorpay = new RazorpayClient(keyId, keySecret);
            
            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", amountInPaise);
            orderRequest.put("currency", "INR");
            orderRequest.put("receipt", bookingReference);
            
            Order order = razorpay.orders.create(orderRequest);
            String orderId = order.get("id").toString();
            log.info("Razorpay order created: {} for amount: ₹{}", orderId, amount);
            
            return orderId;
        } catch (Exception e) {
            log.error("Failed to create Razorpay order, using mock order: ", e);
            // Create mock order ID when Razorpay fails
            String mockOrderId = "order_mock_" + System.currentTimeMillis();
            log.info("Mock order created: {}", mockOrderId);
            return mockOrderId;
        }
    }
    
    public boolean verifyPayment(String orderId, String paymentId, String signature) {
        try {
            log.info("Verifying payment - OrderId: {}, PaymentId: {}", orderId, paymentId);
            
            // Manual signature verification using HMAC SHA256
            String payload = orderId + "|" + paymentId;
            String expectedSignature = calculateHMAC(payload, keySecret);
            
            boolean isValid = expectedSignature.equals(signature);
            log.info("Payment verification result for order: {} - {}", orderId, isValid ? "SUCCESS" : "FAILED");
            return isValid;
        } catch (Exception e) {
            log.error("Payment verification failed for order: {} - Error: {}", orderId, e.getMessage());
            return false;
        }
    }
    
    private String calculateHMAC(String data, String key) {
        try {
            javax.crypto.Mac mac = javax.crypto.Mac.getInstance("HmacSHA256");
            javax.crypto.spec.SecretKeySpec secretKeySpec = new javax.crypto.spec.SecretKeySpec(key.getBytes(), "HmacSHA256");
            mac.init(secretKeySpec);
            byte[] hash = mac.doFinal(data.getBytes());
            
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            log.error("Error calculating HMAC: ", e);
            return "";
        }
    }
}