package com.FlightReservationSystem.service;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.FlightReservationSystem.entity.Booking;
import com.FlightReservationSystem.entity.Flight;
import com.FlightReservationSystem.repository.BookingRepository;
import com.FlightReservationSystem.repository.FlightRepository;

@ExtendWith(MockitoExtension.class)
class FlightStatusServiceTest {

    @Mock
    private FlightRepository flightRepository;
    
    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private FlightStatusService flightStatusService;

    private Flight completedFlight;
    private Flight scheduledFlight;
    private Booking confirmedBooking;

    @BeforeEach
    void setUp() {
        completedFlight = new Flight();
        completedFlight.setId(1L);
        completedFlight.setFlightNumber("FL123");
        completedFlight.setStatus(Flight.FlightStatus.SCHEDULED);
        completedFlight.setArrivalTime(LocalDateTime.now().minusHours(1));

        scheduledFlight = new Flight();
        scheduledFlight.setId(2L);
        scheduledFlight.setFlightNumber("FL456");
        scheduledFlight.setStatus(Flight.FlightStatus.SCHEDULED);
        scheduledFlight.setArrivalTime(LocalDateTime.now().plusHours(1));

        confirmedBooking = new Booking();
        confirmedBooking.setId(1L);
        confirmedBooking.setBookingReference("BK12345678");
        confirmedBooking.setStatus(Booking.BookingStatus.CONFIRMED);
        confirmedBooking.setFlight(completedFlight);
    }

    @Test
    void testUpdateFlightStatusesWithCompletedFlight() {
        List<Flight> flights = Arrays.asList(completedFlight, scheduledFlight);
        List<Booking> confirmedBookings = Arrays.asList(confirmedBooking);
        
        when(flightRepository.findAll()).thenReturn(flights);
        when(flightRepository.save(any(Flight.class))).thenReturn(completedFlight);
        when(bookingRepository.findByFlightAndStatus(eq(completedFlight), eq(Booking.BookingStatus.CONFIRMED)))
            .thenReturn(confirmedBookings);
        when(bookingRepository.save(any(Booking.class))).thenReturn(confirmedBooking);

        flightStatusService.updateFlightStatuses();

        verify(flightRepository).save(completedFlight);
        verify(bookingRepository).save(confirmedBooking);
    }

    @Test
    void testUpdateFlightStatusesWithNoCompletedFlights() {
        List<Flight> flights = Arrays.asList(scheduledFlight);
        
        when(flightRepository.findAll()).thenReturn(flights);

        flightStatusService.updateFlightStatuses();

        verify(flightRepository, never()).save(any(Flight.class));
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void testUpdateFlightStatusesWithNoFlights() {
        when(flightRepository.findAll()).thenReturn(Collections.emptyList());

        flightStatusService.updateFlightStatuses();

        verify(flightRepository, never()).save(any(Flight.class));
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void testUpdateFlightStatusesWithNoConfirmedBookings() {
        List<Flight> flights = Arrays.asList(completedFlight);
        
        when(flightRepository.findAll()).thenReturn(flights);
        when(flightRepository.save(any(Flight.class))).thenReturn(completedFlight);
        when(bookingRepository.findByFlightAndStatus(eq(completedFlight), eq(Booking.BookingStatus.CONFIRMED)))
            .thenReturn(Collections.emptyList());

        flightStatusService.updateFlightStatuses();

        verify(flightRepository).save(completedFlight);
        verify(bookingRepository, never()).save(any(Booking.class));
    }
}