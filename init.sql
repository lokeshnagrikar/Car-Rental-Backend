-- Enable strict SQL mode
SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";

-- Create users table
CREATE TABLE IF NOT EXISTS `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `email` varchar(50) NOT NULL,
  `password` varchar(120) NOT NULL,
  `phone` varchar(15) DEFAULT NULL,
  `date_of_birth` date DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `driving_license` varchar(50) DEFAULT NULL,
  `profile_picture` varchar(255) DEFAULT NULL,
  `role` enum('CUSTOMER','ADMIN') NOT NULL DEFAULT 'CUSTOMER',
  `reset_token` varchar(255) DEFAULT NULL,
  `enabled` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Create cars table
CREATE TABLE IF NOT EXISTS `cars` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `make` varchar(100) NOT NULL,
  `model` varchar(100) NOT NULL,
  `year` int NOT NULL,
  `price_per_day` decimal(10,2) NOT NULL,
  `available` tinyint(1) NOT NULL DEFAULT '1',
  `image_url` varchar(255) DEFAULT NULL,
  `license_plate` varchar(20) DEFAULT NULL,
  `color` varchar(50) DEFAULT NULL,
  `transmission` varchar(50) DEFAULT NULL,
  `seats` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Create bookings table
CREATE TABLE IF NOT EXISTS `bookings` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `car_id` bigint NOT NULL,
  `start_date` date NOT NULL,
  `end_date` date NOT NULL,
  `total_price` decimal(10,2) NOT NULL,
  `status` varchar(20) NOT NULL DEFAULT 'PENDING',
  `pickup_location` varchar(255) NOT NULL,
  `drop_off_location` varchar(255) NOT NULL,
  `created_at` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_car_id` (`car_id`),
  CONSTRAINT `fk_booking_car` FOREIGN KEY (`car_id`) REFERENCES `cars` (`id`),
  CONSTRAINT `fk_booking_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Create payments table
CREATE TABLE IF NOT EXISTS `payments` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `booking_id` bigint NOT NULL,
  `amount` decimal(10,2) NOT NULL,
  `payment_status` varchar(20) NOT NULL DEFAULT 'PENDING',
  `transaction_id` varchar(255) DEFAULT NULL,
  `payment_date` datetime DEFAULT NULL,
  `payment_method` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_booking_id` (`booking_id`),
  CONSTRAINT `fk_payment_booking` FOREIGN KEY (`booking_id`) REFERENCES `bookings` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Create refresh_tokens table for JWT
CREATE TABLE IF NOT EXISTS `refresh_tokens` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `token` varchar(255) NOT NULL,
  `expiry_date` datetime NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_token` (`token`),
  KEY `idx_user_id` (`user_id`),
  CONSTRAINT `fk_refresh_token_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Insert default admin user (password: admin123 - hashed)
INSERT INTO `users` (`name`, `email`, `password`, `role`, `enabled`)
VALUES ('Admin User', 'admin@carrental.com', '$2a$10$XptfskLsT1SL/bOzZLkNo.RaPkjVS7j2rWvG9wfI5gCvcr4qv7DzW', 'ADMIN', 1)
ON DUPLICATE KEY UPDATE `email` = VALUES(`email`);

-- Insert sample cars
INSERT INTO `cars` (`make`, `model`, `year`, `price_per_day`, `available`, `image_url`, `license_plate`, `color`, `transmission`, `seats`) VALUES
('Toyota', 'Camry', 2022, 50.00, 1, '/images/camry.jpg', 'ABC123', 'White', 'Automatic', 5),
('Honda', 'Civic', 2023, 45.00, 1, '/images/civic.jpg', 'XYZ789', 'Black', 'Automatic', 5),
('BMW', 'X5', 2022, 100.00, 1, '/images/x5.jpg', 'BMW001', 'Blue', 'Automatic', 5),
('Mercedes', 'C-Class', 2023, 90.00, 1, '/images/cclass.jpg', 'MERC001', 'Silver', 'Automatic', 5),
('Ford', 'Mustang', 2023, 120.00, 1, '/images/mustang.jpg', 'MUS001', 'Red', 'Manual', 4)
ON DUPLICATE KEY UPDATE `make` = VALUES(`make`), `model` = VALUES(`model`);

COMMIT;