package com.FlightReservationSystem.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EmailValidationServiceTest {

    private EmailValidationService emailValidationService;

    @BeforeEach
    void setUp() {
        emailValidationService = new EmailValidationService();
    }

    @Test
    void testIsValidEmailValid() {
        assertTrue(emailValidationService.isValidEmail("test@gmail.com"));
        assertTrue(emailValidationService.isValidEmail("user.name@domain.co.uk"));
        assertTrue(emailValidationService.isValidEmail("test123@example.org"));
    }

    @Test
    void testIsValidEmailInvalid() {
        assertFalse(emailValidationService.isValidEmail(null));
        assertFalse(emailValidationService.isValidEmail(""));
        assertFalse(emailValidationService.isValidEmail("   "));
        assertFalse(emailValidationService.isValidEmail("invalid-email"));
        assertFalse(emailValidationService.isValidEmail("@gmail.com"));
        assertFalse(emailValidationService.isValidEmail("test@"));
    }

    @Test
    void testIsGmailAddressValid() {
        assertTrue(emailValidationService.isGmailAddress("test@gmail.com"));
        assertTrue(emailValidationService.isGmailAddress("USER@GMAIL.COM"));
        assertTrue(emailValidationService.isGmailAddress("test.user@gmail.com"));
    }

    @Test
    void testIsGmailAddressInvalid() {
        assertFalse(emailValidationService.isGmailAddress(null));
        assertFalse(emailValidationService.isGmailAddress("test@yahoo.com"));
        assertFalse(emailValidationService.isGmailAddress("test@hotmail.com"));
        assertFalse(emailValidationService.isGmailAddress("test@example.com"));
        assertFalse(emailValidationService.isGmailAddress(""));
        assertFalse(emailValidationService.isGmailAddress("   "));
    }

    @Test
    void testIsValidEmailEdgeCases() {
        assertFalse(emailValidationService.isValidEmail("test@.com"));
        assertFalse(emailValidationService.isValidEmail("test..test@gmail.com"));
        assertFalse(emailValidationService.isValidEmail(".test@gmail.com"));
        assertFalse(emailValidationService.isValidEmail("test.@gmail.com"));
        assertTrue(emailValidationService.isValidEmail("test+tag@gmail.com"));
        assertTrue(emailValidationService.isValidEmail("test_user@gmail.com"));
    }
}