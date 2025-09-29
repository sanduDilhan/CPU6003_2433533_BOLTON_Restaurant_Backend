package com.tabletop.restaurant.service;

import com.tabletop.restaurant.dto.LoginRequestDto;
import com.tabletop.restaurant.dto.LoginResponseDto;
import com.tabletop.restaurant.dto.UserRegistrationDto;
import com.tabletop.restaurant.entity.User;
import com.tabletop.restaurant.repository.UserRepository;
import com.tabletop.restaurant.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    
    public boolean addToFavorites(Long userId, Long restaurantId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.getFavorites() == null) {
                user.setFavorites(List.of(restaurantId));
            } else if (!user.getFavorites().contains(restaurantId)) {
                user.getFavorites().add(restaurantId);
            }
            userRepository.save(user);
            return true;
        }
        return false;
    }
    
    public boolean removeFromFavorites(Long userId, Long restaurantId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.getFavorites() != null) {
                user.getFavorites().remove(restaurantId);
                userRepository.save(user);
                return true;
            }
        }
        return false;
    }
}


