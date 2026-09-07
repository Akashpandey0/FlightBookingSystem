package com.FlightReservationSystem.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.FlightReservationSystem.dto.LoginRequest;
import com.FlightReservationSystem.dto.RegisterRequest;
import com.FlightReservationSystem.entity.User;
import com.FlightReservationSystem.exception.InvalidCredentialsException;
import com.FlightReservationSystem.repository.UserRepository;
import com.FlightReservationSystem.security.JwtUtil;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final EmailValidationService emailValidationService;
    private final OtpService otpService;
    
    public Map<String, Object> register(RegisterRequest request) {
        // Validate email format
        if (!emailValidationService.isValidEmail(request.getEmail())) {
            throw new RuntimeException("Invalid email format");
        }
        
        // Check if email is Gmail
        if (!emailValidationService.isGmailAddress(request.getEmail())) {
            throw new RuntimeException("Only Gmail addresses are allowed");
        }
        
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhoneNumber(request.getPhoneNumber());
        
        userRepository.save(user);
        log.info("User registered successfully: {}", request.getUsername());
        
        // Send verification OTP
        otpService.sendEmailOtp(user.getEmail());
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Registration successful! Please check your email for the verification OTP.");
        response.put("email", user.getEmail());
        return response;
    }
    
    public Map<String, Object> login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid username"));
        
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid password");
        }
        
        if (user.getRole() == User.Role.CUSTOMER && !user.isEmailVerified()) {
            throw new RuntimeException("Please verify your email first");
        }
        
        String token = jwtUtil.generateToken(user.getUsername(), user.getRole().name());
        log.info("User logged in successfully: {}", request.getUsername());
        
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("user", user);
        return response;
    }
    
    public Map<String, Object> registerAdmin(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setRole(User.Role.ADMIN);
        
        userRepository.save(user);
        log.info("Admin registered successfully: {}", request.getUsername());
        
        String token = jwtUtil.generateToken(user.getUsername(), user.getRole().name());
        
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("user", user);
        return response;
    }
    
    public Map<String, Object> verifyEmailOtp(String email, String otp) {
        boolean verified = otpService.verifyEmailOtp(email, otp);
        
        if (!verified) {
            throw new RuntimeException("Invalid or expired OTP");
        }
        
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        String jwtToken = jwtUtil.generateToken(user.getUsername(), user.getRole().name());
        log.info("Email verified for user: {}", user.getUsername());
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Email verified successfully!");
        response.put("token", jwtToken);
        response.put("user", user);
        return response;
    }
    
    public Map<String, Object> resendEmailOtp(String email) {
        if (!userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email not found");
        }
        
        otpService.sendEmailOtp(email);
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "OTP sent successfully");
        return response;
    }
}