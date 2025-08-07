-- =====================================================
-- Event Management System Database Script
-- =====================================================

-- Drop and create database
DROP DATABASE IF EXISTS eventManagement;
CREATE DATABASE eventManagement;
USE eventManagement;

-- =====================================================
-- TABLE CREATION
-- =====================================================

-- Independent table: Users (no foreign keys)
CREATE TABLE Users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    full_name VARCHAR(50) NOT NULL,
    email VARCHAR(50) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    u_role ENUM('admin', 'user') DEFAULT 'user',
    is_verified TINYINT(1) DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    -- Constraints
    CONSTRAINT chk_full_name_no_numbers CHECK (full_name REGEXP '^[^0-9]+$'),
    CONSTRAINT chk_email_format CHECK (email REGEXP '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$'),
    CONSTRAINT chk_password CHECK (CHAR_LENGTH(password_hash) >= 8)
);

-- Dependent table with one foreign key: Eventtokens
CREATE TABLE Eventtokens (
    token_id INT PRIMARY KEY AUTO_INCREMENT,
    token_string VARCHAR(255) NOT NULL UNIQUE,
    is_used TINYINT(1) DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by INT NOT NULL,
    use_count INT DEFAULT 0,
    
    -- Foreign key constraint
    FOREIGN KEY (created_by) REFERENCES Users(user_id) ON DELETE CASCADE
);

-- Dependent table with multiple foreign keys: Eventss
CREATE TABLE Eventss (
    event_id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(200) NOT NULL,
    e_description TEXT NOT NULL,
    location VARCHAR(100) NOT NULL,
    start_time DATETIME NOT NULL,
    end_time DATETIME NOT NULL,
    capacity INT NOT NULL,
    e_status ENUM('open', 'closed', 'cancelled', 'finished') DEFAULT 'open',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    organizer_id INT NOT NULL,
    token_id INT NOT NULL,
    
    -- Foreign key constraints
    FOREIGN KEY (organizer_id) REFERENCES Users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (token_id) REFERENCES Eventtokens(token_id) ON DELETE RESTRICT,
    
    -- Table constraints
    CONSTRAINT chk_start_before_end CHECK (start_time < end_time),
    CONSTRAINT chk_capacity_positive CHECK (capacity > 0),
    CONSTRAINT chk_title_length CHECK (CHAR_LENGTH(TRIM(title)) >= 5),
    CONSTRAINT chk_description_length CHECK (CHAR_LENGTH(TRIM(e_description)) >= 10)
);

