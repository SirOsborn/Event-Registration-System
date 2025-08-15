package com.eventms.service;

import com.eventms.model.Event;
import com.eventms.model.EventStatus;
import com.eventms.model.EventToken;
import com.eventms.model.User;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

// Service for managing events, including creation, cancellation, and status updates
// Users can create events using valid tokens multiple times
// Authentication is required for all operations. Extends AuthService for inheritance
public class EventService extends AuthService {

    private final HashMap<Integer, Event> events = new HashMap<>();
    private int nextEventId = 1;

    // Constructor for EventService
    public EventService() {
        super(); // Call parent constructor
    }

    // Creates an event. Users can create events by providing a valid token and being authenticated
    // Tokens can be used multiple times
    // @param sessionId The user's session ID for authentication
    // @param tokenString The token string for validation
    // @param adminService The admin service for token validation
    // @return The created Event, or null if not permitted
    public Event createEvent(String title, String description, String location, String startTime, String endTime,
                            int capacity, String eventType, String sessionId, String tokenString, AdminService adminService) {
        try {
            // Use inherited method to require authentication
            User creator = requireAuthentication(sessionId);
            
            // Validate token
            if (!adminService.isValidToken(tokenString)) {
                System.out.println("Error: Invalid or expired token. Please get a valid token from an admin.");
                return null;
            }
            
            // Get token details
            EventToken token = adminService.getToken(tokenString);
            
            // Create event with token ID
            Event newEvent = new Event(nextEventId++, title, description, location, startTime, endTime, capacity, EventStatus.OPEN, eventType, creator.getUserId(), token.getTokenId());
            events.put(newEvent.getEventId(), newEvent);
            
            // Increment token use count (but don't mark as "used" since it can be reused)
            token.incrementUseCount();
            
            System.out.println("Event created successfully: " + title + " with ID: " + newEvent.getEventId() + " by user " + creator.getUserId());
            return newEvent;
        } catch (SecurityException e) {
            handleAuthenticationError(e, "Event creation");
            return null;
        }
    }

    // Cancels an event by setting its status to CANCELLED
    // Requires authentication and appropriate permissions
    public void cancelEvent(int eventId, String sessionId) {
        try {
            // Use inherited method to require authentication
            User currentUser = requireAuthentication(sessionId);
            
            Event event = findEventById(eventId);
            if (event != null) {
                // Check if user can cancel this event (creator or admin)
                if (currentUser.getUserId() == event.getOrganizerId() || isAdmin(sessionId)) {
                    event.setEventStatus(EventStatus.CANCELLED);
                    System.out.println("Event '" + event.getTitle() + "' has been cancelled.");
                } else {
                    System.out.println("Error: You don't have permission to cancel this event.");
                }
            } else {
                System.out.println("Error: Event not found.");
            }
        } catch (SecurityException e) {
            handleAuthenticationError(e, "Event cancellation");
        }
    }

    // Returns all events with OPEN status
    public List<Event> getOpenEvents() {
        return events.values().stream()
                .filter(event -> event.getEventStatus() == EventStatus.OPEN)
                .collect(Collectors.toList());
    }

    // Finds an event by its ID
    public Event findEventById(int eventId) {
        return events.get(eventId);
    }

    // Updates an event's status
    // Requires authentication and appropriate permissions
    public void updateEvent(int eventId, String newEventStatus, String sessionId) {
        try {
            // Use inherited method to require authentication
            User currentUser = requireAuthentication(sessionId);
            
            Event event = findEventById(eventId);
            if (event != null) {
                // Check if user can update this event (creator or admin)
                if (currentUser.getUserId() == event.getOrganizerId() || isAdmin(sessionId)) {
                    EventStatus status = EventStatus.valueOf(newEventStatus.toUpperCase());
                    event.setEventStatus(status);
                    System.out.println("Event '" + event.getTitle() + "' status updated to: " + status);
                } else {
                    System.out.println("Error: You don't have permission to update this event.");
                }
            } else {
                System.out.println("Error: Event not found.");
            }
        } catch (SecurityException e) {
            handleAuthenticationError(e, "Event update");
        }
    }
}