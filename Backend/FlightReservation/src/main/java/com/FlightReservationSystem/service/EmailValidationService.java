package com.FlightReservationSystem.service;

import org.springframework.stereotype.Service;
import java.util.regex.Pattern;

@Service
public class EmailValidationService {
    
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
    );
    
    public boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }
    
    public boolean isGmailAddress(String email) {
        return email != null && email.toLowerCase().endsWith("@gmail.com");
    }
}