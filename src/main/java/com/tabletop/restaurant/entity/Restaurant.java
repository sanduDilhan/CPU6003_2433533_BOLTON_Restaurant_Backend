package com.tabletop.restaurant.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.List;

@Entity
@Table(name = "restaurants")
public class Restaurant {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Restaurant name is required")
    private String name;
    
    @NotBlank(message = "Cuisine type is required")
    private String cuisine;
    
    @NotBlank(message = "City is required")
    private String city;
    
    @NotBlank(message = "Address is required")
    private String address;
    
    private String phone;
    
    @DecimalMin(value = "0.0", message = "Rating must be at least 0.0")
    @DecimalMax(value = "5.0", message = "Rating must be at most 5.0")
    private Double rating;
    
    private String priceRange;
    
    @Column(length = 1000)
    private String description;
    
    private String imageUrl;
    
    @ElementCollection
    @CollectionTable(name = "restaurant_amenities", joinColumns = @JoinColumn(name = "restaurant_id"))
    @Column(name = "amenity")
    private List<String> amenities;
    
    @Embedded
    private Coordinates coordinates;
    
    @Embedded
    private OpeningHours openingHours;
    
    // Constructors
    public Restaurant() {}
    
    public Restaurant(String name, String cuisine, String city, String address) {
        this.name = name;
        this.cuisine = cuisine;
        this.city = city;
        this.address = address;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getCuisine() { return cuisine; }
    public void setCuisine(String cuisine) { this.cuisine = cuisine; }
    
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public Double getRating() { return rating; }
    public void setRating(Double rating) { this.rating = rating; }
    
    public String getPriceRange() { return priceRange; }
    public void setPriceRange(String priceRange) { this.priceRange = priceRange; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    
    public List<String> getAmenities() { return amenities; }
    public void setAmenities(List<String> amenities) { this.amenities = amenities; }
    
    public Coordinates getCoordinates() { return coordinates; }
    public void setCoordinates(Coordinates coordinates) { this.coordinates = coordinates; }
    
    public OpeningHours getOpeningHours() { return openingHours; }
    public void setOpeningHours(OpeningHours openingHours) { this.openingHours = openingHours; }
}
