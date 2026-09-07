package com.FlightReservationSystem.repository;

import com.FlightReservationSystem.entity.Booking;
import com.FlightReservationSystem.entity.Flight;
import com.FlightReservationSystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUser(User user);
    Optional<Booking> findByBookingReference(String bookingReference);
    List<Booking> findByUserOrderByBookingDateDesc(User user);
    List<Booking> findByStatusAndBookingDateBefore(Booking.BookingStatus status, LocalDateTime cutoffTime);
    List<Booking> findByFlightAndStatus(Flight flight, Booking.BookingStatus status);
}