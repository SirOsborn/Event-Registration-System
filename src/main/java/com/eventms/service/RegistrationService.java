package com.eventms.service;

import com.eventms.model.Event;
import com.eventms.model.Registration;
import com.eventms.model.RegistrationStatus;
import com.eventms.model.User;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

// Service for managing event registrations, including registering guests and cancelling registrations
// All operations require authentication. Extends AuthService for inheritance
public class RegistrationService extends AuthService {

    private final HashMap<Integer, Registration> registrations = new HashMap<>();
    private static int totalRegistrations = 0;

    // Constructor for RegistrationService
    public RegistrationService() {
        super(); // Call parent constructor
    }
    
    // Returns all active (not cancelled) registrations for a given event
    public List<Registration> getActiveRegistrations(int eventId) {
        return registrations.values().stream()
                .filter(r -> r.getEventId() == eventId)
                .filter(r -> r.getStatus() != RegistrationStatus.CANCELLED)
                .collect(Collectors.toList());
    }

    // Registers a user for an event if there is capacity
    // Requires authentication
    // @return The new Registration, or null if registration failed
    public Registration registerGuest(String sessionId, int eventId, String registrationDate, EventService eventService) {
        try {
            // Use inherited method to require authentication
            User currentUser = requireAuthentication(sessionId);
            int userId = currentUser.getUserId();
            
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
            totalRegistrations++;
            System.out.println("Registration successful for User ID " + userId + " for Event ID " + eventId);
            return newRegistration;
        } catch (SecurityException e) {
            handleAuthenticationError(e, "Registration");
            return null;
        }
    }

    // Cancels a registration by ID
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