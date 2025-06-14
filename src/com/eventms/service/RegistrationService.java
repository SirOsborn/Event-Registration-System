package com.eventms.service;

import com.eventms.model.Event;
import com.eventms.model.Registration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


// Manages the registration of Users for Events.
public class RegistrationService {

    private final Map<Integer, Registration> registrations = new HashMap<>();
    
    
    // Helper method to get active registrations for an event.
    public List<Registration> getActiveRegistrationsForEvent(int eventId) {
        return registrations.values().stream()
                .filter(r -> r.getEventId() == eventId)
                .filter(r -> !"Cancelled".equalsIgnoreCase(r.getStatus()))
                .collect(Collectors.toList());
    }

    
    // Behavior: registerGuest() / setRegistration()
    public Registration registerGuest(int userId, int eventId, String registrationDate, EventService eventService) {
        Event event = eventService.findEventById(eventId);
        if (event == null || !"Open".equalsIgnoreCase(event.getEventStatus())) {
            System.out.println("Error: Event is not available for registration.");
            return null;
        }

        long currentRegistrants = getActiveRegistrationsForEvent(eventId).size();

        if (currentRegistrants >= event.getCapacity()) {
            System.out.println("Error: Event is full. Waiting list not yet implemented.");
            return null;
        }

        Registration newRegistration = new Registration(userId, eventId, registrationDate);
        registrations.put(newRegistration.getRegistrationId(), newRegistration);
        System.out.println("User " + userId + " successfully registered for event " + eventId);
        return newRegistration;
    }


    // Behavior: cancelRegistration()
    public void cancelRegistration(int registrationId) {
        Registration registration = registrations.get(registrationId);
        if (registration != null) {
            registration.setStatus("Cancelled");
            System.out.println("Registration " + registrationId + " has been cancelled.");
            // In a real system, you would call a method here to check the waiting list.
        }
    }
}