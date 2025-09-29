package com.tabletop.restaurant.dto;

import jakarta.validation.constraints.NotNull;

public class ToggleFavoriteDto {
    
    @NotNull(message = "Restaurant ID is required")
    private Long restaurantId;
    
    // Constructors
    public ToggleFavoriteDto() {}
    
    public ToggleFavoriteDto(Long restaurantId) {
        this.restaurantId = restaurantId;
    }
    
    // Getters and Setters
    public Long getRestaurantId() { return restaurantId; }
    public void setRestaurantId(Long restaurantId) { this.restaurantId = restaurantId; }
}
