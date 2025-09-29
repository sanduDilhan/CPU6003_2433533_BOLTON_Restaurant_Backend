package com.tabletop.restaurant.dto;

import com.tabletop.restaurant.entity.User;

public class RegistrationResponseDto {
    
    private boolean success;
    private String message;
    private User user;
    private String error;
    
    // Constructors
    public RegistrationResponseDto() {}
    
    public RegistrationResponseDto(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
    
    public RegistrationResponseDto(boolean success, String message, User user) {
        this.success = success;
        this.message = message;
        this.user = user;
    }
    
    public RegistrationResponseDto(boolean success, String message, String error) {
        this.success = success;
        this.message = message;
        this.error = error;
    }
    
    // Static factory methods
    public static RegistrationResponseDto success(String message, User user) {
        return new RegistrationResponseDto(true, message, user);
    }
    
    public static RegistrationResponseDto error(String message, String error) {
        return new RegistrationResponseDto(false, message, error);
    }
    
    // Getters and Setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    
    public String getError() { return error; }
    public void setError(String error) { this.error = error; }
}

