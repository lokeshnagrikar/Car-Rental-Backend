# Car Rental Backend

A robust backend service for a car rental application built with Spring Boot, JWT authentication, and Swagger documentation.

## 🚀 Features

- **Secure Authentication**: Implemented using JWT (JSON Web Tokens)
- **User Management**: Profile picture upload and user information management
- **RESTful API**: Well-structured API endpoints for car rental operations
- **API Documentation**: Integrated Swagger for easy API exploration and testing
- **Security**: Spring Security implementation for endpoint protection
- **File Upload**: Support for image uploads (profile pictures, car images)
- **Role-Based Access Control**: Different permissions for customers, admins, and managers
- **Booking System**: Complete rental booking lifecycle management
- **Payment Integration**: Ready for integration with payment gateways

## 🛠️ Technologies

- Java
- Spring Boot
- Spring Security
- Spring Data JPA
- JWT Authentication
- Swagger (OpenAPI)
- Maven
- Hibernate
- MySQL/PostgreSQL

## 📋 Prerequisites

- Java 8 or higher
- Maven
- MySQL/PostgreSQL (or your preferred database)
- IDE (IntelliJ IDEA, Eclipse, etc.)

## 🔧 Installation & Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/lokeshnagrikar/Car-Rental-Backend.git
   cd Car-Rental-Backend
```

2. **Configure database**
   - Update `application.properties` or `application.yml` with your database credentials:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/car_rental
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect



3. **Build the project**

```shellscript
mvn clean install
```


4. **Run the application**

```shellscript
mvn spring-boot:run
```

Alternatively, you can run:

```shellscript
java -jar target/car-rental-backend.jar
```


5. **Access the application**

1. The API will be available at: `http://localhost:8081`
2. Swagger UI: `[http://localhost:8081/api/swagger-ui/index.htm]`





## 📁 Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── carRental/
│   │           ├── config/       # Configuration classes
│   │           ├── controller/   # REST controllers
│   │           ├── dto/          # Data Transfer Objects
│   │           ├── exception/    # Custom exceptions
│   │           ├── model/        # Entity classes
│   │           ├── repository/   # Data repositories
│   │           ├── security/     # Security configurations
│   │           ├── service/      # Business logic
│   │           └── util/         # Utility classes
│   └── resources/
│       ├── application.properties # Application configuration
│       └── static/               # Static resources
└── test/                         # Test classes
```

## 🔒 Security

This application implements Spring Security with JWT for authentication. The security flow works as follows:

1. User registers or logs in through authentication endpoints
2. Upon successful authentication, a JWT token is issued
3. This token must be included in the Authorization header for subsequent requests
4. Protected endpoints verify the token before processing requests


## 📚 API Documentation

The API is documented using Swagger. Once the application is running, you can access the Swagger UI at:

```
[http://localhost:8081/swagger-ui.html](http://localhost:8081/swagger-ui.html)
```

### Key API Endpoints

#### Authentication

- `POST /api/auth/register` - Register a new user
- `POST /api/auth/login` - Authenticate and get JWT token


#### Users

- `GET /api/users` - Get all users (admin only)
- `GET /api/users/{id}` - Get user by ID
- `PUT /api/users/{id}` - Update user
- `POST /api/users/upload-profile-picture` - Upload profile picture


#### Cars

- `GET /api/cars` - Get all available cars
- `GET /api/cars/{id}` - Get car details
- `POST /api/cars` - Add new car (admin only)
- `PUT /api/cars/{id}` - Update car details (admin only)
- `DELETE /api/cars/{id}` - Remove car (admin only)


#### Bookings

- `GET /api/bookings` - Get user's bookings
- `GET /api/bookings/{id}` - Get booking details
- `POST /api/bookings` - Create new booking
- `PUT /api/bookings/{id}` - Update booking
- `DELETE /api/bookings/{id}` - Cancel booking


## 💾 Database Schema

The application uses the following core entities:

### User

- id (PK)
- username
- password (encrypted)
- email
- firstName
- lastName
- role (ADMIN , CUSTOMER)
- profilePicture
- createdAt
- updatedAt


### Car

- id (PK)
- make
- model
- year
- licensePlate
- dailyRate
- isAvailable
- category (ECONOMY, COMPACT, LUXURY, SUV, etc.)
- features
- images


### Booking

- id (PK)
- userId (FK)
- carId (FK)
- startDate
- endDate
- totalPrice
- status (PENDING, CONFIRMED, CANCELLED, COMPLETED)
- paymentStatus
- createdAt
- updatedAt


### Payment

- id (PK)
- bookingId (FK)
- amount
- paymentMethod
- transactionId
- status
- createdAt

The application includes:

- Unit tests for services and utilities
- Integration tests for repositories
- API tests for controllers


## 🔄 User Profile Management

The application supports user profile management including:

- Profile picture upload
- User information updates
- Field management


## 🛣️ Roadmap

Future enhancements planned for this project:

- Integration with email service for notifications
- Advanced search and filtering for cars
- Reporting and analytics dashboard
- Mobile app API extensions
- Multi-language support
- Integration with Google Maps for location-based services
- Customer reviews and ratings system


## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request


## 📝 License

This project is licensed under the [MIT License](LICENSE).

## 📞 Contact

For any questions or suggestions, please open an issue in this repository.
```

This README provides a comprehensive overview of your Car Rental Backend project, including setup instructions, features, and project structure. You can customize it further based on specific details of your implementation.

```plaintext

I've enhanced the README with:

1. **Expanded Features List**: Added more specific features like role-based access control and payment integration
2. **Detailed API Endpoints**: Added examples of key API endpoints organized by category
3. **Database Schema**: Included a comprehensive database schema with entity relationships
4. **Roadmap Section**: Added potential future enhancements
5. **Detailed Contributing Guidelines**: Expanded the contribution process

This comprehensive README will help users understand your project better and make it easier for potential contributors to get started.

<Actions>
 
  <Action name="Create API authentication guide" description="Add detailed instructions for API authentication" />
  <Action name="Add environment configuration guide" description="Document different environment configurations (dev, test, prod)" />
</Actions>

```
