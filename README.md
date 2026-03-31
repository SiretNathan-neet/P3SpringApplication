# Project 3 - Spring Boot API

REST API for rental property management with JWT authentication system.

## Prerequisites

- Java 21
- Maven 3.8+
- MySQL 8.0+
- Node.js 18+ (for Angular frontend)

## Installation

### 1. Clone the project
```bash
git clone <https://github.com/SiretNathan-neet/P3SpringApplication.git>
cd p3springboot
```

### 2. Database configuration

#### Create MySQL database
```sql
CREATE DATABASE P3Springboot;
```

#### Configure credentials

1. Copy the example configuration file:
```bash
cp src/main/resources/application.properties.example src/main/resources/application.properties.example
```

2. Edit `src/main/resources/application.properties.example` and replace the placeholders (don't forget to remove the .example):
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/P3Springboot
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD
```

### 3. Run the application
```bash
mvn spring-boot:run
```

The API will be accessible at `http://localhost:3001`

### 4. Swagger documentation

Once the application is running, access the interactive documentation using Swagger:
```
http://localhost:3001/swagger-ui/index.html
```

## Database structure

The application uses Hibernate with `ddl-auto=update`, tables will be created automatically on first launch:

- `users` - Application users
- `rentals` - Rental listings
- `messages` - Messages between users

## Main endpoints

### Authentication
- `POST /api/auth/register` - Create an account
- `POST /api/auth/email` - Login
- `GET /api/auth/me` - Get current user information (protected)

### Rentals
- `GET /api/rentals` - List all rentals (protected)
- `GET /api/rentals/{id}` - Get rental details (protected)
- `POST /api/rentals` - Create a rental (protected)
- `PUT /api/rentals/{id}` - Update a rental (protected, owner only)

### Messages
- `POST /api/messages` - Send a message (protected)

### Users
- `GET /api/user/{id}` - Get user information (protected)

## Angular Frontend

The frontend is provided separately. To install :
```bash
git clone <https://github.com/OpenClassrooms-Student-Center/Developpez-le-back-end-en-utilisant-Java-et-Spring.git>
```

The frontend will be accessible at `http://localhost:4200`

## Technologies used

- Spring Boot 4.0.1
- Spring Security with JWT
- MySQL
- Lombok
- SpringDoc OpenAPI (Swagger)