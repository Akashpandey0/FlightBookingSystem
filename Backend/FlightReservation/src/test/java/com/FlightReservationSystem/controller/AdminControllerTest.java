package com.FlightReservationSystem.controller;

import com.FlightReservationSystem.entity.Booking;
import com.FlightReservationSystem.entity.Flight;
import com.FlightReservationSystem.service.BookingService;
import com.FlightReservationSystem.service.FlightService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdminControllerTest {

    @Mock
    private FlightService flightService;

    @Mock
    private BookingService bookingService;

    @InjectMocks
    private AdminController adminController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddFlight() {
        Flight flight = new Flight();
        flight.setId(1L);
        flight.setFlightNumber("AI101");

        when(flightService.addFlight(flight)).thenReturn(flight);

        ResponseEntity<Flight> response = adminController.addFlight(flight);
        assertEquals(flight, response.getBody());
        verify(flightService, times(1)).addFlight(flight);
    }

    @Test
    void testUpdateFlight() {
        Flight flight = new Flight();
        flight.setId(1L);
        flight.setFlightNumber("AI102");

        when(flightService.updateFlight(1L, flight)).thenReturn(flight);

        ResponseEntity<Flight> response = adminController.updateFlight(1L, flight);
        assertEquals(flight, response.getBody());
        verify(flightService, times(1)).updateFlight(1L, flight);
    }

    @Test
    void testDeleteFlight() {
        doNothing().when(flightService).deleteFlight(1L);

        ResponseEntity<Void> response = adminController.deleteFlight(1L);
        assertEquals(200, response.getStatusCode().value());
        verify(flightService, times(1)).deleteFlight(1L);
    }

    @Test
    void testGetAllBookings() {
        Booking booking1 = new Booking();
        Booking booking2 = new Booking();
        List<Booking> bookings = Arrays.asList(booking1, booking2);

        when(bookingService.getAllBookings()).thenReturn(bookings);

        ResponseEntity<List<Booking>> response = adminController.getAllBookings();
        assertEquals(bookings, response.getBody());
        verify(bookingService, times(1)).getAllBookings();
    }
}
