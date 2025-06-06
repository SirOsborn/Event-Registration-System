package com.eventms.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Represents an event created by an Organizer.
 * Corresponds to requirements in section 3.1.2 (Event Management).
 */
public class Event {
    private static int idCounter = 0;

    private final int id;
    private final String name;
    private final LocalDateTime dateTime;
    private final String location;
    private final int capacity;
    private final EventCategory category;
    private final User organizer;

    // Lists to manage participants as per section 3.1.3
    private final List<User> registeredGuests;
    private final Queue<User> waitingList;

    /**
     * Constructor for a new Event.
     * An event can only be created by a user with the ORGANIZER role.
     * @param name The name of the event.
     * @param dateTime The date and time of the event.
     * @param location The venue or location.
     * @param capacity The maximum number of participants.
     * @param category The category of the event.
     * @param organizer The user who is creating the event.
     */
    public Event(String name, LocalDateTime dateTime, String location, int capacity, EventCategory category, User organizer) {
        // Enforce the rule that only an Organizer can create an event.
        if (organizer.getRole() != Role.ORGANIZER) {
            throw new IllegalArgumentException("Error: Only users with the ORGANIZER role can create events.");
        }

        this.id = ++idCounter;
        this.name = name;
        this.dateTime = dateTime;
        this.location = location;
        this.capacity = capacity;
        this.category = category;
        this.organizer = organizer;
        
        // Initialize the lists for participants
        this.registeredGuests = new ArrayList<>();
        this.waitingList = new LinkedList<>();
    }

    // --- We will add methods for registration, cancellation, etc. here later ---
    
    @Override
    public String toString() {
        return "Event '" + name + "' by " + organizer.getUsername();
    }
}