package com.tabletop.restaurant.service;

import com.tabletop.restaurant.entity.Booking;
import com.tabletop.restaurant.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {
    
    @Autowired
    private BookingRepository bookingRepository;
    
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
}
