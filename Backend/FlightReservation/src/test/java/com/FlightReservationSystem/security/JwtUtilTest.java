package com.FlightReservationSystem.security;

import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        // Set secret and expiration manually for testing
        ReflectionTestUtils.setField(jwtUtil, "secret", "mytestsecretkeymytestsecretkey123456"); // 32+ chars for HMAC-SHA256
        ReflectionTestUtils.setField(jwtUtil, "expiration", 3600000L); // 1 hour
    }

    @Test
    void testGenerateAndValidateToken() {
        String token = jwtUtil.generateToken("testUser", "USER");

        assertNotNull(token);
        assertTrue(jwtUtil.isTokenValid(token));
        assertEquals("testUser", jwtUtil.extractUsername(token));
        assertEquals("USER", jwtUtil.extractRole(token));
    }

    @Test
    void testInvalidToken() {
        String invalidToken = "invalid.token.value";

        assertFalse(jwtUtil.isTokenValid(invalidToken));
        assertThrows(JwtException.class, () -> jwtUtil.extractUsername(invalidToken));
    }

    @Test
    void testExpiredToken() throws InterruptedException {
        ReflectionTestUtils.setField(jwtUtil, "expiration", 1L); // 1 ms expiration
        String token = jwtUtil.generateToken("testUser", "USER");

        Thread.sleep(10); // Wait to ensure expiration

        assertFalse(jwtUtil.isTokenValid(token));
    }
}
