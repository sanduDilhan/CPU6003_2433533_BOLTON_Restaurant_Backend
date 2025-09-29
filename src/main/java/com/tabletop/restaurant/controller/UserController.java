package com.tabletop.restaurant.controller;

import com.tabletop.restaurant.dto.RegistrationResponseDto;
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
    
    @PostMapping("/{userId}/favorites/{restaurantId}")
    public ResponseEntity<Boolean> addToFavorites(@PathVariable Long userId, @PathVariable Long restaurantId) {
        boolean success = userService.addToFavorites(userId, restaurantId);
        return ResponseEntity.ok(success);
    }
    
    @DeleteMapping("/{userId}/favorites/{restaurantId}")
    public ResponseEntity<Boolean> removeFromFavorites(@PathVariable Long userId, @PathVariable Long restaurantId) {
        boolean success = userService.removeFromFavorites(userId, restaurantId);
        return ResponseEntity.ok(success);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}


