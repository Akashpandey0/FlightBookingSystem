package com.FlightReservationSystem.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class BookingNotFoundExceptionTest {

    @Test
    void testExceptionMessage() {
        String errorMessage = "Booking with reference BR123 not found";
        BookingNotFoundException exception = new BookingNotFoundException(errorMessage);

        assertEquals(errorMessage, exception.getMessage());
    }
}
