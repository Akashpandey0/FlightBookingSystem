package com.FlightReservationSystem.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class PassengerInfo {
    @NotBlank(message = "Passenger name is required")
    private String name;
    
    @NotBlank(message = "Age is required")
    private String age;
    
    @NotBlank(message = "Gender is required")
    @Pattern(regexp = "^(Male|Female|Other)$", message = "Gender must be Male, Female, or Other")
    private String gender;
    
    private String seatPreference; // Window, Aisle, Middle
}