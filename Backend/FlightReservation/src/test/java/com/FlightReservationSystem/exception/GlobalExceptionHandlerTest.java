package com.FlightReservationSystem.exception;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void testHandleUserNotFound() {
        UserNotFoundException ex = new UserNotFoundException("User not found");
        ResponseEntity<Map<String, String>> response = handler.handleUserNotFound(ex);

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("User not found", response.getBody().get("error"));
    }

    @Test
    void testHandleFlightNotFound() {
        FlightNotFoundException ex = new FlightNotFoundException("Flight not found");
        ResponseEntity<Map<String, String>> response = handler.handleFlightNotFound(ex);

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Flight not found", response.getBody().get("error"));
    }

    @Test
    void testHandleBookingNotFound() {
        BookingNotFoundException ex = new BookingNotFoundException("Booking not found");
        ResponseEntity<Map<String, String>> response = handler.handleBookingNotFound(ex);

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Booking not found", response.getBody().get("error"));
    }

    @Test
    void testHandleInvalidCredentials() {
        InvalidCredentialsException ex = new InvalidCredentialsException("Invalid credentials");
        ResponseEntity<Map<String, String>> response = handler.handleInvalidCredentials(ex);

        assertEquals(401, response.getStatusCodeValue());
        assertEquals("Invalid credentials", response.getBody().get("error"));
    }

    @Test
    void testHandleRuntimeException() {
        RuntimeException ex = new RuntimeException("Runtime error");
        ResponseEntity<Map<String, String>> response = handler.handleRuntimeException(ex);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Runtime error", response.getBody().get("error"));
    }

    @Test
    void testHandleDataIntegrityException() {
        DataIntegrityViolationException ex = new DataIntegrityViolationException("Duplicate entry");
        ResponseEntity<Map<String, String>> response = handler.handleDataIntegrityException(ex);

        assertEquals(409, response.getStatusCodeValue());
        assertEquals("Data already exists or constraint violation", response.getBody().get("error"));
    }

    @Test
    void testHandleBadCredentials() {
        BadCredentialsException ex = new BadCredentialsException("Bad credentials");
        ResponseEntity<Map<String, String>> response = handler.handleBadCredentials(ex);

        assertEquals(401, response.getStatusCodeValue());
        assertEquals("Invalid username or password", response.getBody().get("error"));
    }

    @Test
    void testHandleAccessDenied() {
        AccessDeniedException ex = new AccessDeniedException("Access denied");
        ResponseEntity<Map<String, String>> response = handler.handleAccessDenied(ex);

        assertEquals(403, response.getStatusCodeValue());
        assertEquals("Access denied", response.getBody().get("error"));
    }

    @Test
    void testHandleGenericException() {
        Exception ex = new Exception("Unexpected error");
        ResponseEntity<Map<String, String>> response = handler.handleGenericException(ex);

        assertEquals(500, response.getStatusCodeValue());
        assertEquals("An unexpected error occurred", response.getBody().get("error"));
    }

    @Test
    void testHandleValidationExceptions() {
        FieldError fieldError = new FieldError("objectName", "fieldName", "must not be null");
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        when(ex.getBindingResult()).thenReturn(mock(org.springframework.validation.BindingResult.class));
        when(ex.getBindingResult().getAllErrors()).thenReturn(List.of(fieldError));

        ResponseEntity<Map<String, String>> response = handler.handleValidationExceptions(ex);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("must not be null", response.getBody().get("fieldName"));
    }
}
