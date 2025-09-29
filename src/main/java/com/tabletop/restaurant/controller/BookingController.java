package com.tabletop.restaurant.controller;

import com.tabletop.restaurant.dto.BookingRequestDto;
import com.tabletop.restaurant.dto.BookingResponseDto;
import com.tabletop.restaurant.entity.Booking;
import com.tabletop.restaurant.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "http://localhost:3000")
public class BookingController {
    
    @Autowired
    private BookingService bookingService;
    
    @GetMapping
    public ResponseEntity<List<Booking>> getAllBookings() {
        List<Booking> bookings = bookingService.getAllBookings();
        return ResponseEntity.ok(bookings);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Booking> getBookingById(@PathVariable Long id) {
        Optional<Booking> booking = bookingService.getBookingById(id);
        return booking.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Booking>> getBookingsByUserId(@PathVariable Long userId) {
        List<Booking> bookings = bookingService.getBookingsByUserId(userId);
        return ResponseEntity.ok(bookings);
    }
    
    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<Booking>> getBookingsByRestaurantId(@PathVariable Long restaurantId) {
        List<Booking> bookings = bookingService.getBookingsByRestaurantId(restaurantId);
        return ResponseEntity.ok(bookings);
    }
    
    @PostMapping
    public ResponseEntity<BookingResponseDto> createBooking(@Valid @RequestBody BookingRequestDto bookingRequest) {
        try {
            System.out.println("Booking request received: " + bookingRequest.getUserId() + 
                             " for restaurant: " + bookingRequest.getRestaurantId());
            
            BookingResponseDto response = bookingService.createBookingWithValidation(bookingRequest);
            
            if (response.isSuccess()) {
                System.out.println("Booking created successfully: " + response.getId());
                return ResponseEntity.ok(response);
            } else {
                System.out.println("Booking creation failed: " + response.getError());
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            System.err.println("Booking controller error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest()
                .body(BookingResponseDto.error("Booking failed", "An error occurred during booking: " + e.getMessage()));
        }
    }
    
    @PostMapping("/legacy")
    public ResponseEntity<Booking> createBookingLegacy(@RequestBody Booking booking) {
        Booking savedBooking = bookingService.createBooking(booking);
        return ResponseEntity.ok(savedBooking);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Booking> updateBooking(@PathVariable Long id, @RequestBody Booking booking) {
        booking.setId(id);
        Booking updatedBooking = bookingService.updateBooking(booking);
        return ResponseEntity.ok(updatedBooking);
    }
    
    @PutMapping("/{id}/confirm")
    public ResponseEntity<Booking> confirmBooking(@PathVariable Long id) {
        Booking booking = bookingService.confirmBooking(id);
        if (booking != null) {
            return ResponseEntity.ok(booking);
        }
        return ResponseEntity.notFound().build();
    }
    
    @PutMapping("/{id}/cancel")
    public ResponseEntity<Booking> cancelBooking(@PathVariable Long id) {
        Booking booking = bookingService.cancelBooking(id);
        if (booking != null) {
            return ResponseEntity.ok(booking);
        }
        return ResponseEntity.notFound().build();
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
        bookingService.deleteBooking(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/test-mysql")
    public ResponseEntity<String> testMySQLConnection() {
        try {
            List<Booking> allBookings = bookingService.getAllBookings();
            return ResponseEntity.ok("MySQL connection successful. Total bookings in database: " + allBookings.size());
        } catch (Exception e) {
            return ResponseEntity.ok("MySQL connection test failed: " + e.getMessage());
        }
    }
    
    @GetMapping("/test-booking/{userId}")
    public ResponseEntity<String> testUserBookings(@PathVariable Long userId) {
        try {
            List<Booking> userBookings = bookingService.getBookingsByUserId(userId);
            return ResponseEntity.ok("User " + userId + " has " + userBookings.size() + " bookings in database");
        } catch (Exception e) {
            return ResponseEntity.ok("Error retrieving user bookings: " + e.getMessage());
        }
    }
}


