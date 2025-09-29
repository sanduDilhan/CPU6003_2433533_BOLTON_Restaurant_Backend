package com.tabletop.restaurant.service;

import com.tabletop.restaurant.dto.FavoritesResponseDto;
import com.tabletop.restaurant.dto.LoginRequestDto;
import com.tabletop.restaurant.dto.LoginResponseDto;
import com.tabletop.restaurant.dto.UserRegistrationDto;
import com.tabletop.restaurant.entity.Restaurant;
import com.tabletop.restaurant.entity.User;
import com.tabletop.restaurant.repository.RestaurantRepository;
import com.tabletop.restaurant.repository.UserRepository;
import com.tabletop.restaurant.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private RestaurantRepository restaurantRepository;
    
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }
    
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    public LoginResponseDto login(LoginRequestDto loginRequest) {
        try {
            System.out.println("Login attempt for username: " + loginRequest.getUsername());
            
            // Find user by username
            Optional<User> userOpt = userRepository.findByUsername(loginRequest.getUsername());
            
            if (userOpt.isEmpty()) {
                System.out.println("User not found: " + loginRequest.getUsername());
                return LoginResponseDto.error("Login failed", "Invalid username or password");
            }
            
            User user = userOpt.get();
            System.out.println("User found: " + user.getUsername() + ", Role: " + user.getRole());
            
            // Verify password
            boolean passwordMatch = passwordEncoder.matches(loginRequest.getPassword(), user.getPassword());
            System.out.println("Password match: " + passwordMatch);
            
            if (!passwordMatch) {
                return LoginResponseDto.error("Login failed", "Invalid username or password");
            }
            
            // Generate JWT token
            String token = jwtUtil.generateToken(user.getUsername(), user.getId(), user.getRole().name());
            System.out.println("Token generated successfully");
            
            // Return success response with token and user data
            return LoginResponseDto.success("Login successful", token, user);
            
        } catch (Exception e) {
            System.err.println("Login error: " + e.getMessage());
            e.printStackTrace();
            return LoginResponseDto.error("Login failed", "An error occurred during login: " + e.getMessage());
        }
    }
    
    public User registerUser(UserRegistrationDto registrationDto) {
        // Check if username already exists
        if (userRepository.existsByUsername(registrationDto.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        
        // Check if email already exists
        if (userRepository.existsByEmail(registrationDto.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        
        // Create new user from registration data
        User user = new User();
        user.setUsername(registrationDto.getUsername());
        user.setEmail(registrationDto.getEmail());
        user.setFirstName(registrationDto.getFirstName());
        user.setLastName(registrationDto.getLastName());
        user.setPhone(registrationDto.getPhone());
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        user.setRole(User.Role.USER); // Default role
        user.setCreatedAt(LocalDateTime.now());
        
        return userRepository.save(user);
    }
    
    public User createUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }
    
    public User updateUser(User user) {
        return userRepository.save(user);
    }
    
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
    
    public FavoritesResponseDto getUserFavorites(Long userId) {
        try {
            Optional<User> userOpt = userRepository.findById(userId);
            if (userOpt.isEmpty()) {
                return FavoritesResponseDto.error("User not found", "User with ID " + userId + " not found");
            }
            
            User user = userOpt.get();
            List<Long> favoriteIds = user.getFavorites();
            
            if (favoriteIds == null || favoriteIds.isEmpty()) {
                return FavoritesResponseDto.success("No favorites found", new ArrayList<>());
            }
            
            List<Restaurant> favoriteRestaurants = new ArrayList<>();
            for (Long restaurantId : favoriteIds) {
                Optional<Restaurant> restaurantOpt = restaurantRepository.findById(restaurantId);
                if (restaurantOpt.isPresent()) {
                    favoriteRestaurants.add(restaurantOpt.get());
                }
            }
            
            return FavoritesResponseDto.success("Favorites retrieved successfully", favoriteRestaurants);
            
        } catch (Exception e) {
            System.err.println("Error getting user favorites: " + e.getMessage());
            return FavoritesResponseDto.error("Failed to get favorites", e.getMessage());
        }
    }
    
    public FavoritesResponseDto toggleFavorite(Long userId, Long restaurantId) {
        try {
            System.out.println("Toggle favorite request - User ID: " + userId + ", Restaurant ID: " + restaurantId);
            
            Optional<User> userOpt = userRepository.findById(userId);
            if (userOpt.isEmpty()) {
                System.out.println("User not found with ID: " + userId);
                return FavoritesResponseDto.error("User not found", "User with ID " + userId + " not found");
            }
            
            Optional<Restaurant> restaurantOpt = restaurantRepository.findById(restaurantId);
            if (restaurantOpt.isEmpty()) {
                System.out.println("Restaurant not found with ID: " + restaurantId);
                return FavoritesResponseDto.error("Restaurant not found", "Restaurant with ID " + restaurantId + " not found");
            }
            
            User user = userOpt.get();
            List<Long> favorites = user.getFavorites();
            
            System.out.println("Current favorites: " + favorites);
            
            if (favorites == null) {
                favorites = new ArrayList<>();
                System.out.println("Initialized empty favorites list");
            }
            
            boolean isFavorite = favorites.contains(restaurantId);
            String message;
            
            System.out.println("Is favorite: " + isFavorite);
            
            if (isFavorite) {
                favorites.remove(restaurantId);
                message = "Restaurant removed from favorites";
                System.out.println("Removed from favorites");
            } else {
                favorites.add(restaurantId);
                message = "Restaurant added to favorites";
                System.out.println("Added to favorites");
            }
            
            System.out.println("Updated favorites: " + favorites);
            
            user.setFavorites(favorites);
            userRepository.save(user);
            System.out.println("User saved to database");
            
            // Return updated favorites list
            List<Restaurant> favoriteRestaurants = new ArrayList<>();
            for (Long favId : favorites) {
                Optional<Restaurant> favRestaurantOpt = restaurantRepository.findById(favId);
                if (favRestaurantOpt.isPresent()) {
                    favoriteRestaurants.add(favRestaurantOpt.get());
                }
            }
            
            return FavoritesResponseDto.success(message, favoriteRestaurants);
            
        } catch (Exception e) {
            System.err.println("Error toggling favorite: " + e.getMessage());
            return FavoritesResponseDto.error("Failed to toggle favorite", e.getMessage());
        }
    }
    
    public boolean addToFavorites(Long userId, Long restaurantId) {
        FavoritesResponseDto response = toggleFavorite(userId, restaurantId);
        return response.isSuccess() && !response.getFavorites().isEmpty();
    }
    
    public boolean removeFromFavorites(Long userId, Long restaurantId) {
        FavoritesResponseDto response = toggleFavorite(userId, restaurantId);
        return response.isSuccess();
    }
}


