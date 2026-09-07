package com.FlightReservationSystem.controller;

import com.FlightReservationSystem.dto.BookingRequest;
import com.FlightReservationSystem.entity.Booking;
import com.FlightReservationSystem.service.BookingService;
import com.FlightReservationSystem.service.TicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class BookingController {
    
    private final BookingService bookingService;
    private final TicketService ticketService;
    
    @PostMapping
    public ResponseEntity<Booking> bookFlight(@Valid @RequestBody BookingRequest request, 
                                            Authentication authentication) {
        try {
            Booking booking = bookingService.bookFlight(request, authentication.getName());
            return ResponseEntity.ok(booking);
        } catch (Exception e) {
            throw new RuntimeException("Booking failed: " + e.getMessage());
        }
    }
    
    @GetMapping("/my-bookings")
    public ResponseEntity<List<Booking>> getMyBookings(Authentication authentication) {
        return ResponseEntity.ok(bookingService.getUserBookings(authentication.getName()));
    }
    
    @PutMapping("/{bookingReference}/cancel")
    public ResponseEntity<Booking> cancelBooking(@PathVariable String bookingReference, 
                                               Authentication authentication) {
        return ResponseEntity.ok(bookingService.cancelBooking(bookingReference, authentication.getName()));
    }
    
    @GetMapping("/{bookingReference}/ticket")
    public ResponseEntity<byte[]> downloadTicket(@PathVariable String bookingReference,
                                               Authentication authentication) {
        try {
            Booking booking = bookingService.getBookingByReference(bookingReference, authentication.getName());
            byte[] ticketPdf = ticketService.generateTicket(booking);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.TEXT_PLAIN);
            headers.setContentDispositionFormData("attachment", "ticket-" + bookingReference + ".txt");
            
            return ResponseEntity.ok().headers(headers).body(ticketPdf);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate ticket: " + e.getMessage());
        }
    }
}