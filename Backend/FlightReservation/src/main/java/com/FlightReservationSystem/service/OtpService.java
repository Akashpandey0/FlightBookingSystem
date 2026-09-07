package com.FlightReservationSystem.service;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.FlightReservationSystem.entity.User;
import com.FlightReservationSystem.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class OtpService {
    
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final Random random = new Random();
    
    public String generateOtp() {
        return String.format("%06d", random.nextInt(1000000));
    }
    
    public void sendEmailOtp(String email) {
        String otp = generateOtp();
        LocalDateTime expiry = LocalDateTime.now().plusMinutes(10);
        
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        user.setEmailOtp(otp);
        user.setOtpExpiry(expiry);
        userRepository.save(user);
        
        emailService.sendVerificationOtp(email, otp);
        log.info("Email OTP sent to: {}", email);
    }
    
    public boolean verifyEmailOtp(String email, String otp) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        if (user.getEmailOtp() == null || user.getOtpExpiry() == null) {
            return false;
        }
        
        if (LocalDateTime.now().isAfter(user.getOtpExpiry())) {
            return false;
        }
        
        if (!user.getEmailOtp().equals(otp)) {
            return false;
        }
        
        user.setEmailVerified(true);
        user.setEmailOtp(null);
        user.setOtpExpiry(null);
        userRepository.save(user);
        
        return true;
    }
}