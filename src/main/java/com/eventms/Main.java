package com.eventms;

import com.eventms.model.Event;
import com.eventms.model.EventStatus;
import com.eventms.model.Registration;
import com.eventms.model.User;
import com.eventms.service.EventService;
import com.eventms.service.RegistrationService;
import com.eventms.service.UserService;

public class Main {
    public static void main(String[] args) {
        UserService userService = new UserService();
        EventService eventService = new EventService();
        RegistrationService registrationService = new RegistrationService();

        // Create a user
        User user = userService.createUser("Alice", "alice@email.com", "123456789", "pass", "EN", "Student", "2000-01-01", 'F');
        System.out.println("User ID: " + user.getId());

        // Create an event (using overloaded constructor)
        Event event = new Event(1, "OOP Seminar", "Learn OOP", user.getId());
        System.out.println("Event ID: " + event.getId());

        // Open the event for registration
        event.setEventStatus(EventStatus.OPEN);
        event.setCapacity(2);

        // Register the user for the event
        Registration reg = registrationService.registerGuest(user.getId(), event.getId(), "2025-07-11", eventService);
        if (reg != null) {
            System.out.println("Registration ID: " + reg.getId());
        }

        // Show static method usage
        System.out.println("Total registrations: " + RegistrationService.getTotalRegistrations());
    }
}