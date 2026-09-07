# ✈️ Flight Booking System

<div align="center">

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.4-brightgreen?logo=springboot)
![React](https://img.shields.io/badge/React-19.1.1-blue?logo=react)
![MySQL](https://img.shields.io/badge/MySQL-8.0-orange?logo=mysql)
![JWT](https://img.shields.io/badge/Auth-JWT-yellow?logo=jsonwebtokens)
![Java](https://img.shields.io/badge/Java-17-red?logo=openjdk)
![License](https://img.shields.io/badge/License-MIT-lightgrey)

A full-stack flight booking platform with secure authentication, real-time seat management, payment integration, and email OTP verification.

</div>

---

## 📑 Table of Contents

- [Features](#-features)
- [Tech Stack](#-tech-stack)
- [Project Structure](#-project-structure)
- [Prerequisites](#-prerequisites)
- [Setup & Installation](#-setup--installation)
- [API Reference](#-api-reference)
- [Admin Setup](#-admin-setup)
- [Default Credentials](#-default-credentials)
- [Sample Flights](#-sample-flights)
- [Security](#-security)
- [Future Enhancements](#-future-enhancements)

---

## ✨ Features

<details>
<summary><b>👤 User Features</b></summary>

- Register & login with JWT-based authentication
- Email OTP verification on registration
- Search flights by source, destination, date, and airline
- Book flights with passenger details
- View booking history
- Cancel bookings with refund
- Download PDF tickets

</details>

<details>
<summary><b>🛠️ Admin Features</b></summary>

- Add, update, and delete flights
- View all customer bookings
- Manage flight inventory and seat availability
- Monitor flight status

</details>

<details>
<summary><b>💳 Payment Features</b></summary>

- Razorpay payment gateway integration
- Payment status tracking per booking
- Refund on cancellation

</details>

---

## 🧰 Tech Stack

| Layer | Technology |
|---|---|
| Backend | Spring Boot 3.5.4, Spring Data JPA, Spring Security |
| Auth | JWT (jjwt 0.11.5), BCrypt |
| Database | MySQL 8.0 |
| Frontend | React 19.1.1, React Router 7.7.1 |
| Styling | Bootstrap 5.3.7 |
| HTTP Client | Axios 1.11.0 |
| Payment | Razorpay Java SDK 1.4.3 |
| PDF | OpenPDF 1.3.8 |
| Email | Spring Boot Mail |
| Build | Maven, npm |

---

## 📁 Project Structure

```
Project/
├── Backend/
│   └── FlightReservation/
│       └── src/main/java/com/FlightReservationSystem/
│           ├── controller/
│           │   ├── AdminController.java
│           │   ├── AuthController.java
│           │   ├── BookingController.java
│           │   ├── FlightController.java
│           │   └── PaymentController.java
│           ├── dto/
│           │   ├── BookingRequest.java
│           │   ├── EmailOtpRequest.java
│           │   ├── FlightSearchRequest.java
│           │   ├── LoginRequest.java
│           │   ├── PassengerInfo.java
│           │   ├── PaymentRequest.java
│           │   └── RegisterRequest.java
│           ├── entity/
│           │   ├── Booking.java
│           │   ├── Flight.java
│           │   └── User.java
│           ├── exception/
│           │   ├── GlobalExceptionHandler.java
│           │   └── [Custom Exceptions...]
│           ├── repository/
│           │   ├── BookingRepository.java
│           │   ├── FlightRepository.java
│           │   └── UserRepository.java
│           ├── security/
│           │   ├── JwtAuthenticationFilter.java
│           │   ├── JwtUtil.java
│           │   └── SecurityConfig.java
│           ├── service/
│           │   ├── AuthService.java
│           │   ├── BookingService.java
│           │   ├── BookingCleanupService.java
│           │   ├── EmailService.java
│           │   ├── FlightService.java
│           │   ├── FlightStatusService.java
│           │   ├── OtpService.java
│           │   ├── PaymentService.java
│           │   └── TicketService.java
│           └── FlightReservationApplication.java
├── Frontend/
│   └── flight-booking-frontend/
│       └── src/
│           ├── components/
│           │   ├── Landing.js
│           │   ├── Login.js
│           │   ├── Register.js
│           │   ├── Navbar.js
│           │   ├── FlightSearch.js
│           │   ├── PassengerForm.js
│           │   ├── BookingHistory.js
│           │   ├── AdminDashboard.js
│           │   ├── EmailVerification.js
│           │   ├── EmailOtpVerification.js
│           │   ├── AboutUs.js
│           │   └── ContactUs.js
│           ├── context/
│           │   └── AuthContext.js
│           ├── services/
│           │   └── api.js
│           └── styles/
│               └── animations.css
├── database_setup.sql
└── README.md
```

---

## ✅ Prerequisites

- Java 17+
- Node.js 16+ & npm
- MySQL 8.0+
- Maven 3.6+

---

## 🚀 Setup & Installation

<details>
<summary><b>1. Database Setup</b></summary>

```sql
CREATE DATABASE IF NOT EXISTS flight_booking_db;
```

Update credentials in `Backend/FlightReservation/src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/flight_booking_db
spring.datasource.username=your_username
spring.datasource.password=your_password
```

Run the setup script to seed admin user and sample flights:

```bash
mysql -u your_username -p flight_booking_db < database_setup.sql
```

</details>

<details>
<summary><b>2. Backend Setup</b></summary>

```bash
cd Backend/FlightReservation
mvn clean install
mvn spring-boot:run
```

Backend runs at → `http://localhost:8080`

</details>

<details>
<summary><b>3. Frontend Setup</b></summary>

```bash
cd Frontend/flight-booking-frontend
npm install
npm start
```

Frontend runs at → `http://localhost:3000`

</details>

---

## 📡 API Reference

<details>
<summary><b>🔐 Authentication</b></summary>

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | `/api/auth/register` | Register new customer | No |
| POST | `/api/auth/login` | Login & get JWT token | No |
| POST | `/api/auth/verify-email-otp` | Verify email OTP after registration | No |
| POST | `/api/auth/resend-email-otp` | Resend OTP to email | No |
| POST | `/api/auth/validate-email` | Check if email is a valid Gmail | No |
| POST | `/api/auth/register-admin` | Register a new admin user | No ⚠️ |

> ⚠️ `/api/auth/register-admin` is currently unprotected. See [Admin Setup](#-admin-setup) for details.

</details>

<details>
<summary><b>✈️ Flights</b></summary>

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/flights` | Get all flights |
| POST | `/api/flights/search` | Search flights |
| GET | `/api/flights/{id}` | Get flight by ID |

</details>

<details>
<summary><b>📋 Bookings</b></summary>

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/bookings` | Book a flight |
| GET | `/api/bookings/my-bookings` | Get user's bookings |
| PUT | `/api/bookings/{reference}/cancel` | Cancel a booking |

</details>

<details>
<summary><b>💳 Payments</b></summary>

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/payments/create-order` | Create Razorpay order |
| POST | `/api/payments/verify` | Verify payment |

</details>

<details>
<summary><b>🛠️ Admin</b></summary>

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/admin/flights` | Add new flight |
| PUT | `/api/admin/flights/{id}` | Update flight |
| DELETE | `/api/admin/flights/{id}` | Delete flight |
| GET | `/api/admin/bookings` | View all bookings |

</details>

---

## 👑 Admin Setup

There are **3 ways** to create an admin account:

<details>
<summary><b>Option 1 — Pre-seeded via SQL (Default)</b></summary>

Running `database_setup.sql` automatically inserts a default admin:

| Field | Value |
|-------|-------|
| Username | `admin` |
| Password | `password` |
| Email | `admin@gmail.com` |

> ⚠️ Change this password immediately in production.

</details>

<details>
<summary><b>Option 2 — API Endpoint (No auth required currently)</b></summary>

Send a `POST` request to `/api/auth/register-admin` with the following body:

```json
{
  "username": "newadmin",
  "email": "newadmin@gmail.com",
  "password": "yourpassword",
  "firstName": "Admin",
  "lastName": "User",
  "phoneNumber": "+911234567890"
}
```

Differences from regular registration:
- Role is set to `ADMIN` automatically
- No email OTP verification required — account is active immediately
- Returns a JWT token in the response

> ⚠️ This endpoint is currently open to everyone (`permitAll`). In production, restrict it by updating `SecurityConfig.java`:
> ```java
> .requestMatchers("/api/auth/register-admin").hasRole("ADMIN")
> ```

</details>

<details>
<summary><b>Option 3 — Directly via Database</b></summary>

Promote any existing user to admin by running:

```sql
UPDATE users SET role = 'ADMIN' WHERE username = 'someuser';
```

</details>

---

## 🔑 Default Credentials

| Role | Username | Password |
|------|----------|----------|
| Admin | `admin` | `password` |
| Customer | Register via UI | — |

> ⚠️ Change the admin password after first login in production.

---

## 🛫 Sample Flights (Pre-seeded)

| Flight | Airline | Route | Price |
|--------|---------|-------|-------|
| AI101 | Air India | Delhi → Mumbai | ₹5,500 |
| SG202 | SpiceJet | Mumbai → Bangalore | ₹4,200 |
| 6E303 | IndiGo | Bangalore → Chennai | ₹3,800 |
| UK404 | Vistara | Chennai → Kolkata | ₹6,200 |
| AI505 | Air India | Kolkata → Delhi | ₹5,800 |
| SG606 | SpiceJet | Delhi → Goa | ₹4,500 |
| 6E707 | IndiGo | Goa → Pune | ₹3,200 |
| UK808 | Vistara | Pune → Hyderabad | ₹4,800 |

---

## 🔒 Security

- JWT-based stateless authentication
- BCrypt password hashing
- Role-based access control (`ADMIN` / `CUSTOMER`)
- Email OTP verification on registration
- CORS configured for frontend at `localhost:3000`
- Global exception handling with custom exceptions

---

## 🔮 Future Enhancements

- [ ] Seat selection UI
- [ ] Multi-city booking
- [ ] Real-time flight status updates
- [ ] Mobile responsive improvements
- [ ] SMS notifications
- [ ] Loyalty/rewards program
