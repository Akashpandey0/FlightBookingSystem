package com.FlightReservationSystem.controller;

import com.FlightReservationSystem.dto.LoginRequest;
import com.FlightReservationSystem.dto.RegisterRequest;
import com.FlightReservationSystem.dto.EmailOtpRequest;
import com.FlightReservationSystem.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {
    
    private final AuthService authService;
    
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }
    
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
    
    @PostMapping("/register-admin")
    public ResponseEntity<Map<String, Object>> registerAdmin(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.registerAdmin(request));
    }
    
    @PostMapping("/verify-email-otp")
    public ResponseEntity<Map<String, Object>> verifyEmailOtp(@Valid @RequestBody EmailOtpRequest request) {
        return ResponseEntity.ok(authService.verifyEmailOtp(request.getEmail(), request.getOtp()));
    }
    
    @PostMapping("/resend-email-otp")
    public ResponseEntity<Map<String, Object>> resendEmailOtp(@RequestBody Map<String, String> request) {
        return ResponseEntity.ok(authService.resendEmailOtp(request.get("email")));
    }
    
    @PostMapping("/validate-email")
    public ResponseEntity<Map<String, Object>> validateEmail(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        Map<String, Object> response = new HashMap<>();
        
        if (email == null || email.trim().isEmpty()) {
            response.put("valid", false);
            response.put("message", "Email is required");
            return ResponseEntity.ok(response);
        }
        
        if (!email.toLowerCase().endsWith("@gmail.com")) {
            response.put("valid", false);
            response.put("message", "Only Gmail addresses are allowed");
            return ResponseEntity.ok(response);
        }
        
        response.put("valid", true);
        response.put("message", "Email is valid");
        return ResponseEntity.ok(response);
    }
}