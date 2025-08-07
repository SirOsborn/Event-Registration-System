package com.eventms;

import com.eventms.model.Admin;
import com.eventms.model.Event;
import com.eventms.model.Registration;
import com.eventms.model.RegistrationStatus;
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
            
            // Test admin level validation
            try {
                standardAdmin.setAdminLevel("invalid_level");
                System.out.println("✗ Should have failed: Invalid admin level validation");
            } catch (IllegalArgumentException e) {
                System.out.println("✓ Admin level validation works: " + e.getMessage());
            }

            standardAdmin.setAdminLevel("standard");
            seniorAdmin.setAdminLevel("senior");
            System.out.println("✓ Valid admin levels set");

            // Test promotion permissions
            User regularUser = userService.createUser("Regular User", "regular@email.com", "333333333", "userpass", "EN", "Employee", "1995-05-05", 'M', null);
            
            try {
                standardAdmin.promoteUserToAdmin(regularUser);
                System.out.println("✗ Should have failed: Standard admin shouldn't be able to promote");
            } catch (IllegalStateException e) {
                System.out.println("✓ Promotion permission validation works: " + e.getMessage());
            }

            seniorAdmin.promoteUserToAdmin(regularUser);
            System.out.println("✓ Senior admin successfully promoted user");

            System.out.println("\n=== Testing Event Validation ===");
            try {
                // Test valid event creation
                Event event = eventService.createEvent(
                    "Tech Conference",
                    "Annual technology conference",
                    "Convention Center",
                    "2025-08-01T09:00",
                    "2025-08-01T17:00",
                    100,
                    "Professional",
                    "Conference",
                    "EN",
                    seniorAdmin
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

    }
}