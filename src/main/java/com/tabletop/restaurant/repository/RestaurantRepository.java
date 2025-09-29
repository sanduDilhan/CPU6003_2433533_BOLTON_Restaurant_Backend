package com.tabletop.restaurant.repository;

import com.tabletop.restaurant.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    
    List<Restaurant> findByCityIgnoreCase(String city);
    
    List<Restaurant> findByCuisineIgnoreCase(String cuisine);
    
    List<Restaurant> findByCityIgnoreCaseAndCuisineIgnoreCase(String city, String cuisine);
    
    List<Restaurant> findByRatingGreaterThanEqual(Double rating);
    
    @Query("SELECT r FROM Restaurant r WHERE " +
           "LOWER(r.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(r.cuisine) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(r.city) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Restaurant> searchRestaurants(@Param("searchTerm") String searchTerm);
    
    @Query("SELECT r FROM Restaurant r ORDER BY r.rating DESC")
    List<Restaurant> findAllOrderByRatingDesc();
    
    @Query("SELECT r FROM Restaurant r WHERE r.city = :city ORDER BY r.rating DESC")
    List<Restaurant> findByCityOrderByRatingDesc(@Param("city") String city);
}



