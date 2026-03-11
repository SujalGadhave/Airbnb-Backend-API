# Airbnb Backend API

A Spring Boot REST API for an Airbnb-like hotel booking platform with JWT authentication, hotel management, room inventory, and Stripe payment integration.

## Base URL

```
http://localhost:8080/api/v1
```

## API Documentation (Swagger)

Once the server is running, you can access the Swagger UI at:
```
http://localhost:8080/api/v1/swagger-ui.html
```

---

## API Endpoints for Postman Testing

### 🔐 Authentication Endpoints

#### 1. Sign Up
- **URL:** `POST /auth/signup`
- **Description:** Creates a new user account
- **Authentication:** None required
- **Request Body:**
```json
{
    "email": "user@example.com",
    "password": "yourPassword123",
    "name": "John Doe"
}
```

#### 2. Login
- **URL:** `POST /auth/login`
- **Description:** Authenticates user and returns JWT access token (refresh token set in cookie)
- **Authentication:** None required
- **Request Body:**
```json
{
    "email": "user@example.com",
    "password": "yourPassword123"
}
```

#### 3. Refresh Token
- **URL:** `POST /auth/refresh`
- **Description:** Generates new access token using refresh token from cookies
- **Authentication:** Refresh token in cookies

---

### 👤 User Profile Endpoints

> **Note:** All user endpoints require Bearer token authentication

#### 4. Get My Profile
- **URL:** `GET /users/profile`
- **Description:** Retrieves the current user's profile details
- **Authentication:** Bearer Token

#### 5. Update My Profile
- **URL:** `PATCH /users/profile`
- **Description:** Updates the current user's profile
- **Authentication:** Bearer Token
- **Request Body:**
```json
{
    "name": "John Updated",
    "dateOfBirth": "1990-05-15",
    "gender": "MALE"
}
```
> Gender options: `MALE`, `FEMALE`

#### 6. Get My Bookings
- **URL:** `GET /users/myBookings`
- **Description:** Fetches all bookings for the current user
- **Authentication:** Bearer Token

#### 7. Get All Guests
- **URL:** `GET /users/guests`
- **Description:** Retrieves all guests associated with the user
- **Authentication:** Bearer Token

#### 8. Add New Guest
- **URL:** `POST /users/guests`
- **Description:** Adds a new guest to the user's guest list
- **Authentication:** Bearer Token
- **Request Body:**
```json
{
    "name": "Jane Doe",
    "gender": "FEMALE",
    "age": 28
}
```

#### 9. Update Guest
- **URL:** `PUT /users/guests/{guestId}`
- **Description:** Updates an existing guest's information
- **Authentication:** Bearer Token
- **Request Body:**
```json
{
    "name": "Jane Updated",
    "gender": "FEMALE",
    "age": 29
}
```

#### 10. Delete Guest
- **URL:** `DELETE /users/guests/{guestId}`
- **Description:** Removes a guest from the user's guest list
- **Authentication:** Bearer Token

---

### 🏨 Hotel Browse Endpoints (Public)

#### 11. Search Hotels
- **URL:** `GET /hotels/search`
- **Description:** Search for available hotels based on filters
- **Authentication:** Bearer Token
- **Request Body:**
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

#### 12. Get Hotel Information
- **URL:** `GET /hotels/{hotelId}/info`
- **Description:** Retrieves detailed information about a specific hotel
- **Authentication:** Bearer Token

---

### 🏢 Hotel Admin Management Endpoints

> **Note:** These endpoints require admin/hotel owner authentication

#### 13. Create New Hotel
- **URL:** `POST /admin/hotels`
- **Description:** Creates a new hotel (for hotel owners)
- **Authentication:** Bearer Token
- **Request Body:**
```json
{
    "name": "Grand Hotel",
    "city": "New York",
    "photos": ["https://example.com/photo1.jpg", "https://example.com/photo2.jpg"],
    "amenities": ["WiFi", "Pool", "Gym", "Spa", "Restaurant"],
    "contactInfo": {
        "address": "123 Main Street, New York, NY 10001",
        "phoneNumber": "+1-555-123-4567",
        "email": "info@grandhotel.com",
        "location": "40.7128,-74.0060"
    }
}
```

