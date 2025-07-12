package com.eventms.service;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import com.eventms.model.Event;
import com.eventms.model.EventStatus;

// Manages all business logic for Event entities, including the approval workflow.
public class EventService {

    private final HashMap<Integer, Event> events = new HashMap<>();
    private int nextEventId = 1;

    // Create Event
    public Event createEvent(String title, String description, String location, String startTime, String endTime, 
                int capacity, String preferredOccupation, String eventType, String language, com.eventms.model.User creator) {
        Event newEvent = new Event(nextEventId++, title, description, location, startTime, endTime, capacity, creator.isVerified() ? EventStatus.OPEN : EventStatus.CLOSED, eventType, creator.getId());
        events.put(newEvent.getEventId(), newEvent);
        System.out.println("Event created successfully: " + title + " with ID: " + newEvent.getEventId());
        return newEvent;
    }

    // Cancel Event
    public void cancelEvent(int eventId) {
        Event event = findEventById(eventId);
        if (event != null) {
            event.setEventStatus(EventStatus.CANCELLED);
            System.out.println("Event '" + event.getTitle() + "' has been cancelled.");
        }
    }
    
    // Get all open event
    public List<Event> getOpenEvents() {
        return events.values().stream()
                .filter(event -> event.getEventStatus() == EventStatus.OPEN)
                .collect(Collectors.toList());
    }
    
    //  Update methods for Event Status
    public Event findEventById(int eventId) {
        return events.get(eventId);
    }
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