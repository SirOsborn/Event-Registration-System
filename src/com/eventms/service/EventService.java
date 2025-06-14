package com.eventms.service;

import com.eventms.model.Event;
import com.eventms.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


// Manages all business logic for Event entities.
public class EventService {

    private final Map<Integer, Event> events = new HashMap<>();
    private int nextEventId = 1;


    // Behavior: createEvent()
    public Event createEvent(String title, String description, String location, String startTime, String endTime,
                             int capacity, String preferredOccupation, String eventType, String language, User organizer) {
        
        if (!"ORGANIZER".equalsIgnoreCase(organizer.getRole())) {
            System.out.println("Error: Only an ORGANIZER can create an event.");
            return null;
        }

        Event newEvent = new Event(nextEventId, title, description, location, startTime, endTime, capacity, preferredOccupation, "Open", eventType, language, organizer.getUserId());
        events.put(nextEventId, newEvent);
        System.out.println("Event '" + title + "' created successfully.");
        nextEventId++;
        return newEvent;
    }


    // Behavior: updateEvent()
    public void updateEvent(int eventId, String newTitle, String newDescription) {
        Event event = findEventById(eventId);
        if (event != null) {
            event.setTitle(newTitle);
            event.setDescription(newDescription);
            System.out.println("Event " + eventId + " updated.");
        } else {
            System.out.println("Error: Event not found.");
        }
    }

    
    // Behavior: cancelEvent()
    public void cancelEvent(int eventId) {
        Event event = findEventById(eventId);
        if (event != null) {
            event.setEventStatus("Cancelled");
            System.out.println("Event '" + event.getTitle() + "' has been cancelled.");
        }
    }
    

    // Behavior: findEventById()
    public Event findEventById(int eventId) {
        return events.get(eventId);
    }
    

    // Behavior: getOpenEvents()
    public List<Event> getOpenEvents() {
        return events.values().stream()
                .filter(event -> "Open".equalsIgnoreCase(event.getEventStatus()))
                .collect(Collectors.toList());
    }
}