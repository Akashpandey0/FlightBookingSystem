-- Create database
CREATE DATABASE IF NOT EXISTS flight_booking_db;
USE flight_booking_db;

-- Update existing bookings table to handle longer enum values
ALTER TABLE bookings MODIFY COLUMN status VARCHAR(20);
ALTER TABLE bookings MODIFY COLUMN payment_status VARCHAR(20);



-- Create admin user (run this after starting the application)
INSERT INTO users (username, email, password, first_name, last_name, phone_number, role, created_at) 
VALUES ('admin', 'admin@gmail.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Admin', 'User', '+911234567890', 'ADMIN', NOW())
ON DUPLICATE KEY UPDATE username=username;

-- Sample flights data
INSERT INTO flights (flight_number, airline, source, source_airport, destination, destination_airport, departure_time, arrival_time, price, total_seats, available_seats, status, created_at) VALUES
('AI101', 'Air India', 'Delhi', 'DEL - Indira Gandhi International', 'Mumbai', 'BOM - Chhatrapati Shivaji', '2024-12-25 08:00:00', '2024-12-25 10:30:00', 5500.00, 180, 180, 'SCHEDULED', NOW()),
('SG202', 'SpiceJet', 'Mumbai', 'BOM - Chhatrapati Shivaji', 'Bangalore', 'BLR - Kempegowda International', '2024-12-25 14:00:00', '2024-12-25 16:00:00', 4200.00, 150, 150, 'SCHEDULED', NOW()),
('6E303', 'IndiGo', 'Bangalore', 'BLR - Kempegowda International', 'Chennai', 'MAA - Chennai International', '2024-12-26 09:30:00', '2024-12-26 11:00:00', 3800.00, 180, 180, 'SCHEDULED', NOW()),
('UK404', 'Vistara', 'Chennai', 'MAA - Chennai International', 'Kolkata', 'CCU - Netaji Subhas Chandra Bose', '2024-12-26 16:45:00', '2024-12-26 19:15:00', 6200.00, 160, 160, 'SCHEDULED', NOW()),
('AI505', 'Air India', 'Kolkata', 'CCU - Netaji Subhas Chandra Bose', 'Delhi', 'DEL - Indira Gandhi International', '2024-12-27 11:20:00', '2024-12-27 13:45:00', 5800.00, 180, 180, 'SCHEDULED', NOW()),
('SG606', 'SpiceJet', 'Delhi', 'DEL - Indira Gandhi International', 'Goa', 'GOI - Goa International', '2024-12-27 07:15:00', '2024-12-27 09:45:00', 4500.00, 150, 150, 'SCHEDULED', NOW()),
('6E707', 'IndiGo', 'Goa', 'GOI - Goa International', 'Pune', 'PNQ - Pune Airport', '2024-12-28 12:30:00', '2024-12-28 14:00:00', 3200.00, 180, 180, 'SCHEDULED', NOW()),
('UK808', 'Vistara', 'Pune', 'PNQ - Pune Airport', 'Hyderabad', 'HYD - Rajiv Gandhi International', '2024-12-28 18:00:00', '2024-12-28 19:30:00', 4800.00, 160, 160, 'SCHEDULED', NOW())
ON DUPLICATE KEY UPDATE flight_number=flight_number;