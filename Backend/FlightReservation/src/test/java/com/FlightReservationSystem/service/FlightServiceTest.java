package com.FlightReservationSystem.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.FlightReservationSystem.dto.FlightSearchRequest;
import com.FlightReservationSystem.entity.Flight;
import com.FlightReservationSystem.repository.FlightRepository;

@ExtendWith(MockitoExtension.class)
class FlightServiceTest {

    @Mock
    private FlightRepository flightRepository;

    @InjectMocks
    private FlightService flightService;

    private Flight flight;
    private FlightSearchRequest searchRequest;

    @BeforeEach
    void setUp() {
        flight = new Flight();
        flight.setId(1L);
        flight.setFlightNumber("FL123");
        flight.setSource("NYC");
        flight.setDestination("LAX");
        flight.setDepartureTime(LocalDateTime.now().plusDays(1));
        flight.setEconomyPrice(BigDecimal.valueOf(100));

        searchRequest = new FlightSearchRequest();
        searchRequest.setSource("NYC");
        searchRequest.setDestination("LAX");
        searchRequest.setDepartureDate(LocalDateTime.now().plusDays(1).toLocalDate());
    }

    @Test
    void testGetAllFlights() {
        when(flightRepository.findAll()).thenReturn(List.of(flight));

        List<Flight> result = flightService.getAllFlights();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testGetFlightById() {
        when(flightRepository.findById(anyLong())).thenReturn(Optional.of(flight));

        Flight result = flightService.getFlightById(1L);

        assertNotNull(result);
        assertEquals("FL123", result.getFlightNumber());
    }

    @Test
    void testSearchFlights() {
        when(flightRepository.findBySourceAndDestinationAndDepartureTimeBetween(
            anyString(), anyString(), any(LocalDateTime.class), any(LocalDateTime.class)))
            .thenReturn(List.of(flight));

        List<Flight> result = flightService.searchFlights(searchRequest);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testSaveFlight() {
        when(flightRepository.save(any(Flight.class))).thenReturn(flight);

        Flight result = flightService.saveFlight(flight);

        assertNotNull(result);
        verify(flightRepository).save(flight);
    }

    @Test
    void testGetFlightByIdNotFound() {
        when(flightRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> flightService.getFlightById(1L));
    }

    @Test
    void testSearchFlightsWithAirline() {
        searchRequest.setAirline("Delta");
        when(flightRepository.findBySourceAndDestinationAndAirlineAndDepartureTimeBetween(
            anyString(), anyString(), anyString(), any(LocalDateTime.class), any(LocalDateTime.class)))
            .thenReturn(List.of(flight));

        List<Flight> result = flightService.searchFlights(searchRequest);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testDeleteFlight() {
        doNothing().when(flightRepository).deleteById(anyLong());

        flightService.deleteFlight(1L);

        verify(flightRepository).deleteById(1L);
    }

    @Test
    void testUpdateFlight() {
        when(flightRepository.findById(anyLong())).thenReturn(Optional.of(flight));
        when(flightRepository.save(any(Flight.class))).thenReturn(flight);

        Flight result = flightService.updateFlight(1L, flight);

        assertNotNull(result);
        verify(flightRepository).save(flight);
    }

    @Test
    void testUpdateFlightNotFound() {
        when(flightRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> flightService.updateFlight(1L, flight));
    }

    @Test
    void testSearchFlightsEmptyResult() {
        when(flightRepository.findBySourceAndDestinationAndDepartureTimeBetween(
            anyString(), anyString(), any(LocalDateTime.class), any(LocalDateTime.class)))
            .thenReturn(List.of());

        List<Flight> result = flightService.searchFlights(searchRequest);

        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    void testSearchFlightsWithNullAirline() {
        searchRequest.setAirline(null);
        when(flightRepository.findBySourceAndDestinationAndDepartureTimeBetween(
            anyString(), anyString(), any(LocalDateTime.class), any(LocalDateTime.class)))
            .thenReturn(List.of(flight));

        List<Flight> result = flightService.searchFlights(searchRequest);

        assertNotNull(result);
        assertEquals(1, result.size());
    }
}