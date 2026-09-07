package com.FlightReservationSystem.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.FlightReservationSystem.entity.Booking;
import com.FlightReservationSystem.entity.Flight;
import com.FlightReservationSystem.entity.User;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    @InjectMocks
    private TicketService ticketService;

    private Booking booking;
    private Flight flight;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");

        flight = new Flight();
        flight.setFlightNumber("FL123");
        flight.setAirline("Test Airlines");
        flight.setSource("NYC");
        flight.setDestination("LAX");
        flight.setDepartureTime(LocalDateTime.of(2024, 12, 25, 10, 30));
        flight.setArrivalTime(LocalDateTime.of(2024, 12, 25, 13, 45));

        booking = new Booking();
        booking.setBookingReference("BK12345678");
        booking.setUser(user);
        booking.setFlight(flight);
        booking.setNumberOfPassengers(1);
        booking.setPassengerDetails("John Doe, Age: 30, Gender: Male");
        booking.setSeatClass(Booking.SeatClass.ECONOMY);
        booking.setTotalAmount(BigDecimal.valueOf(150.00));
        booking.setStatus(Booking.BookingStatus.CONFIRMED);
    }

    @Test
    void testGenerateTicketSuccess() {
        byte[] ticket = ticketService.generateTicket(booking);

        assertNotNull(ticket);
        assertTrue(ticket.length > 0);
        
        String ticketContent = new String(ticket);
        assertTrue(ticketContent.contains("FLIGHT TICKET"));
        assertTrue(ticketContent.contains("BK12345678"));
        assertTrue(ticketContent.contains("John Doe"));
        assertTrue(ticketContent.contains("FL123"));
        assertTrue(ticketContent.contains("NYC → LAX"));
        assertTrue(ticketContent.contains("ECONOMY"));
        assertTrue(ticketContent.contains("150.00"));
    }

    @Test
    void testGenerateTicketWithNullPassengerDetails() {
        booking.setPassengerDetails(null);

        byte[] ticket = ticketService.generateTicket(booking);

        assertNotNull(ticket);
        String ticketContent = new String(ticket);
        assertTrue(ticketContent.contains("FLIGHT TICKET"));
        assertFalse(ticketContent.contains("Passenger Details:"));
    }

    @Test
    void testGenerateTicketWithEmptyPassengerDetails() {
        booking.setPassengerDetails("");

        byte[] ticket = ticketService.generateTicket(booking);

        assertNotNull(ticket);
        String ticketContent = new String(ticket);
        assertTrue(ticketContent.contains("FLIGHT TICKET"));
        assertFalse(ticketContent.contains("Passenger Details:"));
    }

    @Test
    void testGenerateTicketWithBusinessClass() {
        booking.setSeatClass(Booking.SeatClass.BUSINESS);
        booking.setTotalAmount(BigDecimal.valueOf(300.00));

        byte[] ticket = ticketService.generateTicket(booking);

        assertNotNull(ticket);
        String ticketContent = new String(ticket);
        assertTrue(ticketContent.contains("BUSINESS"));
        assertTrue(ticketContent.contains("300.00"));
    }

    @Test
    void testGenerateTicketWithMultiplePassengers() {
        booking.setNumberOfPassengers(2);
        booking.setPassengerDetails("John Doe, Age: 30, Gender: Male; Jane Doe, Age: 28, Gender: Female");

        byte[] ticket = ticketService.generateTicket(booking);

        assertNotNull(ticket);
        String ticketContent = new String(ticket);
        assertTrue(ticketContent.contains("Passengers: 2"));
        assertTrue(ticketContent.contains("John Doe"));
        assertTrue(ticketContent.contains("Jane Doe"));
    }

    @Test
    void testGenerateTicketException() {
        // Create a booking with null flight to cause exception
        booking.setFlight(null);

        assertThrows(RuntimeException.class, () -> ticketService.generateTicket(booking));
    }
}