package com.tabletop.restaurant.repository;

import com.tabletop.restaurant.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    
    List<Booking> findByUserId(Long userId);
    
    List<Booking> findByRestaurantId(Long restaurantId);
    
    List<Booking> findByUserIdAndStatus(Long userId, Booking.BookingStatus status);
    
    @Query("SELECT b FROM Booking b WHERE b.restaurantId = :restaurantId AND b.date = :date")
    List<Booking> findByRestaurantIdAndDate(@Param("restaurantId") Long restaurantId, @Param("date") LocalDate date);
    
    @Query("SELECT COUNT(b) FROM Booking b WHERE b.restaurantId = :restaurantId AND b.status = 'CONFIRMED'")
    Long countConfirmedBookingsByRestaurantId(@Param("restaurantId") Long restaurantId);
}