#### 14. Get Hotel by ID (Admin)
- **URL:** `GET /admin/hotels/{hotelId}`
- **Description:** Fetch details of a specific hotel
- **Authentication:** Bearer Token

#### 15. Update Hotel
- **URL:** `PUT /admin/hotels/{hotelId}`
- **Description:** Updates hotel information
- **Authentication:** Bearer Token
- **Request Body:**
```json
{
    "name": "Grand Hotel Updated",
    "city": "New York",
    "photos": ["https://example.com/photo1.jpg"],
    "amenities": ["WiFi", "Pool", "Gym"],
    "contactInfo": {
        "address": "123 Main Street, New York, NY 10001",
        "phoneNumber": "+1-555-123-4567",
        "email": "info@grandhotel.com",
        "location": "40.7128,-74.0060"
    }
}
```

#### 16. Delete Hotel
- **URL:** `DELETE /admin/hotels/{hotelId}`
- **Description:** Deletes a hotel from the system
- **Authentication:** Bearer Token

#### 17. Activate Hotel
- **URL:** `PATCH /admin/hotels/{hotelId}/activate`
- **Description:** Marks a hotel as active
- **Authentication:** Bearer Token

#### 18. Get All Hotels (Admin)
- **URL:** `GET /admin/hotels`
- **Description:** Retrieves all hotels owned by the admin
- **Authentication:** Bearer Token

#### 19. Get Hotel Bookings
- **URL:** `GET /admin/hotels/{hotelId}/bookings`
- **Description:** Gets all bookings for a specific hotel
- **Authentication:** Bearer Token

#### 20. Get Hotel Report
- **URL:** `GET /admin/hotels/{hotelId}/reports`
- **Description:** Gets a report for a specific hotel
- **Authentication:** Bearer Token
- **Query Parameters:**
  - `startDate` (optional): Start date for report (format: YYYY-MM-DD)
  - `endDate` (optional): End date for report (format: YYYY-MM-DD)
- **Example:** `GET /admin/hotels/1/reports?startDate=2025-01-01&endDate=2025-01-31`

---

### 🛏️ Room Admin Management Endpoints

#### 21. Create New Room
- **URL:** `POST /admin/hotels/{hotelId}/rooms`
- **Description:** Adds a new room to a hotel
- **Authentication:** Bearer Token
- **Request Body:**
```json
{
    "type": "Deluxe Suite",
    "basePrice": 299.99,
    "photos": ["https://example.com/room1.jpg", "https://example.com/room2.jpg"],
    "amenities": ["King Bed", "Ocean View", "Mini Bar", "Room Service"],
    "totalCount": 10,
    "capacity": 2
}
```

#### 22. Get All Rooms in Hotel
- **URL:** `GET /admin/hotels/{hotelId}/rooms`
- **Description:** Retrieves all rooms in a specific hotel
- **Authentication:** Bearer Token

#### 23. Update Room
- **URL:** `PUT /admin/hotels/{hotelId}/rooms/{roomId}`
- **Description:** Updates room information
- **Authentication:** Bearer Token
- **Request Body:**
```json
{
    "type": "Premium Suite",
    "basePrice": 399.99,
    "photos": ["https://example.com/room1.jpg"],
    "amenities": ["King Bed", "Ocean View", "Mini Bar"],
    "totalCount": 8,
    "capacity": 3
}
```

#### 24. Delete Room
- **URL:** `DELETE /admin/hotels/{hotelId}/rooms/{roomId}`
- **Description:** Deletes a room from the hotel
- **Authentication:** Bearer Token

---

### 📦 Inventory Management Endpoints

#### 25. Get Room Inventory
- **URL:** `GET /admin/inventory/rooms/{roomId}`
- **Description:** Fetches inventory details for a specific room
- **Authentication:** Bearer Token

