package com.eventms.model;

/**
 * Model Class: Represents an event.
 * All logic is in EventService.
 */
public class Event {

    private int eventId;
    private String title;
    private String description;
    private String location;
    private String startTime;
    private String endTime;
    private int organizerId; // The ID of the user who is the organizer
    private int capacity;
    private String preferredOccupation;
    private String eventStatus; // e.g., "Open", "Closed", "Cancelled"
    private String createdAt;
    private String eventType;
    private String language;

    public Event(int eventId, String title, String description, String location, String startTime, String endTime,
                 int capacity, String preferredOccupation, String eventStatus, String eventType, String language, int organizerId) {
        this.eventId = eventId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.startTime = startTime;
        this.endTime = endTime;
        this.capacity = capacity;
        this.preferredOccupation = preferredOccupation;
        this.eventStatus = eventStatus;
        this.eventType = eventType;
        this.language = language;
        this.organizerId = organizerId;
        this.createdAt = java.time.LocalDate.now().toString();
    }

    // --- Getters ---
    public int getEventId() { return eventId; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getLocation() { return location; }
    public int getCapacity() { return capacity; }
    public String getEventStatus() { return eventStatus; }
    public int getOrganizerId() { return organizerId; }

    // --- Setters ---
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setLocation(String location) { this.location = location; }
    public void setEventStatus(String eventStatus) { this.eventStatus = eventStatus; }
}