# TableTop.lk Backend - Spring Boot Application

This is the backend service for the TableTop.lk restaurant booking platform, built with Spring Boot.

## Features

- RESTful API endpoints for restaurants, bookings, users, and reviews
- JPA/Hibernate for data persistence
- Spring Security for authentication and authorization
- H2 in-memory database for development
- Mock data initialization
- CORS configuration for frontend integration

## Quick Start

### Prerequisites
- Java 17 or higher
- Maven 3.6 or higher

### Running the Application

1. Navigate to the backend directory:
   ```bash
   cd backend
   ```

2. Install dependencies:
   ```bash
   mvn clean install
   ```

3. Run the application:
   ```bash
   mvn spring-boot:run
   ```

The application will start on `http://localhost:8080`

### Accessing the Database Console

Once the application is running, you can access the H2 database console at:
`http://localhost:8080/h2-console`

- **JDBC URL**: `jdbc:h2:mem:restaurantdb`
- **Username**: `sa`
- **Password**: `password`

## API Documentation

### Base URL
All API endpoints are prefixed with `/api`

### Authentication
Currently using mock authentication. In production, implement JWT or OAuth2.

### Endpoints

#### Restaurants
- `GET /api/restaurants` - List all restaurants
- `GET /api/restaurants/{id}` - Get restaurant by ID
- `GET /api/restaurants/top-rated` - Get top-rated restaurants
- `POST /api/restaurants` - Create restaurant (Admin only)
- `PUT /api/restaurants/{id}` - Update restaurant (Admin only)
- `DELETE /api/restaurants/{id}` - Delete restaurant (Admin only)

#### Bookings
- `GET /api/bookings` - List all bookings
- `GET /api/bookings/{id}` - Get booking by ID
- `GET /api/bookings/user/{userId}` - Get user's bookings
- `GET /api/bookings/restaurant/{restaurantId}` - Get restaurant's bookings
- `POST /api/bookings` - Create booking
- `PUT /api/bookings/{id}` - Update booking
- `PUT /api/bookings/{id}/confirm` - Confirm booking
- `PUT /api/bookings/{id}/cancel` - Cancel booking
- `DELETE /api/bookings/{id}` - Delete booking

#### Users
- `GET /api/users` - List all users
- `GET /api/users/{id}` - Get user by ID
- `GET /api/users/username/{username}` - Get user by username
- `POST /api/users` - Create user
- `PUT /api/users/{id}` - Update user
- `POST /api/users/{userId}/favorites/{restaurantId}` - Add to favorites
- `DELETE /api/users/{userId}/favorites/{restaurantId}` - Remove from favorites
- `DELETE /api/users/{id}` - Delete user

#### Reviews
- `GET /api/reviews` - List all reviews
- `GET /api/reviews/{id}` - Get review by ID
- `GET /api/reviews/restaurant/{restaurantId}` - Get restaurant reviews
- `GET /api/reviews/user/{userId}` - Get user reviews
- `GET /api/reviews/restaurant/{restaurantId}/stats` - Get restaurant review stats
- `POST /api/reviews` - Create review
- `PUT /api/reviews/{id}` - Update review
- `DELETE /api/reviews/{id}` - Delete review

## Data Models

### Restaurant
```json
{
  "id": 1,
  "name": "Restaurant Name",
  "cuisine": "Cuisine Type",
  "city": "City",
  "address": "Full Address",
  "phone": "Phone Number",
  "rating": 4.5,
  "priceRange": "$$",
  "description": "Restaurant description",
  "imageUrl": "Image URL",
  "amenities": ["WiFi", "Parking"],
  "coordinates": {
    "latitude": 6.9271,
    "longitude": 79.8612
  },
  "openingHours": {
    "monday": "11:00 AM - 10:00 PM",
    "tuesday": "11:00 AM - 10:00 PM"
  }
}
```

### Booking
```json
{
  "id": 1,
  "userId": 1,
  "restaurantId": 1,
  "date": "2024-02-15",
  "time": "19:00",
  "partySize": 4,
  "status": "CONFIRMED",
  "specialRequests": "Window table preferred"
}
```

### User
```json
{
  "id": 1,
  "username": "john_doe",
  "email": "john@example.com",
  "firstName": "John",
  "lastName": "Doe",
  "phone": "+94 77 123 4567",
  "role": "USER",
  "favorites": [1, 3, 5]
}
```

### Review
```json
{
  "id": 1,
  "userId": 1,
  "restaurantId": 1,
  "rating": 5,
  "comment": "Excellent food and service!"
}
```

## Configuration

### Application Properties
The application uses `application.yml` for configuration:

```yaml
server:
  port: 8080

spring:
  datasource:
    url: jdbc:h2:mem:restaurantdb
    driver-class-name: org.h2.Driver
    username: sa
    password: password
  
  h2:
    console:
      enabled: true
      path: /h2-console
  
  jpa:
    hibernate:
      ddl-auto: create-drop
    show-sql: true

cors:
  allowed-origins: http://localhost:3000
  allowed-methods: GET,POST,PUT,DELETE,OPTIONS
  allowed-headers: "*"
  allow-credentials: true
```

## Mock Data

The application automatically loads mock data from JSON files in `src/main/resources/data/`:

- `restaurants.json` - Sample restaurant data
- `users.json` - Sample user accounts
- `bookings.json` - Sample booking data
- `reviews.json` - Sample review data

## Security

- CORS enabled for frontend integration
- Basic authentication setup (extend for production)
- Password encryption using BCrypt
- Role-based access control

## Development

### Adding New Endpoints

1. Create entity in `entity/` package
2. Create repository in `repository/` package
3. Create service in `service/` package
4. Create controller in `controller/` package
5. Add security configuration if needed

### Database Changes

1. Modify entity classes
2. Update mock data JSON files
3. Test with H2 console

## Testing

Run tests with:
```bash
mvn test
```

## Production Considerations

- Replace H2 with PostgreSQL/MySQL
- Implement proper authentication (JWT/OAuth2)
- Add input validation
- Implement logging
- Add monitoring and health checks
- Configure proper CORS settings
- Add API rate limiting
- Implement caching
- Add database migrations

## Troubleshooting

### Common Issues

1. **Port already in use**: Change port in `application.yml`
2. **Database connection issues**: Check H2 console access
3. **CORS errors**: Verify CORS configuration
4. **Data not loading**: Check JSON file format and paths

### Logs

Enable debug logging by setting:
```yaml
logging:
  level:
    com.tabletop: DEBUG
```

## Support

For issues and questions, please contact the development team.


