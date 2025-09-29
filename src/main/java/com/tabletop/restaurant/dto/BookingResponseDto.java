package com.tabletop.restaurant.dto;

import com.tabletop.restaurant.entity.Booking;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class BookingResponseDto {
    
    private Long id;
    private Long userId;
    private Long restaurantId;
    private LocalDate date;
    private LocalTime time;
    private Integer partySize;
    private String status;
    private String specialRequests;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Response status
    private boolean success;
    private String message;
    private String error;
    
    // Constructors
    public BookingResponseDto() {}
    
    public BookingResponseDto(Booking booking) {
        this.id = booking.getId();
        this.userId = booking.getUserId();
        this.restaurantId = booking.getRestaurantId();
        this.date = booking.getDate();
        this.time = booking.getTime();
        this.partySize = booking.getPartySize();
        this.status = booking.getStatus().name();
        this.specialRequests = booking.getSpecialRequests();
        this.createdAt = booking.getCreatedAt();
        this.updatedAt = booking.getUpdatedAt();
    }
    
    // Static factory methods
    public static BookingResponseDto success(Booking booking, String message) {
        BookingResponseDto response = new BookingResponseDto(booking);
        response.success = true;
        response.message = message;
        return response;
    }
    
    public static BookingResponseDto success(String message) {
        BookingResponseDto response = new BookingResponseDto();
        response.success = true;
        response.message = message;
        return response;
    }
    
    public static BookingResponseDto error(String message, String error) {
        BookingResponseDto response = new BookingResponseDto();
        response.success = false;
        response.message = message;
        response.error = error;
        return response;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    
    public Long getRestaurantId() { return restaurantId; }
    public void setRestaurantId(Long restaurantId) { this.restaurantId = restaurantId; }
    
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    
    public LocalTime getTime() { return time; }
    public void setTime(LocalTime time) { this.time = time; }
    
    public Integer getPartySize() { return partySize; }
    public void setPartySize(Integer partySize) { this.partySize = partySize; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getSpecialRequests() { return specialRequests; }
    public void setSpecialRequests(String specialRequests) { this.specialRequests = specialRequests; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public String getError() { return error; }
    public void setError(String error) { this.error = error; }
}
