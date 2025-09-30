package com.tabletop.restaurant.dto;

import java.time.LocalDateTime;

public class ReviewResponseDto {
    
    private Long id;
    private Long userId;
    private Long restaurantId;
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;
    private String userName; // User's name for display
    private String userEmail; // User's email for display
    
    // Constructors
    public ReviewResponseDto() {}
    
    public ReviewResponseDto(Long id, Long userId, Long restaurantId, Integer rating, 
                           String comment, LocalDateTime createdAt, String userName, String userEmail) {
        this.id = id;
        this.userId = userId;
        this.restaurantId = restaurantId;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = createdAt;
        this.userName = userName;
        this.userEmail = userEmail;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    
    public Long getRestaurantId() { return restaurantId; }
    public void setRestaurantId(Long restaurantId) { this.restaurantId = restaurantId; }
    
    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }
    
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    
    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }
}
