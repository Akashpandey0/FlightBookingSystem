package com.FlightReservationSystem.controller;

import com.FlightReservationSystem.entity.Booking;
import com.FlightReservationSystem.entity.Flight;
import com.FlightReservationSystem.service.BookingService;
import com.FlightReservationSystem.service.FlightService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class AdminController {
    
    private final FlightService flightService;
    private final BookingService bookingService;
    
    @PostMapping("/flights")
    public ResponseEntity<Flight> addFlight(@RequestBody Flight flight) {
        try {
            Flight savedFlight = flightService.addFlight(flight);
            return ResponseEntity.ok(savedFlight);
        } catch (Exception e) {
            throw new RuntimeException("Failed to add flight: " + e.getMessage());
        }
    }
    
    @PutMapping("/flights/{id}")
    public ResponseEntity<Flight> updateFlight(@PathVariable Long id, @Valid @RequestBody Flight flight) {
        return ResponseEntity.ok(flightService.updateFlight(id, flight));
    }
    
    @DeleteMapping("/flights/{id}")
    public ResponseEntity<Void> deleteFlight(@PathVariable Long id) {
        flightService.deleteFlight(id);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/bookings")
    public ResponseEntity<List<Booking>> getAllBookings() {
        return ResponseEntity.ok(bookingService.getAllBookings());
    }
}