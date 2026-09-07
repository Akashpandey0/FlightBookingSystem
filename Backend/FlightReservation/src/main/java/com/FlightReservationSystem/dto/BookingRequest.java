package com.FlightReservationSystem.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class BookingRequest {
    @NotNull(message = "Flight ID is required")
    private Long flightId;
    
    @NotEmpty(message = "At least one passenger is required")
    @Valid
    private List<PassengerInfo> passengers;
    
    @NotBlank(message = "Seat class is required")
    private String seatClass; // ECONOMY or BUSINESS
    

    private String contactPhone;
}