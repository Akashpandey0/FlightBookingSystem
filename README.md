# Flight Booking System

A comprehensive flight booking website built with Spring Boot backend and React frontend.

## Features

### User Features
- User registration and authentication
- Search flights by source, destination, date, and airline
- View flight availability
- Book flights with passenger details
- View booking history
- Cancel bookings with refund
- JWT-based secure authentication

### Admin Features
- Add new flights
- Update existing flights
- Delete flights
- View all customer bookings
- Manage flight inventory

## Technology Stack

### Backend
- Spring Boot 3.5.4
- Spring Data JPA
- Spring Security + JWT
- MySQL Database
- Lombok
- SLF4J Logger
- Maven

### Frontend
- React.js
- Bootstrap for styling
- Axios for API calls
- React Router for navigation

## Setup Instructions

### Prerequisites
- Java 17 or higher
- Node.js 16 or higher
- MySQL 8.0 or higher
- Maven 3.6 or higher

### Database Setup
1. Install MySQL and create a database:
```sql
CREATE DATABASE flight_booking_db;
```

2. Update database credentials in `Backend/FlightReservation/src/main/resources/application.properties`:
```properties
spring.datasource.username=your_username
spring.datasource.password=your_password
```

3. Run the database setup script:
```bash
mysql -u your_username -p flight_booking_db < database_setup.sql
```

### Backend Setup
1. Navigate to the backend directory:
```bash
cd Backend/FlightReservation
```

2. Install dependencies and run:
```bash
mvn clean install
mvn spring-boot:run
```

The backend will start on `http://localhost:8080`

### Frontend Setup
1. Navigate to the frontend directory:
```bash
cd Frontend/flight-booking-frontend
```

2. Install dependencies:
```bash
npm install
```

3. Start the development server:
```bash
npm start
```

The frontend will start on `http://localhost:3000`

## Default Credentials

### Admin Login
- Username: `admin`
- Password: `password`

### Test User
You can register a new user or create one manually in the database.

## API Endpoints

### Authentication
- `POST /api/auth/register` - User registration
- `POST /api/auth/login` - User login

### Flights
- `GET /api/flights` - Get all flights
- `POST /api/flights/search` - Search flights
- `GET /api/flights/{id}` - Get flight by ID

### Bookings
- `POST /api/bookings` - Book a flight
- `GET /api/bookings/my-bookings` - Get user bookings
- `PUT /api/bookings/{reference}/cancel` - Cancel booking

### Admin
- `POST /api/admin/flights` - Add flight
- `PUT /api/admin/flights/{id}` - Update flight
- `DELETE /api/admin/flights/{id}` - Delete flight
- `GET /api/admin/bookings` - Get all bookings

## Project Structure

```
Project/
├── Backend/
│   └── FlightReservation/
│       ├── src/main/java/com/FlightReservationSystem/
│       │   ├── entity/          # JPA entities
│       │   ├── repository/      # Data repositories
│       │   ├── service/         # Business logic
│       │   ├── controller/      # REST controllers
│       │   ├── dto/            # Data transfer objects
│       │   └── security/       # Security configuration
│       └── src/main/resources/
│           └── application.properties
├── Frontend/
│   └── flight-booking-frontend/
│       ├── src/
│       │   ├── components/     # React components
│       │   ├── context/       # Context providers
│       │   └── services/      # API services
│       └── public/
└── database_setup.sql
```

## Usage

1. Start both backend and frontend servers
2. Open `http://localhost:3000` in your browser
3. Register a new account or login with existing credentials
4. Search for flights using the search form
5. Book flights and manage your bookings
6. Admin users can access the admin dashboard to manage flights

## Security Features

- JWT-based authentication
- Password encryption using BCrypt
- Role-based access control (ADMIN/CUSTOMER)
- CORS configuration for frontend integration
- Input validation and error handling

## Future Enhancements

- Payment gateway integration
- Email notifications
- Flight status updates
- Seat selection
- Multi-city booking
- Mobile responsive design improvements