-- Dependent table with multiple foreign keys: Registration
CREATE TABLE Registration (
    registration_id INT PRIMARY KEY AUTO_INCREMENT,
    registration_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    r_status ENUM('pending', 'confirmed', 'cancelled', 'attended') DEFAULT 'pending',
    checkin_time TIMESTAMP NULL DEFAULT NULL,
    event_id INT NOT NULL,
    user_id INT NOT NULL,
    
    -- Foreign key constraints
    FOREIGN KEY (event_id) REFERENCES Eventss(event_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE,
    
    -- Unique constraint to prevent duplicate registrations
    UNIQUE KEY unique_event_user (event_id, user_id)
);

-- =====================================================
-- DATA INSERTION
-- =====================================================

-- Insert data into Users table
INSERT INTO Users (full_name, email, password_hash, u_role, is_verified) VALUES
('John Smith', 'john.smith@example.com', 'hashedpassword123456', 'admin', 1),
('Alice Johnson', 'alice.johnson@example.com', 'securehash456789', 'user', 1),
('Bob Wilson', 'bob.wilson@example.com', 'passwordhash789012', 'user', 1),
('Emma Brown', 'emma.brown@example.com', 'hashpass101112345', 'admin', 1),
('Michael Davis', 'michael.davis@example.com', 'strongpass987654', 'user', 0),
('Sarah Miller', 'sarah.miller@example.com', 'safepassword123', 'user', 1),
('David Garcia', 'david.garcia@example.com', 'mypassword456789', 'admin', 1),
('Lisa Anderson', 'lisa.anderson@example.com', 'userpass123456789', 'user', 1);

-- Insert data into Eventtokens table
INSERT INTO Eventtokens (token_string, created_by, use_count) VALUES
('TOKEN123456789', 1, 0),
('TOKEN987654321', 1, 1),
('TOKEN555666777', 4, 0),
('TOKEN111222333', 7, 0),
('TOKEN444555666', 1, 2),
('TOKEN777888999', 4, 0),
('TOKEN101010101', 7, 0),
('TOKEN202020202', 1, 1),
('TOKEN303030303', 4, 0),
('TOKEN404040404', 7, 0),
('TOKEN505050505', 1, 3),
('TOKEN606060606', 4, 0);

-- Insert data into Eventss table
INSERT INTO Eventss (title, e_description, location, start_time, end_time, capacity, e_status, organizer_id, token_id) VALUES
('Tech Conference 2025', 'Annual technology conference featuring keynote speakers and workshops', 'Convention Center, New York', '2025-08-01 09:00:00', '2025-08-01 17:00:00', 500, 'open', 1, 1),
('Music Festival Summer', 'Outdoor music festival featuring local and international bands', 'City Park, California', '2025-09-15 12:00:00', '2025-09-15 22:00:00', 1000, 'open', 4, 3),
('Charity Marathon Run', '5K marathon run for charity support and community engagement', 'Downtown Stadium, Texas', '2025-10-10 07:00:00', '2025-10-10 12:00:00', 200, 'open', 1, 2),
('Business Networking Event', 'Professional networking event for entrepreneurs and business leaders', 'Hilton Hotel, Chicago', '2025-08-15 18:00:00', '2025-08-15 22:00:00', 150, 'open', 7, 4),
('Art Exhibition Opening', 'Contemporary art exhibition showcasing local artists', 'Modern Art Gallery, Miami', '2025-09-01 19:00:00', '2025-09-01 23:00:00', 100, 'open', 4, 5),
('Cooking Workshop Series', 'Hands-on cooking workshop featuring international cuisines', 'Culinary Institute, Seattle', '2025-08-20 10:00:00', '2025-08-20 16:00:00', 50, 'open', 1, 6),
('Science Fair Competition', 'Annual science fair for students and researchers', 'University Campus, Boston', '2025-09-10 09:00:00', '2025-09-10 18:00:00', 300, 'open', 7, 7),
('Film Festival Screening', 'Independent film festival featuring emerging filmmakers', 'Downtown Cinema, Portland', '2025-10-05 14:00:00', '2025-10-05 20:00:00', 80, 'open', 4, 8),
('Fitness Bootcamp Challenge', 'Intensive fitness bootcamp for all skill levels', 'Community Center, Denver', '2025-08-25 06:00:00', '2025-08-25 10:00:00', 75, 'open', 1, 9),
('Book Reading Club Meeting', 'Monthly book club meeting and discussion session', 'Public Library, Austin', '2025-09-20 15:00:00', '2025-09-20 17:00:00', 30, 'open', 7, 10),
('Photography Workshop', 'Professional photography techniques and portfolio building', 'Studio Space, San Francisco', '2025-10-15 11:00:00', '2025-10-15 17:00:00', 25, 'open', 4, 11),
('Wine Tasting Evening', 'Premium wine tasting event with sommelier guidance', 'Vineyard Estate, Napa Valley', '2025-11-01 17:00:00', '2025-11-01 21:00:00', 60, 'open', 1, 12),
('Digital Marketing Seminar', 'Advanced digital marketing strategies for businesses', 'Conference Room, Las Vegas', '2025-08-30 13:00:00', '2025-08-30 18:00:00', 120, 'open', 7, 1),
('Yoga Retreat Weekend', 'Relaxing yoga retreat with meditation sessions', 'Mountain Resort, Colorado', '2025-09-25 08:00:00', '2025-09-27 16:00:00', 40, 'open', 4, 2),
('Startup Pitch Competition', 'Entrepreneurship pitch competition for startup founders', 'Innovation Hub, Silicon Valley', '2025-10-20 10:00:00', '2025-10-20 19:00:00', 200, 'open', 1, 3),
('Gaming Tournament Finals', 'Esports gaming tournament championship finals', 'Gaming Arena, Los Angeles', '2025-11-05 12:00:00', '2025-11-05 20:00:00', 300, 'open', 7, 4),
('Fashion Show Premiere', 'High-end fashion show featuring designer collections', 'Fashion District, New York', '2025-10-25 19:00:00', '2025-10-25 22:00:00', 150, 'open', 4, 5),
('Environmental Summit', 'Climate change awareness and sustainability conference', 'Green Convention Center, Washington DC', '2025-11-10 09:00:00', '2025-11-10 17:00:00', 400, 'open', 1, 6),
('Dance Competition National', 'National dance competition featuring various dance styles', 'Performance Theater, Nashville', '2025-11-15 14:00:00', '2025-11-15 22:00:00', 250, 'open', 7, 7),
('Career Fair Job Expo', 'Job fair connecting employers with job seekers', 'Exhibition Hall, Phoenix', '2025-11-20 10:00:00', '2025-11-20 16:00:00', 500, 'open', 4, 8),
('Holiday Celebration Gala', 'Annual holiday celebration and charity fundraiser', 'Grand Ballroom, Orlando', '2025-12-15 18:00:00', '2025-12-15 23:00:00', 200, 'open', 1, 9);

-- Insert data into Registration table
INSERT INTO Registration (r_status, event_id, user_id, checkin_time) VALUES
('confirmed', 1, 2, NULL),
('pending', 1, 3, NULL),
('attended', 2, 2, '2025-09-15 12:30:00'),
('cancelled', 3, 3, NULL),
('confirmed', 2, 1, NULL),
('confirmed', 4, 5, NULL),
('attended', 5, 6, '2025-09-01 19:15:00'),
('pending', 6, 8, NULL),
('confirmed', 7, 2, NULL),
('attended', 8, 3, '2025-10-05 14:30:00'),
('confirmed', 9, 5, NULL),
('pending', 10, 6, NULL),
('confirmed', 11, 8, NULL),
('attended', 12, 1, '2025-11-01 17:20:00'),
('confirmed', 13, 2, NULL),
('pending', 14, 3, NULL),
('confirmed', 15, 5, NULL),
('attended', 16, 6, '2025-11-05 12:45:00'),
('confirmed', 17, 8, NULL),
('pending', 18, 1, NULL),
('confirmed', 19, 2, NULL),
('attended', 20, 3, '2025-11-20 10:30:00'),
('confirmed', 1, 5, NULL),
('pending', 2, 6, NULL),
('confirmed', 3, 8, NULL),
('attended', 4, 1, '2025-08-15 18:15:00'),
('confirmed', 5, 2, NULL),
('pending', 6, 3, NULL),
('confirmed', 7, 5, NULL),
('attended', 8, 6, '2025-10-05 15:00:00');

-- =====================================================
-- DISPLAY SUCCESS MESSAGE
-- =====================================================

SELECT 'Event Management Database created successfully!' AS Status;

-- Display table statistics
SELECT 
    'Users' AS TableName, 
    COUNT(*) AS RecordCount 
FROM Users
UNION ALL
SELECT 
    'Eventtokens' AS TableName, 
    COUNT(*) AS RecordCount 
FROM Eventtokens
UNION ALL
SELECT 
    'Eventss' AS TableName, 
    COUNT(*) AS RecordCount 
FROM Eventss
UNION ALL
SELECT 
    'Registration' AS TableName, 
    COUNT(*) AS RecordCount 
FROM Registration;