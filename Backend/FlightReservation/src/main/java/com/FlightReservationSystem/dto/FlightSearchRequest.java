package com.FlightReservationSystem.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class FlightSearchRequest {
    private String source;
    private String destination;
    private LocalDate departureDate;
    private String airline;
    private Integer passengers = 1;
}