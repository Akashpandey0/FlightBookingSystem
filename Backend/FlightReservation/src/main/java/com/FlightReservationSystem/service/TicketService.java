package com.FlightReservationSystem.service;

import com.FlightReservationSystem.entity.Booking;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

@Service
@Slf4j
public class TicketService {
    
    public byte[] generateTicket(Booking booking) {
        try {
            StringBuilder ticket = new StringBuilder();
            ticket.append("FLIGHT TICKET\n");
            ticket.append("=============\n\n");
            ticket.append("Booking Reference: ").append(booking.getBookingReference()).append("\n");
            ticket.append("Passenger: ").append(booking.getUser().getFirstName()).append(" ").append(booking.getUser().getLastName()).append("\n");
            ticket.append("Flight: ").append(booking.getFlight().getAirline()).append(" - ").append(booking.getFlight().getFlightNumber()).append("\n");
            ticket.append("Route: ").append(booking.getFlight().getSource()).append(" → ").append(booking.getFlight().getDestination()).append("\n");
            ticket.append("Departure: ").append(booking.getFlight().getDepartureTime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"))).append("\n");
            ticket.append("Arrival: ").append(booking.getFlight().getArrivalTime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"))).append("\n");
            ticket.append("Passengers: ").append(booking.getNumberOfPassengers()).append("\n");
            if (booking.getPassengerDetails() != null && !booking.getPassengerDetails().isEmpty()) {
                ticket.append("Passenger Details:\n").append(booking.getPassengerDetails()).append("\n");
            }
            ticket.append("Seat Class: ").append(booking.getSeatClass()).append("\n");
            ticket.append("Total Amount: ₹").append(booking.getTotalAmount()).append("\n");
            ticket.append("Status: ").append(booking.getStatus()).append("\n");
            
            log.info("Ticket generated for booking: {}", booking.getBookingReference());
            return ticket.toString().getBytes();
        } catch (Exception e) {
            log.error("Error generating ticket for booking: {}", booking.getBookingReference(), e);
            throw new RuntimeException("Failed to generate ticket");
        }
    }
}