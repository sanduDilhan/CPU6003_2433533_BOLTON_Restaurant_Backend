package com.tabletop.restaurant.controller;

import com.tabletop.restaurant.entity.Restaurant;
import com.tabletop.restaurant.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/restaurants")
@CrossOrigin(origins = "http://localhost:3000")
public class RestaurantController {
    
    @Autowired
    private RestaurantService restaurantService;
    
    @GetMapping
    public ResponseEntity<List<Restaurant>> getAllRestaurants(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String cuisine,
            @RequestParam(required = false) Double minRating,
            @RequestParam(required = false) String search) {
        
        List<Restaurant> restaurants;
        
        if (search != null && !search.trim().isEmpty()) {
            restaurants = restaurantService.searchRestaurants(search);
        } else if (city != null && cuisine != null) {
            restaurants = restaurantService.getRestaurantsByCityAndCuisine(city, cuisine);
        } else if (city != null) {
            restaurants = restaurantService.getRestaurantsByCity(city);
        } else if (cuisine != null) {
            restaurants = restaurantService.getRestaurantsByCuisine(cuisine);
        } else if (minRating != null) {
            restaurants = restaurantService.getRestaurantsByRating(minRating);
        } else {
            restaurants = restaurantService.getAllRestaurants();
        }
        
        return ResponseEntity.ok(restaurants);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Restaurant> getRestaurantById(@PathVariable Long id) {
        Optional<Restaurant> restaurant = restaurantService.getRestaurantById(id);
        return restaurant.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/top-rated")
    public ResponseEntity<List<Restaurant>> getTopRatedRestaurants(
            @RequestParam(required = false) String city) {
        List<Restaurant> restaurants;
        if (city != null) {
            restaurants = restaurantService.getTopRatedRestaurantsByCity(city);
        } else {
            restaurants = restaurantService.getTopRatedRestaurants();
        }
        return ResponseEntity.ok(restaurants);
    }
    
    @PostMapping
    public ResponseEntity<Restaurant> createRestaurant(@RequestBody Restaurant restaurant) {
        Restaurant savedRestaurant = restaurantService.saveRestaurant(restaurant);
        return ResponseEntity.ok(savedRestaurant);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Restaurant> updateRestaurant(@PathVariable Long id, @RequestBody Restaurant restaurant) {
        restaurant.setId(id);
        Restaurant updatedRestaurant = restaurantService.saveRestaurant(restaurant);
        return ResponseEntity.ok(updatedRestaurant);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRestaurant(@PathVariable Long id) {
        restaurantService.deleteRestaurant(id);
        return ResponseEntity.noContent().build();
    }
}

