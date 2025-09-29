package com.tabletop.restaurant.service;

import com.tabletop.restaurant.dto.BookingRequestDto;
import com.tabletop.restaurant.dto.BookingResponseDto;
import com.tabletop.restaurant.entity.Booking;
import com.tabletop.restaurant.entity.Restaurant;
import com.tabletop.restaurant.entity.User;
import com.tabletop.restaurant.repository.BookingRepository;
import com.tabletop.restaurant.repository.RestaurantRepository;
import com.tabletop.restaurant.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {
    
    @Autowired
    private BookingRepository bookingRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RestaurantRepository restaurantRepository;
    
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }
    
    public Optional<Booking> getBookingById(Long id) {
        return bookingRepository.findById(id);
    }
    
    public List<Booking> getBookingsByUserId(Long userId) {
        return bookingRepository.findByUserId(userId);
    }
    
    public List<Booking> getBookingsByRestaurantId(Long restaurantId) {
        return bookingRepository.findByRestaurantId(restaurantId);
    }
    
    public List<Booking> getBookingsByUserIdAndStatus(Long userId, Booking.BookingStatus status) {
        return bookingRepository.findByUserIdAndStatus(userId, status);
    }
    
    public Booking createBooking(Booking booking) {
        booking.setCreatedAt(LocalDateTime.now());
        booking.setUpdatedAt(LocalDateTime.now());
        return bookingRepository.save(booking);
    }
    
    public BookingResponseDto createBookingWithValidation(BookingRequestDto bookingRequest) {
        try {
            System.out.println("Creating booking for user: " + bookingRequest.getUserId() + 
                             ", restaurant: " + bookingRequest.getRestaurantId());
            
            // Validate user exists
            Optional<User> userOpt = userRepository.findById(bookingRequest.getUserId());
            if (userOpt.isEmpty()) {
                System.out.println("User not found: " + bookingRequest.getUserId());
                return BookingResponseDto.error("User not found", "User with ID " + bookingRequest.getUserId() + " not found");
            }
            
            // Validate restaurant exists
            Optional<Restaurant> restaurantOpt = restaurantRepository.findById(bookingRequest.getRestaurantId());
            if (restaurantOpt.isEmpty()) {
                System.out.println("Restaurant not found: " + bookingRequest.getRestaurantId());
                return BookingResponseDto.error("Restaurant not found", "Restaurant with ID " + bookingRequest.getRestaurantId() + " not found");
            }
            
            // Validate booking date is not in the past
            LocalDate today = LocalDate.now();
            if (bookingRequest.getDate().isBefore(today)) {
                System.out.println("Booking date is in the past: " + bookingRequest.getDate());
                return BookingResponseDto.error("Invalid date", "Booking date cannot be in the past");
            }
            
            // Validate booking time for today (if booking is for today, time should be in the future)
            if (bookingRequest.getDate().equals(today)) {
                LocalTime now = LocalTime.now();
                if (bookingRequest.getTime().isBefore(now)) {
                    System.out.println("Booking time is in the past: " + bookingRequest.getTime());
                    return BookingResponseDto.error("Invalid time", "Booking time cannot be in the past");
                }
            }
            
            // Check for existing booking conflicts
            List<Booking> existingBookings = bookingRepository.findByRestaurantIdAndDate(
                bookingRequest.getRestaurantId(), bookingRequest.getDate());
            
            // Simple conflict check - in a real system, you'd check table availability
            if (!existingBookings.isEmpty()) {
                System.out.println("Found existing bookings for restaurant and date");
                // For now, we'll allow multiple bookings - in production, check table availability
            }
            
            // Create booking
            Booking booking = new Booking();
            booking.setUserId(bookingRequest.getUserId());
            booking.setRestaurantId(bookingRequest.getRestaurantId());
            booking.setDate(bookingRequest.getDate());
            booking.setTime(bookingRequest.getTime());
            booking.setPartySize(bookingRequest.getPartySize());
            booking.setSpecialRequests(bookingRequest.getSpecialRequests());
            booking.setStatus(Booking.BookingStatus.PENDING);
            booking.setCreatedAt(LocalDateTime.now());
            booking.setUpdatedAt(LocalDateTime.now());
            
            Booking savedBooking = bookingRepository.save(booking);
            System.out.println("Booking created successfully with ID: " + savedBooking.getId());
            
            return BookingResponseDto.success(savedBooking, "Booking created successfully");
            
        } catch (Exception e) {
            System.err.println("Error creating booking: " + e.getMessage());
            e.printStackTrace();
            return BookingResponseDto.error("Failed to create booking", "An error occurred: " + e.getMessage());
        }
    }
    
    public Booking updateBooking(Booking booking) {
        booking.setUpdatedAt(LocalDateTime.now());
        return bookingRepository.save(booking);
    }
    
    public void deleteBooking(Long id) {
        bookingRepository.deleteById(id);
    }
    
    public Booking confirmBooking(Long id) {
        Optional<Booking> bookingOpt = bookingRepository.findById(id);
        if (bookingOpt.isPresent()) {
            Booking booking = bookingOpt.get();
            booking.setStatus(Booking.BookingStatus.CONFIRMED);
            booking.setUpdatedAt(LocalDateTime.now());
            return bookingRepository.save(booking);
        }
        return null;
    }
    
    public Booking cancelBooking(Long id) {
        Optional<Booking> bookingOpt = bookingRepository.findById(id);
        if (bookingOpt.isPresent()) {
            Booking booking = bookingOpt.get();
            booking.setStatus(Booking.BookingStatus.CANCELLED);
            booking.setUpdatedAt(LocalDateTime.now());
            return bookingRepository.save(booking);
        }
        return null;
    }
    
    public BookingResponseDto cancelBookingWithValidation(Long bookingId, Long userId) {
        try {
            System.out.println("Cancelling booking: " + bookingId + " for user: " + userId);
            
            // Find the booking
            Optional<Booking> bookingOpt = bookingRepository.findById(bookingId);
            if (bookingOpt.isEmpty()) {
                System.out.println("Booking not found: " + bookingId);
                return BookingResponseDto.error("Booking not found", "Booking with ID " + bookingId + " not found");
            }
            
            Booking booking = bookingOpt.get();
            
            // Verify user owns the booking
            if (!booking.getUserId().equals(userId)) {
                System.out.println("User " + userId + " does not own booking " + bookingId);
                return BookingResponseDto.error("Unauthorized", "You can only cancel your own bookings");
            }
            
            // Check if booking is already cancelled
            if (booking.getStatus() == Booking.BookingStatus.CANCELLED) {
                System.out.println("Booking " + bookingId + " is already cancelled");
                return BookingResponseDto.error("Already cancelled", "This booking is already cancelled");
            }
            
            // Check if booking is completed
            if (booking.getStatus() == Booking.BookingStatus.COMPLETED) {
                System.out.println("Booking " + bookingId + " is completed and cannot be cancelled");
                return BookingResponseDto.error("Cannot cancel", "Completed bookings cannot be cancelled");
            }
            
            // Check if booking date is in the past (optional business rule)
            LocalDate today = LocalDate.now();
            if (booking.getDate().isBefore(today)) {
                System.out.println("Booking " + bookingId + " is for a past date");
                return BookingResponseDto.error("Cannot cancel", "Past bookings cannot be cancelled");
            }
            
            // Cancel the booking
            booking.setStatus(Booking.BookingStatus.CANCELLED);
            booking.setUpdatedAt(LocalDateTime.now());
            
            Booking savedBooking = bookingRepository.save(booking);
            System.out.println("Booking cancelled successfully: " + savedBooking.getId());
            
            return BookingResponseDto.success(savedBooking, "Booking cancelled successfully");
            
        } catch (Exception e) {
            System.err.println("Error cancelling booking: " + e.getMessage());
            e.printStackTrace();
            return BookingResponseDto.error("Failed to cancel booking", "An error occurred: " + e.getMessage());
        }
    }
}


