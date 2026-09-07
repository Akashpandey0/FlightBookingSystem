package com.FlightReservationSystem.controller;

import com.FlightReservationSystem.dto.EmailOtpRequest;
import com.FlightReservationSystem.dto.LoginRequest;
import com.FlightReservationSystem.dto.RegisterRequest;
import com.FlightReservationSystem.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegister() {
        RegisterRequest request = new RegisterRequest();
        Map<String, Object> expectedResponse = new HashMap<>();
        expectedResponse.put("status", "success");

        when(authService.register(request)).thenReturn(expectedResponse);

        ResponseEntity<Map<String, Object>> response = authController.register(request);
        assertEquals(expectedResponse, response.getBody());
    }

    @Test
    void testLogin() {
        LoginRequest request = new LoginRequest();
        Map<String, Object> expectedResponse = new HashMap<>();
        expectedResponse.put("token", "abc123");

        when(authService.login(request)).thenReturn(expectedResponse);

        ResponseEntity<Map<String, Object>> response = authController.login(request);
        assertEquals(expectedResponse, response.getBody());
    }

    @Test
    void testRegisterAdmin() {
        RegisterRequest request = new RegisterRequest();
        Map<String, Object> expectedResponse = new HashMap<>();
        expectedResponse.put("role", "admin");

        when(authService.registerAdmin(request)).thenReturn(expectedResponse);

        ResponseEntity<Map<String, Object>> response = authController.registerAdmin(request);
        assertEquals(expectedResponse, response.getBody());
    }

    @Test
    void testVerifyEmailOtp() {
        EmailOtpRequest request = new EmailOtpRequest();
        request.setEmail("test@gmail.com");
        request.setOtp("123456");

        Map<String, Object> expectedResponse = new HashMap<>();
        expectedResponse.put("verified", true);

        when(authService.verifyEmailOtp("test@gmail.com", "123456")).thenReturn(expectedResponse);

        ResponseEntity<Map<String, Object>> response = authController.verifyEmailOtp(request);
        assertEquals(expectedResponse, response.getBody());
    }

    @Test
    void testResendEmailOtp() {
        Map<String, String> request = new HashMap<>();
        request.put("email", "test@gmail.com");

        Map<String, Object> expectedResponse = new HashMap<>();
        expectedResponse.put("otpSent", true);

        when(authService.resendEmailOtp("test@gmail.com")).thenReturn(expectedResponse);

        ResponseEntity<Map<String, Object>> response = authController.resendEmailOtp(request);
        assertEquals(expectedResponse, response.getBody());
    }

    @Test
    void testValidateEmail_validGmail() {
        Map<String, String> request = new HashMap<>();
        request.put("email", "user@gmail.com");

        ResponseEntity<Map<String, Object>> response = authController.validateEmail(request);
        assertTrue((Boolean) response.getBody().get("valid"));
    }

    @Test
    void testValidateEmail_invalidDomain() {
        Map<String, String> request = new HashMap<>();
        request.put("email", "user@yahoo.com");

        ResponseEntity<Map<String, Object>> response = authController.validateEmail(request);
        assertFalse((Boolean) response.getBody().get("valid"));
        assertEquals("Only Gmail addresses are allowed", response.getBody().get("message"));
    }

    @Test
    void testValidateEmail_missingEmail() {
        Map<String, String> request = new HashMap<>();

        ResponseEntity<Map<String, Object>> response = authController.validateEmail(request);
        assertFalse((Boolean) response.getBody().get("valid"));
        assertEquals("Email is required", response.getBody().get("message"));
    }
}
