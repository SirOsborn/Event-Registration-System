package com.eventms.service;

import com.eventms.model.Event;
import com.eventms.model.EventStatus;
import com.eventms.model.EventToken;
import com.eventms.model.User;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for managing events, including creation, cancellation, and status updates.
 * Users can create events using valid tokens multiple times.
 */
public class EventService {

    private final HashMap<Integer, Event> events = new HashMap<>();
    private int nextEventId = 1;

    /**
     * Creates an event. Users can create events by providing a valid token.
     * Tokens can be used multiple times.
     * @param creator The user attempting to create the event.
     * @param tokenString The token string for validation.
     * @param adminService The admin service for token validation.
     * @return The created Event, or null if not permitted.
     */
    public Event createEvent(String title, String description, String location, String startTime, String endTime,
                            int capacity, String eventType, User creator, String tokenString, AdminService adminService) {
        if (creator == null) {
            System.out.println("Error: Creator cannot be null.");
            return null;
        }
        
        // Check if token is valid
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
    }

    // Cancels an event by setting its status to CANCELLED.

    public void cancelEvent(int eventId) {
        Event event = findEventById(eventId);
        if (event != null) {
            event.setEventStatus(EventStatus.CANCELLED);
            System.out.println("Event '" + event.getTitle() + "' has been cancelled.");
        }
    }

    // Returns all events with OPEN status.
    public List<Event> getOpenEvents() {
        return events.values().stream()
                .filter(event -> event.getEventStatus() == EventStatus.OPEN)
                .collect(Collectors.toList());
    }

    // Finds an event by its ID.
    public Event findEventById(int eventId) {
        return events.get(eventId);
    }

    // Updates an event's status.
    public void updateEvent(int eventId, String newEventStatus) {
        Event event = findEventById(eventId);
        if (event != null) {
            EventStatus status = EventStatus.valueOf(newEventStatus.toUpperCase());
            event.setEventStatus(status);
            System.out.println("Event '" + event.getTitle() + "' status updated to: " + status);
        } else {
            System.out.println("Error: Event not found.");
        }
    }
}