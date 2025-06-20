package com.eventms.service;

import com.eventms.model.Event;
import com.eventms.model.EventStatus;
import com.eventms.model.Role;
import com.eventms.model.User;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// Manages all business logic for Event entities, including the approval workflow.
public class EventService {

    private final Map<Integer, Event> events = new HashMap<>();
    private int nextEventId = 1;

    // Behavior: createEvent()
    public Event createEvent(String title, String description, String location, String startTime, String endTime,
                             int capacity, String preferredOccupation, String eventType, String language, User creator) {
        
        EventStatus initialStatus = creator.isVerified() ? EventStatus.OPEN : EventStatus.PENDING_APPROVAL;
        
        Event newEvent = new Event(nextEventId, title, description, location, startTime, endTime, capacity, preferredOccupation, initialStatus, eventType, language, creator.getUserId());
        events.put(nextEventId, newEvent);
        nextEventId++;
        System.out.println("Event '" + title + "' created. Status: " + initialStatus);
        return newEvent;
    }

    // Behavior: approveEvent()
    public void approveEvent(User admin, int eventId) {
        if (admin.getRole() != Role.ADMIN) {
            System.out.println("ACTION DENIED: Only ADMINs can approve events.");
            return;
        }
        Event event = findEventById(eventId);
        if (event != null && event.getEventStatus() == EventStatus.PENDING_APPROVAL) {
            event.setEventStatus(EventStatus.OPEN);
            System.out.println("SUCCESS: Event '" + event.getTitle() + "' has been approved.");
        } else {
            System.out.println("Error: Event not found or not pending approval.");
        }
    }

    // Behavior: cancelEvent()
    public void cancelEvent(int eventId) {
        Event event = findEventById(eventId);
        if (event != null) {
            event.setEventStatus(EventStatus.CANCELLED);
            System.out.println("Event '" + event.getTitle() + "' has been cancelled.");
        }
    }
    
    // Behavior: getOpenEvents()
    public List<Event> getOpenEvents() {
        return events.values().stream()
                .filter(event -> event.getEventStatus() == EventStatus.OPEN)
                .collect(Collectors.toList());
    }
    
    // Other methods for EventService
    public Event findEventById(int eventId) { return events.get(eventId); }
    public void updateEvent(int eventId, String newTitle, String newDescription) { /* ... */ }
}