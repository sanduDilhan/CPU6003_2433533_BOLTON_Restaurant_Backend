package com.tabletop.restaurant.config;

import com.tabletop.restaurant.entity.*;
import com.tabletop.restaurant.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
public class SimpleDataInitializer implements CommandLineRunner {
    
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
            // Don't fail the application startup
        }
    }
    
    private void initializeData() {
        // Create sample restaurants
        Restaurant restaurant1 = new Restaurant("The Spice Garden", "Sri Lankan", "Colombo", "123 Galle Road, Colombo 03");
        restaurant1.setPhone("+94 11 234 5678");
        restaurant1.setRating(4.5);
        restaurant1.setPriceRange("$$");
        restaurant1.setDescription("Authentic Sri Lankan cuisine with a modern twist");
        restaurant1.setImageUrl("https://images.unsplash.com/photo-1555396273-367ea4eb4db5?w=500");
        restaurant1.setAmenities(Arrays.asList("WiFi", "Parking", "Outdoor Seating", "Takeaway"));
        restaurant1.setCoordinates(new Coordinates(6.9271, 79.8612));
        restaurantRepository.save(restaurant1);
        
        Restaurant restaurant2 = new Restaurant("Bamboo Garden", "Chinese", "Kandy", "45 Peradeniya Road, Kandy");
        restaurant2.setPhone("+94 81 234 5678");
        restaurant2.setRating(4.2);
        restaurant2.setPriceRange("$$$");
        restaurant2.setDescription("Traditional Chinese dishes in a serene garden setting");
        restaurant2.setImageUrl("https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?w=500");
        restaurant2.setAmenities(Arrays.asList("WiFi", "Parking", "Garden Seating", "Private Dining"));
        restaurant2.setCoordinates(new Coordinates(7.2906, 80.6337));
        restaurantRepository.save(restaurant2);
        
        // Create sample users
        User user1 = new User("john_doe", "john.doe@email.com", "John", "Doe", passwordEncoder.encode("password123"));
        user1.setPhone("+94 77 123 4567");
        user1.setFavorites(Arrays.asList(1L, 2L));
        userRepository.save(user1);
        
        User admin = new User("admin", "admin@tabletop.lk", "Admin", "User", passwordEncoder.encode("admin123"));
        admin.setPhone("+94 77 999 9999");
        admin.setRole(User.Role.ADMIN);
        userRepository.save(admin);
        
        // Create sample bookings
        Booking booking1 = new Booking(1L, 1L, java.time.LocalDate.now().plusDays(7), java.time.LocalTime.of(19, 0), 4);
        booking1.setStatus(Booking.BookingStatus.CONFIRMED);
        booking1.setSpecialRequests("Window table preferred");
        booking1.setCreatedAt(LocalDateTime.now());
        booking1.setUpdatedAt(LocalDateTime.now());
        bookingRepository.save(booking1);
        
        // Create sample reviews
        Review review1 = new Review(1L, 1L, 5, "Excellent food and service! The curry was amazing.");
        review1.setCreatedAt(LocalDateTime.now());
        reviewRepository.save(review1);
        
        System.out.println("Sample data initialized successfully!");
    }
}


