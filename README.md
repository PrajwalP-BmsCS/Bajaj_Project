# Cinema Booking Platform - Backend API

> A production-grade, scalable Spring Boot backend system for movie ticket booking with comprehensive seat management, JWT authentication, and role-based access control.

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-14+-blue.svg)](https://www.postgresql.org/)
[![Redis](https://img.shields.io/badge/Redis-6+-red.svg)](https://redis.io/)
[![Kafka](https://img.shields.io/badge/Apache%20Kafka-3.x-black.svg)](https://kafka.apache.org/)
---

## 📋 Table of Contents

- [Overview](#overview)
- [Architecture & Features](#architecture--features)
- [Technology Stack](#technology-stack)
- [Getting Started](#getting-started)
- [API Documentation](#api-documentation)
- [Authentication & Authorization](#authentication--authorization)
- [Core Modules](#core-modules)
- [Testing](#testing)

---

## 🎯 Overview

This project implements a comprehensive movie ticket booking system with enterprise-grade features including:

- **Multi-venue support** with dynamic movie scheduling
- **Real-time seat management** with hold/book mechanism and automatic expiration
- **Dual pricing tiers** (Regular & Premium seating)
- **Secure authentication** using JWT tokens with role-based access
- **Complete booking lifecycle** from search to confirmation
- **RESTful API design** following industry best practices
- **Full test coverage** with integration tests

Built as a showcase of clean architecture, SOLID principles, and production-ready Spring Boot development.

---

## 🏗 Architecture & Features

### System Architecture
![Architecture Diagram](https://github.com/user-attachments/assets/9af10358-16d4-40c4-8840-fe20d80c59fb)

### Key Capabilities

| Feature | Description |
|---------|-------------|
| **User Management** | Registration, authentication, profile management with JWT |
| **Venue System** | Multi-location venue management with city-based filtering |
| **Movie Catalog** | Complete CRUD operations with filtering by title, genre, date, and location |
| **Show Scheduling** | Flexible show timing with customizable seat pricing per show |
| **Seat Management** | Real-time availability tracking with temporary hold mechanism |
| **Booking Engine** | Two-phase booking (hold → confirm) with automatic seat release |
| **Booking History** | Complete audit trail of all transactions |
| **Redis Caching** | Smart caching with automatic cache invalidation |
| **Event Notifications** | Kafka-based booking confirmation messages |
| **API Documentation** | Interactive Swagger UI for testing and documentation |

### Security Features

- ✅ JWT-based stateless authentication
- ✅ Role-based access control (USER/ADMIN)
- ✅ Password encryption with BCrypt
- ✅ Secured endpoints with Spring Security
- ✅ Bearer token authorization

### Data Integrity

- ✅ Transactional booking operations
- ✅ Optimistic locking for seat reservations
- ✅ Automatic seat hold expiration
- ✅ Referential integrity with foreign keys
- ✅ Validation at API and service layers

---

## 🛠 Technology Stack

### Core Framework
- **Java 17** - LTS version with modern language features
- **Spring Boot 3.x** - Application framework
- **Spring Data JPA** - Data persistence layer
- **Hibernate** - ORM implementation
- **Spring Security** - Authentication and authorization

### Database
- **PostgreSQL 14+** - Primary relational database
- **Flyway/Liquibase** - Database migration management
- **Redis** - High-performance caching layer

### Messaging & Events
- **Apache Kafka** - Booking confirmation notifications
- **Spring Kafka** - Kafka integration framework

### Security & Authentication
- **JWT (JSON Web Tokens)** - Stateless authentication
- **BCrypt** - Password hashing algorithm

### Documentation
- **springdoc-openapi** - OpenAPI 3.0 specification
- **Swagger UI** - Interactive API documentation

### Testing
- **JUnit 5** - Unit testing framework
- **MockMvc** - Spring MVC test framework
- **AssertJ** - Fluent assertions

### Build & Deployment
- **Maven** - Dependency management and build
- **Docker** - Containerization
- **Docker Compose** - Multi-container orchestration

---

## 🚀 Getting Started

### Prerequisites

```bash
# Required
Java 17+
Maven 3.8+
PostgreSQL 14+
Redis 6+
Apache Kafka 3.x

# Verify installations
java -version
mvn -version
redis-cli --version
kafka-topics.sh --version
```

### Local Development Setup

1. **Clone the repository**
```bash
git clone <repository-url>
cd cinema_project
```

2. **Configure database**

Create a PostgreSQL database:
```sql
CREATE DATABASE cinema_booking;
CREATE USER cinema_user WITH PASSWORD 'your_password';
GRANT ALL PRIVILEGES ON DATABASE cinema_booking TO cinema_user;
```

3. **Configure application properties**

Update `src/main/resources/application.yml`:
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/cinema_booking
    username: cinema_user
    password: your_password
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
  
  # Redis Configuration
  redis:
    host: localhost
    port: 6379
    password: your_redis_password  # if authentication is enabled
    
  # Kafka Configuration
  kafka:
    bootstrap-servers: localhost:9092
    consumer:
      group-id: cinema-booking-group
      auto-offset-reset: earliest
    producer:
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: org.springframework.kafka.support.serializer.JsonSerializer

jwt:
  secret: your-secret-key-here
  expiration: 86400000  # 24 hours in milliseconds

# Cache Configuration
cache:
  ttl: 300  # 5 minutes
```

4. **Build the project**
```bash
mvn clean install
```

5. **Run the application**
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### Using Docker Compose (Recommended)

```bash
# Start all services (PostgreSQL, Redis, Kafka, Zookeeper, Application)
docker-compose up -d

# View logs
docker-compose logs -f

# Stop services
docker-compose down
```

**Docker Compose includes**:
- PostgreSQL database
- Redis cache server
- Apache Kafka broker
- Zookeeper (for Kafka coordination)
- Cinema Booking API application

---

## 📖 API Documentation
<img width="824" height="791" alt="image" src="https://github.com/user-attachments/assets/74c99281-4556-47a1-90c9-c2d22bd0d445" />

### Interactive Documentation

Once the application is running, access the Swagger UI:

**Swagger UI**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

**OpenAPI JSON**: [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

### Quick Start Guide

1. **Register a user** via `POST /auth/register`
2. **Login** to receive JWT token via `POST /auth/login`
3. **Add token** to all subsequent requests:
   ```
   Authorization: Bearer <your-jwt-token>
   ```

### API Endpoint Overview

#### Authentication (`/auth`)
| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| POST | `/auth/register` | Register new user | None |
| POST | `/auth/login` | Login and receive JWT | None |

#### User Management (`/user`)
| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| GET | `/user/me` | Get current user profile | USER |
| PUT | `/user/me` | Update user profile | USER |

#### Venue Management (`/venues`)
| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| POST | `/venues` | Create new venue | ADMIN |
| GET | `/venues` | List all venues | USER |
| GET | `/venues/{id}` | Get venue details | USER |
| GET | `/venues/{id}/movies` | Get movies at venue | USER |
| DELETE | `/venues/{id}` | Delete venue | ADMIN |

#### Movie Management (`/movie`)
| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| POST | `/movie` | Add new movie | ADMIN |
| GET | `/movie` | Search movies (with filters) | USER |
| GET | `/movie/{id}` | Get movie details | USER |
| PUT | `/movie/{id}` | Update movie | ADMIN |
| DELETE | `/movie/{id}` | Delete movie | ADMIN |

**Search Filters**: `?title=...&genre=...&date=YYYY-MM-DD&location=...`

#### Show Management (`/shows`)
| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| POST | `/shows` | Create new show | ADMIN |
| GET | `/shows/movie/{movieId}` | Get shows for a movie | USER |
| PUT | `/shows/{id}` | Update show details | ADMIN |

#### Seat & Booking (`/shows`, `/movie/booking`)
| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| GET | `/shows/{showId}/seats` | View seat availability | USER |
| POST | `/movie/booking/hold` | Hold seats temporarily | USER |
| POST | `/movie/booking/seats` | Confirm booking | USER |
| GET | `/movie/booking/my` | View my bookings | USER |
| GET | `/movie/bookings` | View all bookings | ADMIN |

---

## 🔐 Authentication & Authorization

### User Roles

| Role | Permissions |
|------|-------------|
| **USER** | Browse movies, view shows, hold/book seats, view own bookings |
| **ADMIN** | All USER permissions + venue/movie/show management, view all bookings |

### JWT Token Flow

```
1. User registers/logs in
   ↓
2. Server generates JWT token
   ↓
3. Client stores token
   ↓
4. Client sends token in Authorization header
   ↓
5. Server validates token and extracts user info
   ↓
6. Request proceeds if authorized
```

### Example Usage

```bash
# Register
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{"email":"user@example.com","password":"password123","role":"USER"}'

# Login
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"user@example.com","password":"password123"}'

# Response: {"token":"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."}

# Use token
curl -X GET http://localhost:8080/movie \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

---

## 🧩 Core Modules

### Project Structure

```
src/main/java/com/cinema_package/cinema_project/
├── auth/                 # Authentication & JWT handling
│   ├── AuthController.java
│   ├── AuthService.java
│   └── JwtUtil.java
├── user/                 # User profile management
│   ├── UserController.java
│   ├── UserService.java
│   └── User.java
├── venue/                # Venue CRUD operations
│   ├── VenueController.java
│   ├── VenueService.java
│   └── Venue.java
├── movie/                # Movie catalog & booking
│   ├── MovieController.java
│   ├── MovieService.java
│   ├── Movie.java
│   └── BookingHistory.java
├── show/                 # Show scheduling
│   ├── ShowController.java
│   ├── ShowService.java
│   └── Show.java
├── seat/                 # Seat availability management
│   ├── SeatController.java
│   ├── SeatService.java
│   └── Seat.java
├── kafka/                # Kafka event messaging
│   ├── BookingEventProducer.java
│   └── events/
├── cache/                # Redis caching annotations
│   └── CacheConfig.java
├── config/               # Application configuration
│   ├── SecurityConfig.java
│   ├── SwaggerConfig.java
│   ├── RedisConfig.java
│   ├── KafkaConfig.java
│   └── JwtAuthenticationFilter.java
└── CinemaProjectApplication.java
```

### Seat Management System

#### Seat Categories
- **REGULAR**: Standard seating with base pricing
- **PREMIUM**: Enhanced seating with premium pricing

#### Seat Status Flow
```
AVAILABLE → HELD → BOOKED
     ↑         ↓
     └─────────┘
   (hold expires)
```

#### Hold Mechanism
- Seats can be temporarily held during checkout
- Hold expires automatically after configured timeout
- Expired holds return seats to AVAILABLE status
- Prevents double-booking during user decision time

### Redis Caching Layer

The application uses Redis with Spring Cache annotations for performance optimization:

**Implementation**:
- `@Cacheable`: Automatically caches method results (e.g., venue lists, show details)
- `@CacheEvict`: Removes stale cache entries on updates/deletions
- **Cache Names**: `venues`, `shows`, `movies`, `seats`
- **Benefits**: Reduced database queries, faster API response times

**Example Usage**:
```java
@Cacheable(value = "venues")
public List<Venue> getAllVenues() { }

@CacheEvict(value = "venues", allEntries = true)
public Venue updateVenue(Venue venue) { }
```

### Kafka Messaging

Kafka is used for asynchronous booking notifications:

**Implementation**:
- **Producer**: Sends booking confirmation message after successful ticket booking
- **Topic**: `booking-confirmations`
- **Use Case**: Decouples booking process from notification delivery
- **Message Format**: JSON containing booking details (bookingId, userEmail, showId, seats, timestamp)

**Flow**:
```
User Books Ticket → Save to DB → Send Kafka Message → (Future: Email/SMS Service)
```

---

## 🧪 Testing

### Test Coverage Summary

The application includes **7 comprehensive integration tests** covering all critical workflows with **100% pass rate**.

### Test Suite Breakdown

| Test Class | Purpose | Coverage |
|------------|---------|----------|
| **AuthIntegrationTest** | Authentication success flows | ✅ User registration<br>✅ JWT token generation |
| **AuthLoginFailureIntegrationTest** | Authentication failure scenarios | ✅ Invalid credentials handling<br>✅ 401 Unauthorized validation |
| **VenueCrudIntegrationTest** | Venue lifecycle operations | ✅ Create, Read, Update, Delete<br>✅ Admin role verification |
| **MovieCrudIntegrationTest** | Movie management workflows | ✅ Full CRUD operations<br>✅ Venue-Movie relationships |
| **ShowCrudIntegrationTest** | Show scheduling operations | ✅ Show CRUD with dependencies<br>✅ Time & seat configuration |
| **BookingFlowIntegrationTest** | End-to-end booking workflow | ✅ Multi-user scenarios<br>✅ Seat hold & confirm<br>✅ Kafka event production<br>✅ Database transactions |

### Key Test Achievements

- **Security Testing**: JWT authentication, role-based authorization (USER/ADMIN)
- **Database Integration**: PostgreSQL transactions, optimistic locking, referential integrity
- **Cache Validation**: Redis cache operations and invalidation
- **Event-Driven**: Kafka message production verification
- **E2E Scenarios**: Complete user journeys from registration to booking confirmation

### Running Tests

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=BookingFlowIntegrationTest

# Run with coverage report
mvn clean test jacoco:report
```

### Test Results
```
Tests run: 7, Failures: 0, Errors: 0, Skipped: 0
✅ 100% SUCCESS RATE
```

---

## 📦 Deployment

### Docker Deployment

```bash
# Build Docker image
docker build -t cinema-booking-api .

# Run with Docker Compose
docker-compose up -d
```

### Production Checklist

- [ ] Configure production database credentials
- [ ] Set strong JWT secret key
- [ ] Enable HTTPS/TLS
- [ ] Configure Redis password authentication
- [ ] Set up Kafka cluster (if not using managed service)
- [ ] Configure environment-specific `application-prod.yml`
- [ ] Set up logging and monitoring
- [ ] Configure backup strategy for PostgreSQL
- [ ] Review and harden security settings

---

## 🤝 Contributing

Contributions are welcome! Please follow these guidelines:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

**Code Standards**:
- Follow Java naming conventions
- Write unit tests for new features
- Update documentation as needed
- Ensure all tests pass before submitting PR

---

## 👤 Author

**Prajwal P**  
Backend Engineering Intern Project

---

