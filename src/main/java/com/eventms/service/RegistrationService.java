package com.eventms.service;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import com.eventms.model.Event;
import com.eventms.model.Registration;
import com.eventms.model.RegistrationStatus;

// Manages the registration of Users for Events.
public class RegistrationService {

    private final HashMap<Integer, Registration> registrations = new HashMap<>();
    private static int totalRegistrations = 0;
    
    // Helper method to get active registrations for an event.
    public List<Registration> getActiveRegistrations(int eventId) {
        return registrations.values().stream()
                .filter(r -> r.getEventId() == eventId)
                .filter(r -> r.getStatus() != RegistrationStatus.CANCELLED)
                .collect(Collectors.toList());
    }

    // Register Guest
    public Registration registerGuest(int userId, int eventId, String registrationDate, EventService eventService) {
        Event event = eventService.findEventById(eventId);
        if (event == null) {
            System.out.println("Error: Event not found.");
            return null;
        }

        long currentRegistrants = getActiveRegistrations(eventId).size();

        if (currentRegistrants >= event.getCapacity()) {
            System.out.println("Error: Event is full. Waiting list not yet implemented.");
            return null;
        }

        Registration newRegistration = new Registration(userId, eventId, registrationDate);
        registrations.put(newRegistration.getRegistrationId(), newRegistration);
        totalRegistrations++; // Increment static field
        System.out.println("Registration successful for User ID " + userId + " for Event ID " + eventId);
        return newRegistration;
    }

    //  Cancel Registration
    public void cancelRegistration(int registrationId) {
        Registration registration = registrations.get(registrationId);
        if (registration != null) {
            registration.setStatus(RegistrationStatus.CANCELLED);
            System.out.println("Registration " + registrationId + " has been cancelled.");
        }
    }

    public static int getTotalRegistrations() {
        return totalRegistrations;
    }
}