package com.tabletop.restaurant.controller;

import com.tabletop.restaurant.dto.FavoritesResponseDto;
import com.tabletop.restaurant.dto.LoginRequestDto;
import com.tabletop.restaurant.dto.LoginResponseDto;
import com.tabletop.restaurant.dto.RegistrationResponseDto;
import com.tabletop.restaurant.dto.ToggleFavoriteDto;
import com.tabletop.restaurant.dto.UserRegistrationDto;
import com.tabletop.restaurant.entity.User;
import com.tabletop.restaurant.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);
        return user.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/username/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        Optional<User> user = userService.getUserByUsername(username);
        return user.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequest) {
        try {
            System.out.println("Login request received: " + loginRequest.getUsername());
            LoginResponseDto response = userService.login(loginRequest);
            
            if (response.isSuccess()) {
                System.out.println("Login successful for: " + loginRequest.getUsername());
                return ResponseEntity.ok(response);
            } else {
                System.out.println("Login failed for: " + loginRequest.getUsername() + ", Error: " + response.getError());
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            System.err.println("Login controller error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest()
                .body(LoginResponseDto.error("Login failed", "An error occurred during login: " + e.getMessage()));
        }
    }
    
    @GetMapping("/test-login")
    public ResponseEntity<String> testLogin() {
        return ResponseEntity.ok("Login endpoint is accessible");
    }
    
    @GetMapping("/test-favorites/{userId}")
    public ResponseEntity<String> testFavorites(@PathVariable Long userId) {
        try {
            Optional<User> user = userService.getUserById(userId);
            if (user.isPresent()) {
                return ResponseEntity.ok("User found: " + user.get().getUsername() + ", Favorites: " + user.get().getFavorites());
            } else {
                return ResponseEntity.ok("User not found with ID: " + userId);
            }
        } catch (Exception e) {
            return ResponseEntity.ok("Error: " + e.getMessage());
        }
    }
    
    @PostMapping("/register")
    public ResponseEntity<RegistrationResponseDto> registerUser(@Valid @RequestBody UserRegistrationDto registrationDto) {
        try {
            // Validate password confirmation
            if (!registrationDto.isPasswordMatch()) {
                return ResponseEntity.badRequest()
                    .body(RegistrationResponseDto.error("Registration failed", "Passwords do not match"));
            }
            
            User user = userService.registerUser(registrationDto);
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(RegistrationResponseDto.success("User registered successfully", user));
                
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                .body(RegistrationResponseDto.error("Registration failed", e.getMessage()));
        }
    }
    
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        try {
            User savedUser = userService.createUser(user);
            return ResponseEntity.ok(savedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        user.setId(id);
        User updatedUser = userService.updateUser(user);
        return ResponseEntity.ok(updatedUser);
    }
    
    @GetMapping("/{userId}/favorites")
    public ResponseEntity<FavoritesResponseDto> getUserFavorites(@PathVariable Long userId) {
        try {
            FavoritesResponseDto response = userService.getUserFavorites(userId);
            if (response.isSuccess()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(FavoritesResponseDto.error("Failed to get favorites", e.getMessage()));
        }
    }
    
    @PostMapping("/{userId}/favorites/toggle")
    public ResponseEntity<FavoritesResponseDto> toggleFavorite(@PathVariable Long userId, @Valid @RequestBody ToggleFavoriteDto toggleDto) {
        try {
            FavoritesResponseDto response = userService.toggleFavorite(userId, toggleDto.getRestaurantId());
            if (response.isSuccess()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(FavoritesResponseDto.error("Failed to toggle favorite", e.getMessage()));
        }
    }
    
    @PostMapping("/{userId}/favorites/{restaurantId}")
    public ResponseEntity<FavoritesResponseDto> addToFavorites(@PathVariable Long userId, @PathVariable Long restaurantId) {
        try {
            System.out.println("Add to favorites request - User ID: " + userId + ", Restaurant ID: " + restaurantId);
            
            // Check if already favorite
            Optional<User> userOpt = userService.getUserById(userId);
            if (userOpt.isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(FavoritesResponseDto.error("User not found", "User with ID " + userId + " not found"));
            }
            
            User user = userOpt.get();
            List<Long> favorites = user.getFavorites();
            if (favorites != null && favorites.contains(restaurantId)) {
                return ResponseEntity.badRequest()
                    .body(FavoritesResponseDto.error("Already favorite", "Restaurant is already in favorites"));
            }
            
            FavoritesResponseDto response = userService.toggleFavorite(userId, restaurantId);
            if (response.isSuccess()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            System.err.println("Add to favorites error: " + e.getMessage());
            return ResponseEntity.badRequest()
                .body(FavoritesResponseDto.error("Failed to add to favorites", e.getMessage()));
        }
    }
    
    @DeleteMapping("/{userId}/favorites/{restaurantId}")
    public ResponseEntity<FavoritesResponseDto> removeFromFavorites(@PathVariable Long userId, @PathVariable Long restaurantId) {
        try {
            System.out.println("Remove from favorites request - User ID: " + userId + ", Restaurant ID: " + restaurantId);
            
            // Check if not favorite
            Optional<User> userOpt = userService.getUserById(userId);
            if (userOpt.isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(FavoritesResponseDto.error("User not found", "User with ID " + userId + " not found"));
            }
            
            User user = userOpt.get();
            List<Long> favorites = user.getFavorites();
            if (favorites == null || !favorites.contains(restaurantId)) {
                return ResponseEntity.badRequest()
                    .body(FavoritesResponseDto.error("Not favorite", "Restaurant is not in favorites"));
            }
            
            FavoritesResponseDto response = userService.toggleFavorite(userId, restaurantId);
            if (response.isSuccess()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            System.err.println("Remove from favorites error: " + e.getMessage());
            return ResponseEntity.badRequest()
                .body(FavoritesResponseDto.error("Failed to remove from favorites", e.getMessage()));
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}


