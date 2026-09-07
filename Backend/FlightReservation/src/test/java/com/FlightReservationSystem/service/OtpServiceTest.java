package com.FlightReservationSystem.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.FlightReservationSystem.entity.User;
import com.FlightReservationSystem.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class OtpServiceTest {

    @Mock
    private UserRepository userRepository;
    
    @Mock
    private EmailService emailService;

    @InjectMocks
    private OtpService otpService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("test@gmail.com");
        user.setEmailOtp("123456");
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(5));
    }

    @Test
    void testGenerateOtp() {
        String otp = otpService.generateOtp();
        
        assertNotNull(otp);
        assertEquals(6, otp.length());
        assertTrue(otp.matches("\\d{6}"));
    }

    @Test
    void testSendEmailOtpSuccess() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        assertDoesNotThrow(() -> otpService.sendEmailOtp("test@gmail.com"));
        
        verify(emailService).sendVerificationOtp(anyString(), anyString());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testVerifyEmailOtpSuccess() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        boolean result = otpService.verifyEmailOtp("test@gmail.com", "123456");

        assertTrue(result);
        verify(userRepository, times(2)).save(any(User.class));
    }

    @Test
    void testVerifyEmailOtpExpired() {
        user.setOtpExpiry(LocalDateTime.now().minusMinutes(1));
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        boolean result = otpService.verifyEmailOtp("test@gmail.com", "123456");

        assertFalse(result);
    }

    @Test
    void testVerifyEmailOtpInvalid() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        boolean result = otpService.verifyEmailOtp("test@gmail.com", "654321");

        assertFalse(result);
    }

    @Test
    void testSendEmailOtpUserNotFound() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> otpService.sendEmailOtp("test@gmail.com"));
    }

    @Test
    void testVerifyEmailOtpUserNotFound() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> otpService.verifyEmailOtp("test@gmail.com", "123456"));
    }

    @Test
    void testVerifyEmailOtpNullOtp() {
        user.setEmailOtp(null);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        boolean result = otpService.verifyEmailOtp("test@gmail.com", "123456");

        assertFalse(result);
    }

    @Test
    void testVerifyEmailOtpNullExpiry() {
        user.setOtpExpiry(null);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        boolean result = otpService.verifyEmailOtp("test@gmail.com", "123456");

        assertFalse(result);
    }
}