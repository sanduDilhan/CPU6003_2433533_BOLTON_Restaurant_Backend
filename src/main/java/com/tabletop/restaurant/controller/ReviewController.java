package com.tabletop.restaurant.controller;

import com.tabletop.restaurant.dto.ReviewRequestDto;
import com.tabletop.restaurant.dto.ReviewResponseDto;
import com.tabletop.restaurant.entity.Review;
import com.tabletop.restaurant.entity.User;
import com.tabletop.restaurant.service.ReviewService;
import com.tabletop.restaurant.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reviews")
@CrossOrigin(origins = "http://localhost:3000")
public class ReviewController {
    
    @Autowired
    private ReviewService reviewService;
    
    @Autowired
    private UserService userService;
    
    @GetMapping
    public ResponseEntity<List<ReviewResponseDto>> getAllReviews() {
        List<Review> reviews = reviewService.getAllReviews();
        List<ReviewResponseDto> responseDtos = reviews.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDtos);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ReviewResponseDto> getReviewById(@PathVariable Long id) {
        Optional<Review> review = reviewService.getReviewById(id);
        return review.map(r -> ResponseEntity.ok(convertToResponseDto(r)))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<ReviewResponseDto>> getReviewsByRestaurantId(@PathVariable Long restaurantId) {
        List<Review> reviews = reviewService.getReviewsByRestaurantId(restaurantId);
        List<ReviewResponseDto> responseDtos = reviews.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDtos);
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReviewResponseDto>> getReviewsByUserId(@PathVariable Long userId) {
        List<Review> reviews = reviewService.getReviewsByUserId(userId);
        List<ReviewResponseDto> responseDtos = reviews.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDtos);
    }
    
    @GetMapping("/restaurant/{restaurantId}/stats")
    public ResponseEntity<Object> getRestaurantReviewStats(@PathVariable Long restaurantId) {
        Double averageRating = reviewService.getAverageRatingByRestaurantId(restaurantId);
        Long reviewCount = reviewService.getReviewCountByRestaurantId(restaurantId);

        return ResponseEntity.ok(java.util.Map.of(
            "averageRating", averageRating,
            "reviewCount", reviewCount
        ));
    }
    
    @PostMapping
    public ResponseEntity<ReviewResponseDto> createReview(@RequestBody ReviewRequestDto reviewRequest) {
        Review review = new Review();
        review.setUserId(reviewRequest.getUserId());
        review.setRestaurantId(reviewRequest.getRestaurantId());
        review.setRating(reviewRequest.getRating());
        review.setComment(reviewRequest.getComment());
        review.setCreatedAt(LocalDateTime.now());
        
        Review savedReview = reviewService.createReview(review);
        ReviewResponseDto responseDto = convertToResponseDto(savedReview);
        return ResponseEntity.ok(responseDto);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ReviewResponseDto> updateReview(@PathVariable Long id, @RequestBody ReviewRequestDto reviewRequest) {
        Optional<Review> existingReview = reviewService.getReviewById(id);
        if (existingReview.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        Review review = existingReview.get();
        review.setRating(reviewRequest.getRating());
        review.setComment(reviewRequest.getComment());
        
        Review updatedReview = reviewService.updateReview(review);
        ReviewResponseDto responseDto = convertToResponseDto(updatedReview);
        return ResponseEntity.ok(responseDto);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }
    
    // Helper method to convert Review entity to ReviewResponseDto
    private ReviewResponseDto convertToResponseDto(Review review) {
        ReviewResponseDto responseDto = new ReviewResponseDto();
        responseDto.setId(review.getId());
        responseDto.setUserId(review.getUserId());
        responseDto.setRestaurantId(review.getRestaurantId());
        responseDto.setRating(review.getRating());
        responseDto.setComment(review.getComment());
        responseDto.setCreatedAt(review.getCreatedAt());
        
        // Get user information
        try {
            Optional<User> user = userService.getUserById(review.getUserId());
            if (user.isPresent()) {
                responseDto.setUserName(user.get().getFirstName() + " " + user.get().getLastName());
                responseDto.setUserEmail(user.get().getEmail());
            }
        } catch (Exception e) {
            // If user not found, set default values
            responseDto.setUserName("Anonymous");
            responseDto.setUserEmail("");
        }
        
        return responseDto;
    }
}