#### 26. Update Room Inventory
- **URL:** `PATCH /admin/inventory/rooms/{roomId}`
- **Description:** Updates inventory for a room (surge pricing, availability)
- **Authentication:** Bearer Token
- **Request Body:**
```json
{
    "startDate": "2025-06-01",
    "endDate": "2025-06-30",
    "surgeFactor": 1.5,
    "closed": false
}
```

---

### 📅 Booking Endpoints

#### 27. Initialize Booking
- **URL:** `POST /booking/init`
- **Description:** Starts a new booking process
- **Authentication:** Bearer Token
- **Request Body:**
```json
{
    "hotelId": 1,
    "roomId": 1,
    "checkInDate": "2025-06-01",
    "checkOutDate": "2025-06-05",
    "roomsCount": 1
}
```

#### 28. Add Guests to Booking
- **URL:** `POST /booking/{bookingId}/addGuests`
- **Description:** Adds guests to an existing booking
- **Authentication:** Bearer Token
- **Request Body:**
```json
[
    {
        "name": "John Doe",
        "gender": "MALE",
        "age": 30
    },
    {
        "name": "Jane Doe",
        "gender": "FEMALE",
        "age": 28
    }
]
```

#### 29. Initiate Payment
- **URL:** `POST /booking/{bookingId}/payments`
- **Description:** Initiates Stripe payment for the booking
- **Authentication:** Bearer Token

#### 30. Cancel Booking
- **URL:** `POST /booking/{bookingId}/cancel`
- **Description:** Cancels an existing booking
- **Authentication:** Bearer Token

#### 31. Get Booking Status
- **URL:** `GET /booking/{bookingId}/status`
- **Description:** Gets the current status of a booking
- **Authentication:** Bearer Token

---

### 💳 Webhook Endpoint

#### 32. Stripe Payment Webhook
- **URL:** `POST /webhook/payment`
- **Description:** Handles Stripe payment webhook events
- **Headers Required:**
  - `Stripe-Signature`: Stripe signature header
- **Note:** This endpoint is called by Stripe, not directly by users

---

## Setting Up Postman

### 1. Create Environment Variables

Create a Postman environment with the following variables:

| Variable | Initial Value | Description |
|----------|---------------|-------------|
| `base_url` | `http://localhost:8080/api/v1` | Base URL for API |
| `access_token` | (empty) | JWT token after login |

### 2. Configure Authentication

For authenticated endpoints, add this header:
```
Authorization: Bearer {{access_token}}
```

Or use Postman's "Authorization" tab:
- Type: Bearer Token
- Token: `{{access_token}}`

### 3. Auto-save Token on Login

Add this script to the "Tests" tab of your Login request:
```javascript
if (pm.response.code === 200) {
    var jsonData = pm.response.json();
    pm.environment.set("access_token", jsonData.data.accessToken);
}
```

---

## Sample Test Flow

1. **Sign Up** → Create a new user account
2. **Login** → Get JWT access token
3. **Create Hotel** → Create a new hotel (admin)
4. **Create Room** → Add rooms to the hotel
5. **Activate Hotel** → Make the hotel available
6. **Search Hotels** → Search for available hotels
7. **Initialize Booking** → Start a booking
8. **Add Guests** → Add guest information
9. **Initiate Payment** → Process payment
10. **Get Booking Status** → Check booking confirmation

---

## HTTP Status Codes

| Code | Meaning |
|------|---------|
| 200 | Success |
| 201 | Created |
| 204 | No Content (Success without response body) |
| 400 | Bad Request |
| 401 | Unauthorized |
| 403 | Forbidden |
| 404 | Not Found |
| 500 | Internal Server Error |

---

## Technology Stack

- **Framework:** Spring Boot 3.4.2
- **Language:** Java 23
- **Database:** MySQL 8.0
- **Authentication:** JWT (JSON Web Tokens)
- **Payment:** Stripe
- **Documentation:** SpringDoc OpenAPI (Swagger)
- **ORM:** Spring Data JPA / Hibernate