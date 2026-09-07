package com.FlightReservationSystem.controller;

import com.FlightReservationSystem.dto.BookingRequest;
import com.FlightReservationSystem.entity.Booking;
import com.FlightReservationSystem.service.BookingService;
import com.FlightReservationSystem.service.TicketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookingControllerTest {

    @Mock
    private BookingService bookingService;

    @Mock
    private TicketService ticketService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private BookingController bookingController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testBookFlight() {
        BookingRequest request = new BookingRequest();
        Booking booking = new Booking();
        booking.setBookingReference("REF123");

        when(authentication.getName()).thenReturn("user@example.com");
        when(bookingService.bookFlight(request, "user@example.com")).thenReturn(booking);

        ResponseEntity<Booking> response = bookingController.bookFlight(request, authentication);
        assertEquals(booking, response.getBody());
        verify(bookingService).bookFlight(request, "user@example.com");
    }

    @Test
    void testGetMyBookings() {
        Booking booking1 = new Booking();
        Booking booking2 = new Booking();
        List<Booking> bookings = Arrays.asList(booking1, booking2);

        when(authentication.getName()).thenReturn("user@example.com");
        when(bookingService.getUserBookings("user@example.com")).thenReturn(bookings);

        ResponseEntity<List<Booking>> response = bookingController.getMyBookings(authentication);
        assertEquals(bookings, response.getBody());
        verify(bookingService).getUserBookings("user@example.com");
    }

    @Test
    void testCancelBooking() {
        Booking booking = new Booking();
        booking.setBookingReference("REF123");

        when(authentication.getName()).thenReturn("user@example.com");
        when(bookingService.cancelBooking("REF123", "user@example.com")).thenReturn(booking);

        ResponseEntity<Booking> response = bookingController.cancelBooking("REF123", authentication);
        assertEquals(booking, response.getBody());
        verify(bookingService).cancelBooking("REF123", "user@example.com");
    }

    @Test
    void testDownloadTicket() {
        Booking booking = new Booking();
        booking.setBookingReference("REF123");
        byte[] ticketPdf = "Sample Ticket Content".getBytes();

        when(authentication.getName()).thenReturn("user@example.com");
        when(bookingService.getBookingByReference("REF123", "user@example.com")).thenReturn(booking);
        when(ticketService.generateTicket(booking)).thenReturn(ticketPdf);

        ResponseEntity<byte[]> response = bookingController.downloadTicket("REF123", authentication);
        assertArrayEquals(ticketPdf, response.getBody());
        assertEquals(MediaType.TEXT_PLAIN, response.getHeaders().getContentType());
        assertTrue(response.getHeaders().getContentDisposition().getFilename().contains("ticket-REF123.txt"));
    }

    @Test
    void testBookFlightException() {
        BookingRequest request = new BookingRequest();

        when(authentication.getName()).thenReturn("user@example.com");
        when(bookingService.bookFlight(request, "user@example.com")).thenThrow(new RuntimeException("Service error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            bookingController.bookFlight(request, authentication);
        });
        
        assertTrue(exception.getMessage().contains("Booking failed: Service error"));
        verify(bookingService).bookFlight(request, "user@example.com");
    }

    @Test
    void testBookFlightSuccess() {
        BookingRequest request = new BookingRequest();
        Booking booking = new Booking();
        booking.setBookingReference("REF123");
        booking.setId(1L);

        when(authentication.getName()).thenReturn("user@example.com");
        when(bookingService.bookFlight(request, "user@example.com")).thenReturn(booking);

        ResponseEntity<Booking> response = bookingController.bookFlight(request, authentication);
        
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(booking, response.getBody());
        assertEquals("REF123", response.getBody().getBookingReference());
        verify(bookingService).bookFlight(request, "user@example.com");
    }

    @Test
    void testGetMyBookingsEmpty() {
        List<Booking> emptyBookings = Arrays.asList();

        when(authentication.getName()).thenReturn("user@example.com");
        when(bookingService.getUserBookings("user@example.com")).thenReturn(emptyBookings);

        ResponseEntity<List<Booking>> response = bookingController.getMyBookings(authentication);
        
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(emptyBookings, response.getBody());
        assertTrue(response.getBody().isEmpty());
        verify(bookingService).getUserBookings("user@example.com");
    }

    @Test
    void testGetMyBookingsMultiple() {
        Booking booking1 = new Booking();
        booking1.setBookingReference("REF123");
        Booking booking2 = new Booking();
        booking2.setBookingReference("REF456");
        Booking booking3 = new Booking();
        booking3.setBookingReference("REF789");
        List<Booking> bookings = Arrays.asList(booking1, booking2, booking3);

        when(authentication.getName()).thenReturn("user@example.com");
        when(bookingService.getUserBookings("user@example.com")).thenReturn(bookings);

        ResponseEntity<List<Booking>> response = bookingController.getMyBookings(authentication);
        
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(3, response.getBody().size());
        assertEquals("REF123", response.getBody().get(0).getBookingReference());
        assertEquals("REF456", response.getBody().get(1).getBookingReference());
        assertEquals("REF789", response.getBody().get(2).getBookingReference());
        verify(bookingService).getUserBookings("user@example.com");
    }

    @Test
    void testCancelBookingSuccess() {
        Booking booking = new Booking();
        booking.setBookingReference("REF123");
        booking.setId(1L);

        when(authentication.getName()).thenReturn("user@example.com");
        when(bookingService.cancelBooking("REF123", "user@example.com")).thenReturn(booking);

        ResponseEntity<Booking> response = bookingController.cancelBooking("REF123", authentication);
        
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(booking, response.getBody());
        assertEquals("REF123", response.getBody().getBookingReference());
        verify(bookingService).cancelBooking("REF123", "user@example.com");
    }

    @Test
    void testCancelBookingDifferentReference() {
        Booking booking = new Booking();
        booking.setBookingReference("REF456");

        when(authentication.getName()).thenReturn("user@example.com");
        when(bookingService.cancelBooking("REF456", "user@example.com")).thenReturn(booking);

        ResponseEntity<Booking> response = bookingController.cancelBooking("REF456", authentication);
        
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(booking, response.getBody());
        verify(bookingService).cancelBooking("REF456", "user@example.com");
    }

    @Test
    void testDownloadTicketSuccess() {
        Booking booking = new Booking();
        booking.setBookingReference("REF123");
        booking.setId(1L);
        byte[] ticketPdf = "Detailed Ticket Content with Flight Info".getBytes();

        when(authentication.getName()).thenReturn("user@example.com");
        when(bookingService.getBookingByReference("REF123", "user@example.com")).thenReturn(booking);
        when(ticketService.generateTicket(booking)).thenReturn(ticketPdf);

        ResponseEntity<byte[]> response = bookingController.downloadTicket("REF123", authentication);
        
        assertEquals(200, response.getStatusCodeValue());
        assertArrayEquals(ticketPdf, response.getBody());
        assertEquals(MediaType.TEXT_PLAIN, response.getHeaders().getContentType());
        assertTrue(response.getHeaders().getContentDisposition().getFilename().contains("ticket-REF123.txt"));
        verify(bookingService).getBookingByReference("REF123", "user@example.com");
        verify(ticketService).generateTicket(booking);
    }

    @Test
    void testDownloadTicketBookingServiceException() {
        when(authentication.getName()).thenReturn("user@example.com");
        when(bookingService.getBookingByReference("REF123", "user@example.com"))
            .thenThrow(new RuntimeException("Booking not found"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            bookingController.downloadTicket("REF123", authentication);
        });
        
        assertTrue(exception.getMessage().contains("Failed to generate ticket: Booking not found"));
        verify(bookingService).getBookingByReference("REF123", "user@example.com");
        verify(ticketService, never()).generateTicket(any());
    }

    @Test
    void testDownloadTicketTicketServiceException() {
        Booking booking = new Booking();
        booking.setBookingReference("REF123");

        when(authentication.getName()).thenReturn("user@example.com");
        when(bookingService.getBookingByReference("REF123", "user@example.com")).thenReturn(booking);
        when(ticketService.generateTicket(booking)).thenThrow(new RuntimeException("Ticket generation failed"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            bookingController.downloadTicket("REF123", authentication);
        });
        
        assertTrue(exception.getMessage().contains("Failed to generate ticket: Ticket generation failed"));
        verify(bookingService).getBookingByReference("REF123", "user@example.com");
        verify(ticketService).generateTicket(booking);
    }

    @Test
    void testDownloadTicketDifferentReference() {
        Booking booking = new Booking();
        booking.setBookingReference("REF789");
        byte[] ticketPdf = "Another Ticket Content".getBytes();

        when(authentication.getName()).thenReturn("user@example.com");
        when(bookingService.getBookingByReference("REF789", "user@example.com")).thenReturn(booking);
        when(ticketService.generateTicket(booking)).thenReturn(ticketPdf);

        ResponseEntity<byte[]> response = bookingController.downloadTicket("REF789", authentication);
        
        assertEquals(200, response.getStatusCodeValue());
        assertArrayEquals(ticketPdf, response.getBody());
        assertTrue(response.getHeaders().getContentDisposition().getFilename().contains("ticket-REF789.txt"));
    }

    @Test
    void testAuthenticationNameExtraction() {
        BookingRequest request = new BookingRequest();
        Booking booking = new Booking();
        
        // Test with different username
        when(authentication.getName()).thenReturn("different.user@example.com");
        when(bookingService.bookFlight(request, "different.user@example.com")).thenReturn(booking);

        ResponseEntity<Booking> response = bookingController.bookFlight(request, authentication);
        
        assertEquals(200, response.getStatusCodeValue());
        verify(bookingService).bookFlight(request, "different.user@example.com");
    }

    @Test
    void testResponseEntityHeaders() {
        Booking booking = new Booking();
        booking.setBookingReference("REF123");
        byte[] ticketPdf = "Test Content".getBytes();

        when(authentication.getName()).thenReturn("user@example.com");
        when(bookingService.getBookingByReference("REF123", "user@example.com")).thenReturn(booking);
        when(ticketService.generateTicket(booking)).thenReturn(ticketPdf);

        ResponseEntity<byte[]> response = bookingController.downloadTicket("REF123", authentication);
        
        HttpHeaders headers = response.getHeaders();
        assertNotNull(headers);
        assertEquals(MediaType.TEXT_PLAIN, headers.getContentType());
        assertNotNull(headers.getContentDisposition());
        assertEquals("attachment", headers.getContentDisposition().getType());
        assertEquals("ticket-REF123.txt", headers.getContentDisposition().getFilename());
    }
}
