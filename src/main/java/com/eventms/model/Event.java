package com.eventms.model;

// Model Class: Represents an event.
public class Event implements Identifier {

    private int eventId;
    private String title;
    private String description;
    private String location;
    private String startTime;
    private String endTime;
    private int organizerId;
    private int capacity;
    private EventStatus eventStatus;
    private String createdAt;
    private String eventType;

    // Main constructor with all fields
    public Event(int eventId, String title, String description, String location, String startTime, String endTime,
                 int capacity, EventStatus eventStatus, String eventType, int organizerId) {
        this.eventId = eventId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.startTime = startTime;
        this.endTime = endTime;
        this.capacity = capacity;
        this.eventStatus = eventStatus;
        this.eventType = eventType;
        this.organizerId = organizerId;
        this.createdAt = java.time.LocalDate.now().toString();
    }

    // Overloaded constructor: only required fields
    public Event(int eventId, String title, String description, int organizerId) {
        this(eventId, title, description, "TBA", null, null, 0, EventStatus.OPEN, "General", organizerId);
    }

    // Getters
    public int getEventId() { return eventId; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getLocation() { return location; }
    public int getCapacity() { return capacity; }
    public EventStatus getEventStatus() { return eventStatus; }
    public int getOrganizerId() { return organizerId; }
    public String getStartTime() { return startTime; }
    public String getEndTime() { return endTime; }
    public String getCreatedAt() { return createdAt; }
    public String getEventType() { return eventType; }

    // Setters
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setLocation(String location) { this.location = location; }
    public void setEventStatus(EventStatus eventStatus) { this.eventStatus = eventStatus; }
    public void setStartTime(String startTime) { this.startTime = startTime; }
    public void setEndTime(String endTime) { this.endTime = endTime; }
    public void setCapacity(int capacity) { this.capacity = capacity; }
    public void setEventType(String eventType) { this.eventType = eventType; }
    public void setOrganizerId(int organizerId) { this.organizerId = organizerId; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    @Override
    public int getId() { return eventId; }
}