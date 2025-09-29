package com.tabletop.restaurant.dto;

import com.tabletop.restaurant.entity.User;

public class LoginResponseDto {
    
    private boolean success;
    private String message;
    private String token;
    private User user;
    private String error;
    
    // Constructors
    public LoginResponseDto() {}
    
    public LoginResponseDto(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
    
    public LoginResponseDto(boolean success, String message, String token, User user) {
        this.success = success;
        this.message = message;
        this.token = token;
        this.user = user;
    }
    
    public LoginResponseDto(boolean success, String message, String error) {
        this.success = success;
        this.message = message;
        this.error = error;
    }
    
    // Static factory methods
    public static LoginResponseDto success(String message, String token, User user) {
        return new LoginResponseDto(true, message, token, user);
    }
    
    public static LoginResponseDto error(String message, String error) {
        return new LoginResponseDto(false, message, error);
    }
    
    // Getters and Setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    
    public String getError() { return error; }
    public void setError(String error) { this.error = error; }
}
