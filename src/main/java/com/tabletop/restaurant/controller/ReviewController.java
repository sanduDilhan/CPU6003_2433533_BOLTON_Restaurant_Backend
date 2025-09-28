package com.tabletop.restaurant.controller;

import com.tabletop.restaurant.entity.Review;
import com.tabletop.restaurant.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/reviews")
@CrossOrigin(origins = "http://localhost:3000")
public class ReviewController {
    
    @Autowired
    private ReviewService reviewService;
    
    @GetMapping
    public ResponseEntity<List<Review>> getAllReviews() {
        List<Review> reviews = reviewService.getAllReviews();
        return ResponseEntity.ok(reviews);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Review> getReviewById(@PathVariable Long id) {
        Optional<Review> review = reviewService.getReviewById(id);
        return review.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<Review>> getReviewsByRestaurantId(@PathVariable Long restaurantId) {
        List<Review> reviews = reviewService.getReviewsByRestaurantId(restaurantId);
        return ResponseEntity.ok(reviews);
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Review>> getReviewsByUserId(@PathVariable Long userId) {
        List<Review> reviews = reviewService.getReviewsByUserId(userId);
        return ResponseEntity.ok(reviews);
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
    public ResponseEntity<Review> createReview(@RequestBody Review review) {
        Review savedReview = reviewService.createReview(review);
        return ResponseEntity.ok(savedReview);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Review> updateReview(@PathVariable Long id, @RequestBody Review review) {
        review.setId(id);
        Review updatedReview = reviewService.updateReview(review);
        return ResponseEntity.ok(updatedReview);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }
}
