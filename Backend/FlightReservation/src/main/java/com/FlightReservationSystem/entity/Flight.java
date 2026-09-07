package com.FlightReservationSystem.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "flights")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Flight number is required")
    @Column(unique = true)
    private String flightNumber;

    @NotBlank(message = "Airline is required")
    private String airline;

    @NotBlank(message = "Source is required")
    private String source;
    
    private String sourceAirport;

    @NotBlank(message = "Destination is required")
    private String destination;
    
    private String destinationAirport;

    @NotNull(message = "Departure time is required")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime departureTime;

    @NotNull(message = "Arrival time is required")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime arrivalTime;

    @Positive(message = "Economy price must be positive")
    private BigDecimal economyPrice;
    
    @Positive(message = "Business price must be positive")
    private BigDecimal businessPrice;

    @Positive(message = "Economy seats must be positive")
    private Integer economySeats;
    
    @Positive(message = "Business seats must be positive")
    private Integer businessSeats;

    private Integer availableEconomySeats;
    private Integer availableBusinessSeats;
    
    // Legacy fields for backward compatibility
    private BigDecimal price;
    private Integer totalSeats;
    private Integer availableSeats;

    @Enumerated(EnumType.STRING)
    private FlightStatus status = FlightStatus.SCHEDULED;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "flight", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Booking> bookings;

    public enum FlightStatus {
        SCHEDULED, DELAYED, CANCELLED, COMPLETED
    }
}