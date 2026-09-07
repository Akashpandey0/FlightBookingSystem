package com.FlightReservationSystem.controller;

import com.FlightReservationSystem.dto.FlightSearchRequest;
import com.FlightReservationSystem.entity.Flight;
import com.FlightReservationSystem.service.FlightService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/flights")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class FlightController {
    
    private final FlightService flightService;
    
    @PostMapping("/search")
    public ResponseEntity<List<Flight>> searchFlights(@RequestBody FlightSearchRequest request) {
        return ResponseEntity.ok(flightService.searchFlights(request));
    }
    
    @GetMapping
    public ResponseEntity<List<Flight>> getAllFlights() {
        return ResponseEntity.ok(flightService.getAllFlights());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Flight> getFlightById(@PathVariable Long id) {
        return ResponseEntity.ok(flightService.getFlightById(id));
    }
}