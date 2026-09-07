package com.FlightReservationSystem.controller;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import com.FlightReservationSystem.dto.FlightSearchRequest;
import com.FlightReservationSystem.entity.Flight;
import com.FlightReservationSystem.service.FlightService;

class FlightControllerTest {

    @Mock
    private FlightService flightService;

    @InjectMocks
    private FlightController flightController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSearchFlights() {
        FlightSearchRequest request = new FlightSearchRequest();
        Flight flight1 = new Flight();
        Flight flight2 = new Flight();
        List<Flight> flights = Arrays.asList(flight1, flight2);

        when(flightService.searchFlights(request)).thenReturn(flights);

        ResponseEntity<List<Flight>> response = flightController.searchFlights(request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void testGetAllFlights() {
        Flight flight1 = new Flight();
        Flight flight2 = new Flight();
        List<Flight> flights = Arrays.asList(flight1, flight2);

        when(flightService.getAllFlights()).thenReturn(flights);

        ResponseEntity<List<Flight>> response = flightController.getAllFlights();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void testGetFlightById() {
        Flight flight = new Flight();
        flight.setId(1L);

        when(flightService.getFlightById(1L)).thenReturn(flight);

        ResponseEntity<Flight> response = flightController.getFlightById(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1L, response.getBody().getId());
    }
}
