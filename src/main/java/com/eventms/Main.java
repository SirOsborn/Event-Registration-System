package com.eventms;

import com.eventms.model.Admin;
import com.eventms.model.Event;
import com.eventms.model.User;
import com.eventms.service.EventService;
import com.eventms.service.UserService;

public class Main {
    public static void main(String[] args) {
        UserService userService = new UserService();
        EventService eventService = new EventService();

        // Create a default admin
        Admin admin = new Admin(1, "Default Admin", "admin@email.com", "000000000", "adminpass", "Administrator", "1990-01-01", 'M');
        System.out.println("Admin created: " + admin.getFullName() + ", ID: " + admin.getId() + ", Role: " + admin.getRole());

        // Test 1: Create a user
        User user = userService.createUser("Bob", "bob@email.com", "987654321", "adminpass", "EN", "Manager", "1995-05-05", 'M', null);
        System.out.println("User created: " + user.getFullName() + ", ID: " + user.getId() + ", Role: " + user.getRole());

        // Promote user to admin using admin
        admin.promoteUserToAdmin(user);
        System.out.println("User promoted by admin. New role: " + user.getRole());

        // Test 2: Create an event as admin
        Event event = eventService.createEvent(
            "Admin Conference",
            "A conference for admins.",
            "Main Hall",
            "2025-08-01 09:00",
            "2025-08-01 17:00",
            100,
            "Manager",
            "Conference",
            "EN",
            admin
        );
        System.out.println("Event created: " + event.getTitle() + ", ID: " + event.getId() + ", Status: " + event.getEventStatus());
    }
}