package com.FlightReservationSystem.service;

import com.FlightReservationSystem.dto.FlightSearchRequest;
import com.FlightReservationSystem.entity.Flight;
import com.FlightReservationSystem.repository.FlightRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FlightService {
    
    private final FlightRepository flightRepository;
    
    public List<Flight> searchFlights(FlightSearchRequest request) {
        if (request.getSource() != null && request.getDestination() != null && request.getDepartureDate() != null) {
            LocalDateTime searchDate = request.getDepartureDate().atStartOfDay();
            return flightRepository.findBySourceDestinationAndDateAndStatusNot(
                request.getSource(), request.getDestination(), searchDate, Flight.FlightStatus.COMPLETED);
        } else if (request.getSource() != null && request.getDestination() != null) {
            return flightRepository.findBySourceAndDestinationAndStatusNot(request.getSource(), request.getDestination(), Flight.FlightStatus.COMPLETED);
        } else if (request.getAirline() != null) {
            return flightRepository.findByAirlineAndStatusNot(request.getAirline(), Flight.FlightStatus.COMPLETED);
        }
        
        return flightRepository.findByAvailableSeatsGreaterThanAndStatusNot(0, Flight.FlightStatus.COMPLETED);
    }
    
    public List<Flight> getAllFlights() {
        return flightRepository.findAll();
    }
    
    public Flight getFlightById(Long id) {
        return flightRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Flight not found"));
    }
    
    public Flight addFlight(Flight flight) {
        if (flight.getAvailableSeats() == null) {
            flight.setAvailableSeats(flight.getTotalSeats());
        }
        if (flight.getStatus() == null) {
            flight.setStatus(Flight.FlightStatus.SCHEDULED);
        }
        Flight savedFlight = flightRepository.save(flight);
        log.info("Flight added: {}", savedFlight.getFlightNumber());
        return savedFlight;
    }
    
    public Flight updateFlight(Long id, Flight flightDetails) {
        Flight flight = getFlightById(id);
        
        flight.setFlightNumber(flightDetails.getFlightNumber());
        flight.setAirline(flightDetails.getAirline());
        flight.setSource(flightDetails.getSource());
        flight.setDestination(flightDetails.getDestination());
        flight.setDepartureTime(flightDetails.getDepartureTime());
        flight.setArrivalTime(flightDetails.getArrivalTime());
        flight.setPrice(flightDetails.getPrice());
        flight.setTotalSeats(flightDetails.getTotalSeats());
        
        // Update seat class fields
        flight.setEconomyPrice(flightDetails.getEconomyPrice());
        flight.setBusinessPrice(flightDetails.getBusinessPrice());
        flight.setEconomySeats(flightDetails.getEconomySeats());
        flight.setBusinessSeats(flightDetails.getBusinessSeats());
        flight.setAvailableEconomySeats(flightDetails.getAvailableEconomySeats());
        flight.setAvailableBusinessSeats(flightDetails.getAvailableBusinessSeats());
        
        if (flightDetails.getStatus() != null) {
            flight.setStatus(flightDetails.getStatus());
        }
        // Update available seats if total seats changed
        if (flight.getAvailableSeats() == null) {
            flight.setAvailableSeats(flightDetails.getTotalSeats());
        }
        
        Flight updatedFlight = flightRepository.save(flight);
        log.info("Flight updated: {}", updatedFlight.getFlightNumber());
        return updatedFlight;
    }
    
    public void deleteFlight(Long id) {
        Flight flight = getFlightById(id);
        flightRepository.delete(flight);
        log.info("Flight deleted: {}", flight.getFlightNumber());
    }
    
    public Flight saveFlight(Flight flight) {
        return flightRepository.save(flight);
    }
}