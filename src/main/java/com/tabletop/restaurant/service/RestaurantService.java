package com.tabletop.restaurant.service;

import com.tabletop.restaurant.entity.Restaurant;
import com.tabletop.restaurant.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RestaurantService {
    
    @Autowired
    private RestaurantRepository restaurantRepository;
    
    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }
    
    public Optional<Restaurant> getRestaurantById(Long id) {
        return restaurantRepository.findById(id);
    }
    
    public List<Restaurant> getRestaurantsByCity(String city) {
        return restaurantRepository.findByCityIgnoreCase(city);
    }
    
    public List<Restaurant> getRestaurantsByCuisine(String cuisine) {
        return restaurantRepository.findByCuisineIgnoreCase(cuisine);
    }
    
    public List<Restaurant> getRestaurantsByCityAndCuisine(String city, String cuisine) {
        return restaurantRepository.findByCityIgnoreCaseAndCuisineIgnoreCase(city, cuisine);
    }
    
    public List<Restaurant> getRestaurantsByRating(Double minRating) {
        return restaurantRepository.findByRatingGreaterThanEqual(minRating);
    }
    
    public List<Restaurant> searchRestaurants(String searchTerm) {
        return restaurantRepository.searchRestaurants(searchTerm);
    }
    
    public List<Restaurant> getTopRatedRestaurants() {
        return restaurantRepository.findAllOrderByRatingDesc();
    }
    
    public List<Restaurant> getTopRatedRestaurantsByCity(String city) {
        return restaurantRepository.findByCityOrderByRatingDesc(city);
    }
    
    public Restaurant saveRestaurant(Restaurant restaurant) {
        return restaurantRepository.save(restaurant);
    }
    
    public void deleteRestaurant(Long id) {
        restaurantRepository.deleteById(id);
    }
}

