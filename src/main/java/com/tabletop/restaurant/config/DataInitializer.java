package com.tabletop.restaurant.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tabletop.restaurant.entity.*;
import com.tabletop.restaurant.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;

// @Component  // Temporarily disabled to avoid startup issues
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private RestaurantRepository restaurantRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private BookingRepository bookingRepository;
    
    @Autowired
    private ReviewRepository reviewRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public void run(String... args) throws Exception {
        try {
            if (restaurantRepository.count() == 0) {
                initializeData();
            }
        } catch (Exception e) {
            System.err.println("Error initializing data: " + e.getMessage());
            e.printStackTrace();
            // Don't fail the application startup if data initialization fails
        }
    }
    
    private void initializeData() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        
        // Load restaurants
        try (InputStream inputStream = new ClassPathResource("data/restaurants.json").getInputStream()) {
            List<Restaurant> restaurants = mapper.readValue(inputStream, new TypeReference<List<Restaurant>>() {});
            restaurantRepository.saveAll(restaurants);
            System.out.println("Loaded " + restaurants.size() + " restaurants");
        } catch (Exception e) {
            System.err.println("Error loading restaurants: " + e.getMessage());
            throw e;
        }
        
        // Load users
        try (InputStream inputStream = new ClassPathResource("data/users.json").getInputStream()) {
            List<User> users = mapper.readValue(inputStream, new TypeReference<List<User>>() {});
            for (User user : users) {
                user.setPassword(passwordEncoder.encode("password123")); // Default password
            }
            userRepository.saveAll(users);
            System.out.println("Loaded " + users.size() + " users");
        } catch (Exception e) {
            System.err.println("Error loading users: " + e.getMessage());
            throw e;
        }
        
        // Load bookings
        try (InputStream inputStream = new ClassPathResource("data/bookings.json").getInputStream()) {
            List<Booking> bookings = mapper.readValue(inputStream, new TypeReference<List<Booking>>() {});
            for (Booking booking : bookings) {
                // Set current time for demo purposes
                booking.setCreatedAt(LocalDateTime.now());
                booking.setUpdatedAt(LocalDateTime.now());
            }
            bookingRepository.saveAll(bookings);
            System.out.println("Loaded " + bookings.size() + " bookings");
        } catch (Exception e) {
            System.err.println("Error loading bookings: " + e.getMessage());
            throw e;
        }
        
        // Load reviews
        try (InputStream inputStream = new ClassPathResource("data/reviews.json").getInputStream()) {
            List<Review> reviews = mapper.readValue(inputStream, new TypeReference<List<Review>>() {});
            for (Review review : reviews) {
                // Set current time for demo purposes
                review.setCreatedAt(LocalDateTime.now());
            }
            reviewRepository.saveAll(reviews);
            System.out.println("Loaded " + reviews.size() + " reviews");
        } catch (Exception e) {
            System.err.println("Error loading reviews: " + e.getMessage());
            throw e;
        }
    }
}
