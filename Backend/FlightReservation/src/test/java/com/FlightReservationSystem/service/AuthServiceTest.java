package com.FlightReservationSystem.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.FlightReservationSystem.dto.LoginRequest;
import com.FlightReservationSystem.dto.RegisterRequest;
import com.FlightReservationSystem.entity.User;
import com.FlightReservationSystem.repository.UserRepository;
import com.FlightReservationSystem.security.JwtUtil;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;
    
    @Mock
    private PasswordEncoder passwordEncoder;
    
    @Mock
    private JwtUtil jwtUtil;
    
    @Mock
    private EmailValidationService emailValidationService;
    
    @Mock
    private OtpService otpService;

    @InjectMocks
    private AuthService authService;

    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;
    private User user;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest();
        registerRequest.setUsername("testuser");
        registerRequest.setEmail("test@gmail.com");
        registerRequest.setPassword("Password123!");
        registerRequest.setFirstName("Test");
        registerRequest.setLastName("User");
        registerRequest.setPhoneNumber("+1234567890");

        loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("Password123!");

        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("test@gmail.com");
        user.setEmailVerified(true);
        user.setRole(User.Role.CUSTOMER);
    }

    @Test
    void testRegisterSuccess() {
        when(emailValidationService.isValidEmail(anyString())).thenReturn(true);
        when(emailValidationService.isGmailAddress(anyString())).thenReturn(true);
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        Map<String, Object> result = authService.register(registerRequest);

        assertNotNull(result);
        assertTrue(result.containsKey("message"));
        verify(otpService).sendEmailOtp(anyString());
    }

    @Test
    void testLoginSuccess() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(jwtUtil.generateToken(anyString(), anyString())).thenReturn("jwt-token");

        Map<String, Object> result = authService.login(loginRequest);

        assertNotNull(result);
        assertTrue(result.containsKey("token"));
        assertTrue(result.containsKey("user"));
    }

    @Test
    void testVerifyEmailOtpSuccess() {
        when(otpService.verifyEmailOtp(anyString(), anyString())).thenReturn(true);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(jwtUtil.generateToken(anyString(), anyString())).thenReturn("jwt-token");

        Map<String, Object> result = authService.verifyEmailOtp("test@gmail.com", "123456");

        assertNotNull(result);
        assertTrue(result.containsKey("token"));
        assertTrue(result.containsKey("user"));
    }

    @Test
    void testRegisterInvalidEmail() {
        when(emailValidationService.isValidEmail(anyString())).thenReturn(false);

        assertThrows(RuntimeException.class, () -> authService.register(registerRequest));
    }

    @Test
    void testRegisterNonGmailEmail() {
        when(emailValidationService.isValidEmail(anyString())).thenReturn(true);
        when(emailValidationService.isGmailAddress(anyString())).thenReturn(false);

        assertThrows(RuntimeException.class, () -> authService.register(registerRequest));
    }

    @Test
    void testRegisterUsernameExists() {
        when(emailValidationService.isValidEmail(anyString())).thenReturn(true);
        when(emailValidationService.isGmailAddress(anyString())).thenReturn(true);
        when(userRepository.existsByUsername(anyString())).thenReturn(true);

        assertThrows(RuntimeException.class, () -> authService.register(registerRequest));
    }

    @Test
    void testLoginUnverifiedEmail() {
        user.setEmailVerified(false);
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        assertThrows(RuntimeException.class, () -> authService.login(loginRequest));
    }

    @Test
    void testResendEmailOtp() {
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        Map<String, Object> result = authService.resendEmailOtp("test@gmail.com");

        assertNotNull(result);
        assertTrue(result.containsKey("message"));
        verify(otpService).sendEmailOtp(anyString());
    }

    @Test
    void testLoginInvalidUsername() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> authService.login(loginRequest));
    }

    @Test
    void testLoginInvalidPassword() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        assertThrows(RuntimeException.class, () -> authService.login(loginRequest));
    }

    @Test
    void testRegisterAdminSuccess() {
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(jwtUtil.generateToken(anyString(), anyString())).thenReturn("jwt-token");

        Map<String, Object> result = authService.registerAdmin(registerRequest);

        assertNotNull(result);
        assertTrue(result.containsKey("token"));
    }

    @Test
    void testVerifyEmailOtpInvalid() {
        when(otpService.verifyEmailOtp(anyString(), anyString())).thenReturn(false);

        assertThrows(RuntimeException.class, () -> authService.verifyEmailOtp("test@gmail.com", "123456"));
    }

    @Test
    void testResendEmailOtpNotFound() {
        when(userRepository.existsByEmail(anyString())).thenReturn(false);

        assertThrows(RuntimeException.class, () -> authService.resendEmailOtp("test@gmail.com"));
    }
}