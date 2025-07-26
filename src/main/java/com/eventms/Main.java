package com.eventms;

import com.eventms.model.Admin;
import com.eventms.model.Event;
import com.eventms.model.EventStatus;
import com.eventms.model.Registration;
import com.eventms.model.RegistrationStatus;
import com.eventms.model.Role;
import com.eventms.model.User;
import com.eventms.service.EventService;
import com.eventms.service.UserService;

public class Main {
    public static void main(String[] args) {
        UserService userService = new UserService();
        EventService eventService = new EventService();

        System.out.println("=== Testing User Validation ===");
        try {
            // Test 1: Valid user creation
            User user = userService.createUser("Bob Smith", "bob@email.com", "987654321", "password123", "EN", "Manager", "1995-05-05", 'M', null);
            System.out.println("✓ Valid user created: " + user.getFullName());

            // Test invalid email
            try {
                user.setEmail("invalid-email");
                System.out.println("✗ Should have failed: Invalid email validation");
            } catch (IllegalArgumentException e) {
                System.out.println("✓ Email validation works: " + e.getMessage());
            }

            // Test invalid password
            try {
                user.setPassword("short");
                System.out.println("✗ Should have failed: Invalid password validation");
            } catch (IllegalArgumentException e) {
                System.out.println("✓ Password validation works: " + e.getMessage());
            }

            // Test invalid contact number
            try {
                user.setContactNumber("abc123");
                System.out.println("✗ Should have failed: Invalid contact number validation");
            } catch (IllegalArgumentException e) {
                System.out.println("✓ Contact number validation works: " + e.getMessage());
            }

            // Test invalid date format
            try {
                user.setDob("1995/05/05");
                System.out.println("✗ Should have failed: Invalid date format validation");
            } catch (IllegalArgumentException e) {
                System.out.println("✓ Date format validation works: " + e.getMessage());
            }
        } catch (Exception e) {
            System.out.println("✗ User creation failed: " + e.getMessage());
        }

        System.out.println("\n=== Testing Admin Validation ===");
        try {
            // Create admin with different levels
            Admin standardAdmin = new Admin(1, "Standard Admin", "standard@email.com", "111111111", "adminpass", "Administrator", "1990-01-01", 'M');
            Admin seniorAdmin = new Admin(2, "Senior Admin", "senior@email.com", "222222222", "adminpass", "Administrator", "1990-01-01", 'M');
            
            // Corrected promotion calls
            User regularUser = userService.createUser("Regular User", "regular@email.com", "333333333", "userpass", "EN", "Employee", "1995-05-05", 'M', null);
            
            try {
                standardAdmin.promoteUserToAdmin(regularUser, Role.ADMIN_STANDARD);
                System.out.println("✗ Should have failed: Standard admin shouldn't be able to promote");
            } catch (SecurityException e) {
                System.out.println("✓ Promotion permission validation works: " + e.getMessage());
            }

            seniorAdmin.promoteUserToAdmin(regularUser, Role.ADMIN_STANDARD);
            System.out.println("✓ Senior admin successfully promoted user");

            System.out.println("\n=== Testing Event Validation ===");
            try {
                // Corrected Event constructor
                Event event = new Event(
                    1,
                    "Tech Conference",
                    "Annual technology conference",
                    "Convention Center",
                    "2025-08-01T09:00",
                    "2025-08-01T17:00",
                    100,
                    EventStatus.OPEN,
                    "Conference",
                    seniorAdmin.getId()
                );
                System.out.println("✓ Valid event created: " + event.getTitle());

                // Test invalid date format
                try {
                    event.setStartTime("2025/08/01 09:00");
                    System.out.println("✗ Should have failed: Invalid start time format");
                } catch (IllegalArgumentException e) {
                    System.out.println("✓ Start time validation works: " + e.getMessage());
                }

                // Test end time before start time
                try {
                    event.setEndTime("2025-08-01T08:00");
                    System.out.println("✗ Should have failed: End time before start time");
                } catch (IllegalArgumentException e) {
                    System.out.println("✓ End time validation works: " + e.getMessage());
                }

                // Test negative capacity
                try {
                    event.setCapacity(-10);
                    System.out.println("✗ Should have failed: Negative capacity");
                } catch (IllegalArgumentException e) {
                    System.out.println("✓ Capacity validation works: " + e.getMessage());
                }

                System.out.println("\n=== Testing Registration Validation ===");
                // Create a new registration
                Registration registration = new Registration(regularUser.getId(), event.getEventId(), "2025-07-19");
                System.out.println("✓ Valid registration created");

                // Test invalid registration date format
                try {
                    registration.setRegistrationDate("2025/07/19");
                    System.out.println("✗ Should have failed: Invalid registration date format");
                } catch (IllegalArgumentException e) {
                    System.out.println("✓ Registration date validation works: " + e.getMessage());
                }

                // Test invalid checkin time format
                try {
                    registration.setCheckinTime("2025/08/01 09:00");
                    System.out.println("✗ Should have failed: Invalid checkin time format");
                } catch (IllegalArgumentException e) {
                    System.out.println("✓ Checkin time validation works: " + e.getMessage());
                }

                // Test valid updates
                registration.setCheckinTime("2025-08-01T09:30");
                registration.setStatus(RegistrationStatus.CONFIRMED);
                System.out.println("✓ Registration successfully updated");

                // Final test: Create another event using senior admin
                Event finalEvent = eventService.createEvent(
                    "Admin Conference",
                    "A conference for admins.",
                    "Main Hall",
                    "2025-08-01T09:00",
                    "2025-08-01T17:00",
                    100,
                    "Manager",
                    "Conference",
                    "EN",
                    seniorAdmin  // Use the senior admin object we created earlier
                );
                System.out.println("✓ Final event created: " + finalEvent.getTitle() + ", ID: " + finalEvent.getId() + ", Status: " + finalEvent.getEventStatus());

            } catch (Exception e) {
                System.out.println("✗ Event/Registration tests failed: " + e.getMessage());
            }

        } catch (Exception e) {
            System.out.println("✗ Admin tests failed: " + e.getMessage());
        }

        System.out.println("\n=== Comprehensive Testing Script ===");

        try {
            // Test User creation and validation
            User user = new User(1, "Alice Johnson", "alice@example.com", "1234567890", "Password1!", "Engineer", "1990-01-01", 'F', Role.USER);
            System.out.println("✓ User created successfully: " + user);

            // Test invalid email
            try {
                user.setEmail("invalid-email");
            } catch (IllegalArgumentException e) {
                System.out.println("✓ Email validation works: " + e.getMessage());
            }

            // Test invalid password
            try {
                user.setPassword("short");
            } catch (IllegalArgumentException e) {
                System.out.println("✓ Password validation works: " + e.getMessage());
            }

            // Test Admin creation and promotion
            Admin admin = new Admin(2, "Bob Admin", "bob@admin.com", "9876543210", "AdminPass1!", "Manager", "1985-05-15", 'M');
            System.out.println("✓ Admin created successfully: " + admin);

            // Promote user to admin
            try {
                admin.promoteUserToAdmin(user, Role.ADMIN_STANDARD);
                System.out.println("✓ User promoted to admin successfully");
            } catch (Exception e) {
                System.out.println("✗ Promotion failed: " + e.getMessage());
            }

            // Test Event creation
            Event event = new Event(1, "Tech Summit", "A summit for tech enthusiasts", "Tech Hall", "2025-08-01T09:00", "2025-08-01T17:00", 200, EventStatus.OPEN, "Conference", admin.getId());
            System.out.println("✓ Event created successfully: " + event);

            // // Test invalid event capacity
            // try {
            //     event.setCapacity(-50);
            // } catch (IllegalArgumentException e) {
            //     System.out.println("✓ Event capacity validation works: " + e.getMessage());
            // }

            // Test Registration
            Registration registration = new Registration(user.getId(), event.getEventId(), "2025-07-20");
            System.out.println("✓ Registration created successfully: " + registration);

            // Test invalid registration date
            try {
                registration.setRegistrationDate("2025/07/20");
            } catch (IllegalArgumentException e) {
                System.out.println("✓ Registration date validation works: " + e.getMessage());
            }

        } catch (Exception e) {
            System.out.println("✗ Comprehensive testing failed: " + e.getMessage());
        }

        System.out.println("\n=== Testing promoteUserToAdmin ===");
        try {
            // Create a user and an admin
            User testUser = userService.createUser("Test User", "testuser@email.com", "444444444", "userpassword", "EN", "Employee", "1990-01-01", 'F', null);
            Admin testAdmin = new Admin(3, "Test Admin", "testadmin@email.com", "555555555", "adminpassword", "Administrator", "1980-01-01", 'M');

            System.out.println("Promoter Role: " + testAdmin.getRole() + " (Level: " + testAdmin.getRole().getAccessLevel() + ")");
            System.out.println("Target Role: " + Role.ADMIN_STANDARD + " (Level: " + Role.ADMIN_STANDARD.getAccessLevel() + ")");
            System.out.println("User's Current Role: " + (testUser.getRole() != null ? testUser.getRole() : "None"));

            // Attempt to promote the user
            testAdmin.promoteUserToAdmin(testUser, Role.ADMIN_STANDARD);
            System.out.println("✓ User successfully promoted to: " + testUser.getRole());
        } catch (Exception e) {
            System.out.println("✗ Promotion test failed: " + e.getMessage());
        }
    }
}