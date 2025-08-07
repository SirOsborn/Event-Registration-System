package com.eventms;

import com.eventms.model.*;
import com.eventms.service.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        // Services
        EventService eventService = new EventService();
        RegistrationService registrationService = new RegistrationService();
        AdminService adminService = new AdminService();

        // Demonstrate OOP concepts and Database-aligned features
        List<Displayable> displayables = new ArrayList<>();

        // Create Admin
        Admin admin = new Admin(1, "Alice Admin", "alice@admin.com", "1112223333", "adminpass123", "Manager", "1980-01-01", 'F');
        displayables.add(admin);

        // Generate event token
        EventToken token = adminService.generateEventToken(admin.getUserId());
        displayables.add(token);

        // Create Users
        User user1 = new User(2, "Bob User", "bob@user.com", "2223334444", "userpass123", "Developer", "1990-02-02", 'M');
        User user2 = new User(3, "Carol User", "carol@user.com", "3334445555", "userpass123", "Designer", "1985-03-03", 'F');
        displayables.add(user1);
        displayables.add(user2);

        // Create Events using tokens (multiple users can use same token multiple times)
        Event event1 = eventService.createEvent("Tech Conference 2025", "A comprehensive technology conference featuring keynote speakers", "New York", "2025-09-15T09:00", "2025-09-15T17:00", 100, "Conference", user1, token.getTokenString(), adminService);
        displayables.add(event1);

        Event event2 = eventService.createEvent("Workshop Series", "Hands-on workshops for developers and designers", "San Francisco", "2025-10-01T10:00", "2025-10-01T16:00", 50, "Workshop", user2, token.getTokenString(), adminService);
        displayables.add(event2);

        // Create Registration
        Registration registration = registrationService.registerGuest(user2.getUserId(), event1.getEventId(), "2025-08-01", eventService);
        displayables.add(registration);

        // Polymorphic display
        System.out.println("=== DATABASE-ALIGNED EVENT MANAGEMENT SYSTEM ===");
        System.out.println("--- Polymorphic Display (Displayable Interface) ---");
        for (Displayable d : displayables) {
            d.displayInfo();
            System.out.println();
        }

        // Show toString() usage
        System.out.println("--- toString() Output ---");
        System.out.println(admin);
        System.out.println(user1);
        System.out.println(user2);
        System.out.println(event1);
        System.out.println(event2);
        System.out.println(registration);
        System.out.println(token);

        // Demonstrate static fields/methods
        System.out.println("\n--- Static Field Demonstration ---");
        System.out.println("Total registrations created: " + RegistrationService.getTotalRegistrations());
        
        // Show token reusability
        System.out.println("\n--- Token Reusability Demonstration ---");
        System.out.println("Token use count: " + token.getUseCount());
        System.out.println("Token can be reused: " + adminService.isValidToken(token.getTokenString()));
    }
}