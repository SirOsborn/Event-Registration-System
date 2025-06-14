package com.eventms;

import com.eventms.model.Event;
import com.eventms.model.Registration;
import com.eventms.model.User;
import com.eventms.service.EventService;
import com.eventms.service.RegistrationService;
import com.eventms.service.UserService;
import java.util.List;

/**
 * The main entry point for the Event Management System application.
 * This class is used to simulate user actions and test the system's logic.
 */
public class Main {

    public static void main(String[] args) {

        System.out.println("--- Event Management System Simulation ---");

        // 1. Initialize all the services
        UserService userService = new UserService();
        EventService eventService = new EventService();
        RegistrationService registrationService = new RegistrationService();

        // 2. Create Users (Behavior: createUser)
        System.out.println("\n--- [STEP 1: Creating Users] ---");
        User organizer = userService.createUser("Sun Heng", "sun.heng@organizer.com", "012345678", "pass123", "Khmer", "Project Lead", "1999-01-01", 'M', "ORGANIZER");
        User guest = userService.createUser("Veat Bunchhour", "bunchhour.v@guest.com", "098765432", "pass456", "English", "Student", "2000-02-02", 'M', "GUEST");

        // 3. Organizer Creates an Event (Behavior: createEvent)
        System.out.println("\n--- [STEP 2: Organizer Creates an Event] ---");
        Event workshop = eventService.createEvent("Java Programming Workshop", "A workshop for beginners.", "CamTech, Room 101", "2025-08-10 09:00", "2025-08-10 17:00", 50, "Student", "Workshop", "English", organizer);

        // Let's create another event to test capacity
        Event seminar = eventService.createEvent("Public Speaking Seminar", "Learn to speak confidently.", "Main Hall", "2025-09-15 14:00", "2025-09-15 16:00", 1, "Any", "Seminar", "English", organizer);


        // 4. Guest Registers for an Event (Behavior: registerGuest)
        System.out.println("\n--- [STEP 3: Guest Registers for an Event] ---");
        registrationService.registerGuest(guest.getUserId(), workshop.getEventId(), "2025-06-15", eventService);

        // 5. Check the status
        System.out.println("\n--- [STEP 4: Checking System Status] ---");
        List<Event> openEvents = eventService.getOpenEvents();
        System.out.println("Currently open events: " + openEvents.size());
        for (Event e : openEvents) {
            System.out.println("  - " + e.getTitle());
        }

        List<Registration> workshopRegistrations = registrationService.getActiveRegistrationsForEvent(workshop.getEventId());
        System.out.println("Active registrations for '" + workshop.getTitle() + "': " + workshopRegistrations.size());


        // 6. Test Event Capacity Limit
        System.out.println("\n--- [STEP 5: Testing Event Capacity] ---");
        System.out.println("Registering for '" + seminar.getTitle() + "' which has capacity of " + seminar.getCapacity());
        
        // First guest registers successfully
        registrationService.registerGuest(guest.getUserId(), seminar.getEventId(), "2025-06-15", eventService);

        // A second user tries to register for the full event
        User anotherGuest = userService.createUser("Cheav Vichar", "vichar.c@guest.com", "011223344", "pass789", "English", "Student", "2001-03-03", 'M', "GUEST");
        System.out.println("\n" + anotherGuest.getFullName() + " is now trying to register for the full seminar...");
        registrationService.registerGuest(anotherGuest.getUserId(), seminar.getEventId(), "2025-06-15", eventService);


        // 7. Organizer cancels an event
        System.out.println("\n--- [STEP 6: Organizer Cancels an Event] ---");
        eventService.cancelEvent(workshop.getEventId());
        System.out.println("Status of '" + workshop.getTitle() + "' is now: " + workshop.getEventStatus());

        System.out.println("\n--- Simulation Complete ---");
    }
}