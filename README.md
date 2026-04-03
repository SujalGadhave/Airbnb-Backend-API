# 🏨 Stayfinder

<div align="center">

<img src="https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" />
<img src="https://img.shields.io/badge/Spring%20Boot-3.4.2-6DB33F?style=for-the-badge&logo=springboot&logoColor=white" />
<img src="https://img.shields.io/badge/MySQL-8.0-4479A1?style=for-the-badge&logo=mysql&logoColor=white" />
<img src="https://img.shields.io/badge/JWT-Auth-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white" />
<img src="https://img.shields.io/badge/Stripe-Payments-635BFF?style=for-the-badge&logo=stripe&logoColor=white" />
<img src="https://img.shields.io/badge/Swagger-OpenAPI-85EA2D?style=for-the-badge&logo=swagger&logoColor=black" />
<img src="https://img.shields.io/badge/Maven-Build-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white" />

<br/><br/>

> **A production-ready REST API** for a full-featured hotel booking platform — built with Spring Boot, secured with JWT, powered by Stripe payments, and documented with Swagger/OpenAPI.

</div>

---

## 📋 Table of Contents

- [✨ Features](#-features)
- [🏗️ Architecture & Design](#️-architecture--design)
- [🛠️ Tech Stack](#️-tech-stack)
- [📁 Project Structure](#-project-structure)
- [🚀 Getting Started](#-getting-started)
- [⚙️ Configuration](#️-configuration)
- [🔐 Authentication](#-authentication-flow)
- [💡 Pricing Engine](#-smart-pricing-engine)
- [📡 API Reference](#-api-reference)
- [🧪 Testing with Postman](#-testing-with-postman)
- [🔁 Sample Test Flow](#-sample-test-flow)
- [📊 HTTP Status Codes](#-http-status-codes)

---

## ✨ Features

| Feature | Description |
|--------|-------------|
| 🔐 **JWT Authentication** | Stateless auth with access + HTTP-only refresh tokens |
| 🏨 **Hotel Management** | Full CRUD — name, city, amenities, photos, contact info |
| 🛏️ **Room Management** | Multi-type rooms with capacity, photos, and base pricing |
| 📦 **Inventory Control** | Date-range availability with open/close management |
| 💰 **Smart Pricing Engine** | Strategy pattern: Base, Surge, Occupancy, Holiday & Urgency pricing |
| 👥 **Guest Management** | Add, update, delete guests linked to user accounts |
| 📅 **Multi-step Booking** | Init → Add Guests → Payment → Confirm flow |
| 💳 **Stripe Integration** | Payment sessions + webhook event processing |
| 👮 **Role-Based Access** | Separate USER and ADMIN protected routes |
| 📄 **Swagger / OpenAPI** | Auto-generated interactive API docs |
| 🧱 **Global Exception Handling** | Centralized error responses via `@ControllerAdvice` |
| 📊 **Hotel Reporting** | Date-range booking reports per hotel |

---

## 🏗️ Architecture & Design

This project follows a clean **layered architecture** and applies several well-known design patterns:

```
Client Request
      │
      ▼
  Controller Layer        ← REST endpoints, request/response DTOs
      │
      ▼
  Service Layer           ← Business logic, transaction management
      │
      ▼
  Repository Layer        ← Spring Data JPA, database access
      │
      ▼
  Database (MySQL)
```

### 🎯 Design Patterns Used

- **Strategy Pattern** — Pluggable pricing strategies (`BasePricingStrategy`, `SurgePricingStrategy`, `OccupancyPricingStrategy`, `HolidayPricingStrategy`, `UrgencyPricingStrategy`) composed via `PricingService`
- **DTO Pattern** — Decoupled request/response objects prevent entity leakage
- **Repository Pattern** — Spring Data JPA repositories for all entities
- **Advice Pattern** — `@ControllerAdvice` for unified error handling
- **Filter Chain** — JWT authentication via Spring Security filter

---

## 🛠️ Tech Stack

| Layer | Technology | Version |
|-------|-----------|---------|
| Language | Java | 17 |
| Framework | Spring Boot | 3.4.2 |
| Security | Spring Security + JJWT | 0.12.6 |
| Database | MySQL | 8.0 |
| ORM | Spring Data JPA / Hibernate | — |
| Payments | Stripe Java SDK | 28.2.0 |
| API Docs | SpringDoc OpenAPI (Swagger UI) | 2.8.3 |
| Mapping | ModelMapper | 3.2.2 |
| Build Tool | Apache Maven | — |
| Utilities | Lombok | — |

---

## 📁 Project Structure

```
Airbnb-Backend-API/
├── pom.xml
└── src/
    └── main/
        ├── java/com/sujal/airbnb/
        │   ├── AirBnbApplication.java          # Spring Boot entry point
        │   ├── Advice/                         # Global exception handling (@ControllerAdvice)
        │   ├── Config/                         # Spring Security, CORS, Bean configs
        │   ├── Controller/
        │   │   ├── AuthController.java         # Signup, Login, Refresh Token
        │   │   ├── UserController.java         # Profile, Guests, My Bookings
        │   │   ├── HotelBrowseController.java  # Public hotel search & info
        │   │   ├── HotelController.java        # Admin hotel CRUD + reports
        │   │   ├── RoomAdminController.java    # Admin room CRUD
        │   │   ├── InventoryController.java    # Inventory management
        │   │   ├── HotelBookingController.java # Booking lifecycle
        │   │   └── WebHookController.java      # Stripe webhook handler
        │   ├── Dto/                            # Request & Response DTOs
        │   ├── Entities/
        │   │   ├── UserEntity.java
        │   │   ├── HotelEntity.java
        │   │   ├── HotelContactInfo.java
        │   │   ├── HotelMiniPriceEntity.java   # Cached min-price per hotel
        │   │   ├── RoomEntity.java
        │   │   ├── InventoryEntity.java        # Per-room, per-date inventory
        │   │   ├── BookingEntity.java
        │   │   └── GuestEntity.java
        │   ├── Enums/                          # BookingStatus, Gender, Role, etc.
        │   ├── Exception/                      # Custom exception classes
        │   ├── Repository/                     # Spring Data JPA repositories
        │   ├── Security/                       # JWT filter, UserDetailsService
        │   ├── Service/                        # Business logic interfaces + impls
        │   ├── Strategy/
        │   │   ├── PricingStrategy.java        # Interface
        │   │   ├── PricingService.java         # Orchestrator
        │   │   ├── BasePricingStrategy.java
        │   │   ├── SurgePricingStrategy.java
        │   │   ├── OccupancyPricingStrategy.java
        │   │   ├── HolidayPricingStrategy.java
        │   │   └── UrgencyPricingStrategy.java
        │   └── Utils/                          # Helper utilities
        └── resources/
            └── application.properties
```

---

## 🚀 Getting Started

### Prerequisites

- ☕ Java 17+
- 🐬 MySQL 8.0+
- 🔧 Maven 3.8+
- 💳 Stripe account (for payments)

### 1. Clone the Repository

```bash
git clone https://github.com/SujalGadhave/Airbnb-Backend-API.git
cd Airbnb-Backend-API
```

### 2. Create the MySQL Database

```sql
CREATE DATABASE airbnb_db;
```

### 3. Configure Environment Variables

Set the following environment variables (or update `application.properties`):

| Variable | Description |
|----------|-------------|
| `JWT_SECRET` | A strong secret key for signing JWTs |
| `FRONTEND_URL` | Your frontend origin URL (for CORS) |
| `STRIPE_SECRET_KEY` | Your Stripe secret API key |
| `STRIPE_WEBHOOK_SECRET` | Your Stripe webhook signing secret |

### 4. Build & Run

```bash
# Build
mvn clean install

# Run
mvn spring-boot:run
```

The server starts at: **`http://localhost:8080`**

---

## ⚙️ Configuration

```properties name=src/main/resources/application.properties
# Database
spring.datasource.url=jdbc:mysql://127.0.0.1:3306/airbnb_db
spring.datasource.username=root
spring.datasource.password=YOUR_DB_PASSWORD
spring.jpa.hibernate.ddl-auto=update
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect

# Server
server.servlet.context-path=/api/v1
server.port=8080

# JWT
jwt.secretKey=${JWT_SECRET}

# Frontend (CORS)
frontend.url=${FRONTEND_URL}

# Stripe
stripe.secret.key=${STRIPE_SECRET_KEY}
stripe.webhook.secret=${STRIPE_WEBHOOK_SECRET}

# Swagger
springdoc.swagger-ui.operationsSorter=method
springdoc.swagger-ui.tagsSorter=alpha
```

---

## 🌐 Base URL & Swagger

| Resource | URL |
|----------|-----|
| Base URL | `http://localhost:8080/api/v1` |
| Swagger UI | `http://localhost:8080/api/v1/swagger-ui.html` |
| OpenAPI JSON | `http://localhost:8080/api/v1/v3/api-docs` |

---

## 🔐 Authentication Flow

```
1. POST /auth/signup      → Register new user
2. POST /auth/login       → Get access token (JWT) + refresh token (HttpOnly cookie)
3. POST /auth/refresh     → Exchange refresh token for new access token
```

> All protected routes require: `Authorization: Bearer <access_token>`

### Token Details

| Token | Storage | Expiry |
|-------|---------|--------|
| Access Token | Response body | Short-lived |
| Refresh Token | HTTP-only cookie | Long-lived |

---

## 💡 Smart Pricing Engine

The pricing engine uses the **Strategy design pattern** to compose multiple pricing strategies dynamically:

```
Base Price
    │
    ├─▶ SurgePricingStrategy     → Applies admin-set surge multiplier
    ├─▶ OccupancyPricingStrategy → Increases price as occupancy rises
    ├─▶ HolidayPricingStrategy   → Adds premium on public holidays
    └─▶ UrgencyPricingStrategy   → Raises price for last-minute bookings

                    ▼
            Final Price (via PricingService)
```

This makes adding new pricing rules as simple as implementing `PricingStrategy` — zero changes to existing code.

---

## 📡 API Reference

### 🔐 Authentication — `/auth`

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| `POST` | `/auth/signup` | None | Register a new user |
| `POST` | `/auth/login` | None | Login and get JWT token |
| `POST` | `/auth/refresh` | Cookie | Refresh access token |

<details>
<summary><b>POST /auth/signup — Request Body</b></summary>

```json
{
    "email": "user@example.com",
    "password": "yourPassword123",
    "name": "John Doe"
}
```
</details>

<details>
<summary><b>POST /auth/login — Request Body</b></summary>

```json
{
    "email": "user@example.com",
    "password": "yourPassword123"
}
```
</details>

---

### 👤 User Profile — `/users`

> 🔒 All endpoints require Bearer Token

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/users/profile` | Get current user profile |
| `PATCH` | `/users/profile` | Update profile (name, DOB, gender) |
| `GET` | `/users/myBookings` | List all bookings for current user |
| `GET` | `/users/guests` | List all saved guests |
| `POST` | `/users/guests` | Add a new guest |
| `PUT` | `/users/guests/{guestId}` | Update a guest |
| `DELETE` | `/users/guests/{guestId}` | Remove a guest |

<details>
<summary><b>PATCH /users/profile — Request Body</b></summary>

```json
{
    "name": "John Updated",
    "dateOfBirth": "1990-05-15",
    "gender": "MALE"
}
```
> Gender options: `MALE`, `FEMALE`
</details>

<details>
<summary><b>POST /users/guests — Request Body</b></summary>

```json
{
    "name": "Jane Doe",
    "gender": "FEMALE",
    "age": 28
}
```
</details>

---

### 🏨 Hotel Browse — `/hotels` (Public)

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/hotels/search` | Search available hotels with filters |
| `GET` | `/hotels/{hotelId}/info` | Get detailed hotel information |

<details>
<summary><b>GET /hotels/search — Request Body</b></summary>

```json
{
    "city": "New York",
    "startDate": "2025-06-01",
    "endDate": "2025-06-05",
    "roomsCount": 2,
    "page": 0,
    "size": 10
}
```
</details>

---

### 🏢 Hotel Admin — `/admin/hotels`

> 🔒 Requires Admin Bearer Token

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/admin/hotels` | Create a new hotel |
| `GET` | `/admin/hotels` | List all your hotels |
| `GET` | `/admin/hotels/{hotelId}` | Get hotel by ID |
| `PUT` | `/admin/hotels/{hotelId}` | Update hotel details |
| `DELETE` | `/admin/hotels/{hotelId}` | Delete a hotel |
| `PATCH` | `/admin/hotels/{hotelId}/activate` | Activate hotel (make it bookable) |
| `GET` | `/admin/hotels/{hotelId}/bookings` | View all bookings for a hotel |
| `GET` | `/admin/hotels/{hotelId}/reports` | Get booking report (with date range) |

<details>
<summary><b>POST /admin/hotels — Request Body</b></summary>

```json
{
    "name": "Grand Hotel",
    "city": "New York",
    "photos": ["https://example.com/photo1.jpg"],
    "amenities": ["WiFi", "Pool", "Gym", "Spa", "Restaurant"],
    "contactInfo": {
        "address": "123 Main Street, New York, NY 10001",
        "phoneNumber": "+1-555-123-4567",
        "email": "info@grandhotel.com",
        "location": "40.7128,-74.0060"
    }
}
```
</details>

<details>
<summary><b>GET /admin/hotels/{id}/reports — Query Params</b></summary>

```
GET /admin/hotels/1/reports?startDate=2025-01-01&endDate=2025-01-31
```
</details>

---

### 🛏️ Room Admin — `/admin/hotels/{hotelId}/rooms`

> 🔒 Requires Admin Bearer Token

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/admin/hotels/{hotelId}/rooms` | Create a new room |
| `GET` | `/admin/hotels/{hotelId}/rooms` | List all rooms in a hotel |
| `PUT` | `/admin/hotels/{hotelId}/rooms/{roomId}` | Update room details |
| `DELETE` | `/admin/hotels/{hotelId}/rooms/{roomId}` | Delete a room |

<details>
<summary><b>POST /admin/hotels/{hotelId}/rooms — Request Body</b></summary>

```json
{
    "type": "Deluxe Suite",
    "basePrice": 299.99,
    "photos": ["https://example.com/room1.jpg"],
    "amenities": ["King Bed", "Ocean View", "Mini Bar", "Room Service"],
    "totalCount": 10,
    "capacity": 2
}
```
</details>

---

### 📦 Inventory — `/admin/inventory`

> 🔒 Requires Admin Bearer Token

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/admin/inventory/rooms/{roomId}` | View inventory for a room |
| `PATCH` | `/admin/inventory/rooms/{roomId}` | Update pricing / availability |

<details>
<summary><b>PATCH /admin/inventory/rooms/{roomId} — Request Body</b></summary>

```json
{
    "startDate": "2025-06-01",
    "endDate": "2025-06-30",
    "surgeFactor": 1.5,
    "closed": false
}
```
</details>

---

### 📅 Booking — `/booking`

> 🔒 Requires Bearer Token

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/booking/init` | Initialize a new booking |
| `POST` | `/booking/{bookingId}/addGuests` | Add guests to booking |
| `POST` | `/booking/{bookingId}/payments` | Initiate Stripe payment |
| `POST` | `/booking/{bookingId}/cancel` | Cancel a booking |
| `GET` | `/booking/{bookingId}/status` | Get booking status |

<details>
<summary><b>POST /booking/init — Request Body</b></summary>

```json
{
    "hotelId": 1,
    "roomId": 1,
    "checkInDate": "2025-06-01",
    "checkOutDate": "2025-06-05",
    "roomsCount": 1
}
```
</details>

<details>
<summary><b>POST /booking/{bookingId}/addGuests — Request Body</b></summary>

```json
[
    { "name": "John Doe", "gender": "MALE", "age": 30 },
    { "name": "Jane Doe", "gender": "FEMALE", "age": 28 }
]
```
</details>

---

### 💳 Webhooks — `/webhook`

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/webhook/payment` | Stripe payment event handler |

> ⚠️ This endpoint is called by **Stripe**, not by users. Requires `Stripe-Signature` header.

---

## 🧪 Testing with Postman

### Step 1 — Set Up Environment

Create a Postman environment with:

| Variable | Value | Description |
|----------|-------|-------------|
| `base_url` | `http://localhost:8080/api/v1` | API base URL |
| `access_token` | _(empty)_ | Set automatically after login |

### Step 2 — Configure Auth Header

```
Authorization: Bearer {{access_token}}
```

### Step 3 — Auto-capture Token on Login

Add this to the **Tests** tab of your Login request:

```javascript
if (pm.response.code === 200) {
    var jsonData = pm.response.json();
    pm.environment.set("access_token", jsonData.data.accessToken);
}
```

---

## 🔁 Sample Test Flow

```
 1. POST /auth/signup           → Register your account
 2. POST /auth/login            → Get JWT access token
 3. POST /admin/hotels          → Create a hotel (admin)
 4. POST /admin/hotels/{id}/rooms → Add room types
 5. PATCH /admin/hotels/{id}/activate → Make hotel bookable
 6. GET  /hotels/search         → Search for hotels (as user)
 7. GET  /hotels/{id}/info      → View hotel details
 8. POST /booking/init          → Initialize a booking
 9. POST /booking/{id}/addGuests → Add traveller details
10. POST /booking/{id}/payments → Pay with Stripe
11. GET  /booking/{id}/status   → Confirm booking is active
```

---

## 📊 HTTP Status Codes

| Code | Status | Meaning |
|------|--------|---------|
| `200` | ✅ OK | Request succeeded |
| `201` | ✅ Created | Resource successfully created |
| `204` | ✅ No Content | Success with no response body |
| `400` | ❌ Bad Request | Invalid input / validation error |
| `401` | ❌ Unauthorized | Missing or invalid JWT |
| `403` | ❌ Forbidden | Authenticated but insufficient permissions |
| `404` | ❌ Not Found | Resource does not exist |
| `500` | ❌ Internal Server Error | Unexpected server-side error |

---

## 🤝 Contributing

Contributions, issues, and feature requests are welcome!

1. Fork the repository
2. Create a feature branch: `git checkout -b feature/amazing-feature`
3. Commit your changes: `git commit -m 'Add amazing feature'`
4. Push to the branch: `git push origin feature/amazing-feature`
5. Open a Pull Request

---

## 👨‍💻 Author

**Sujal Gadhave**

[![GitHub](https://img.shields.io/badge/GitHub-SujalGadhave-181717?style=flat-square&logo=github)](https://github.com/SujalGadhave)

---

<div align="center">

⭐ **If you found this project helpful, please give it a star!** ⭐

</div>
