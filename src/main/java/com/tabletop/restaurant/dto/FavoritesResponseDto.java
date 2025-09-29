package com.tabletop.restaurant.dto;

import com.tabletop.restaurant.entity.Restaurant;

import java.util.List;

public class FavoritesResponseDto {
    
    private boolean success;
    private String message;
    private List<Restaurant> favorites;
    private String error;
    
    // Constructors
    public FavoritesResponseDto() {}
    
    public FavoritesResponseDto(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
    
    public FavoritesResponseDto(boolean success, String message, List<Restaurant> favorites) {
        this.success = success;
        this.message = message;
        this.favorites = favorites;
    }
    
    public FavoritesResponseDto(boolean success, String message, String error) {
        this.success = success;
        this.message = message;
        this.error = error;
    }
    
    // Static factory methods
    public static FavoritesResponseDto success(String message, List<Restaurant> favorites) {
        return new FavoritesResponseDto(true, message, favorites);
    }
    
    public static FavoritesResponseDto error(String message, String error) {
        return new FavoritesResponseDto(false, message, error);
    }
    
    // Getters and Setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public List<Restaurant> getFavorites() { return favorites; }
    public void setFavorites(List<Restaurant> favorites) { this.favorites = favorites; }
    
    public String getError() { return error; }
    public void setError(String error) { this.error = error; }
}

