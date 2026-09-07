package com.FlightReservationSystem.repository;

import com.FlightReservationSystem.entity.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {
    List<Flight> findBySourceAndDestinationAndStatusNot(String source, String destination, Flight.FlightStatus status);
    
    @Query("SELECT f FROM Flight f WHERE f.source = :source AND f.destination = :destination AND DATE(f.departureTime) = DATE(:date) AND f.status != :status")
    List<Flight> findBySourceDestinationAndDateAndStatusNot(@Param("source") String source, 
                                               @Param("destination") String destination, 
                                               @Param("date") LocalDateTime date,
                                               @Param("status") Flight.FlightStatus status);
    
    List<Flight> findByAirlineAndStatusNot(String airline, Flight.FlightStatus status);
    List<Flight> findByAvailableSeatsGreaterThanAndStatusNot(Integer seats, Flight.FlightStatus status);
    
    // Keep original methods for admin dashboard
    List<Flight> findBySourceAndDestination(String source, String destination);
    
    @Query("SELECT f FROM Flight f WHERE f.source = :source AND f.destination = :destination AND DATE(f.departureTime) = DATE(:date)")
    List<Flight> findBySourceDestinationAndDate(@Param("source") String source, 
                                               @Param("destination") String destination, 
                                               @Param("date") LocalDateTime date);
    
    List<Flight> findByAirline(String airline);
    List<Flight> findByAvailableSeatsGreaterThan(Integer seats);
}