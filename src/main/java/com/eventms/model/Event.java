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

    // Main constructor with validation
    public Event(int eventId, String title, String description, String location, String startTime, String endTime,
                 int capacity, EventStatus eventStatus, String eventType, int organizerId) {
        if (eventId <= 0) {
            throw new IllegalArgumentException("Event ID must be positive");
        }
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Event title cannot be empty");
        }
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Event description cannot be empty");
        }
        if (capacity < 0) {
            throw new IllegalArgumentException("Capacity cannot be negative");
        }
        if (eventStatus == null) {
            throw new IllegalArgumentException("Event status cannot be null");
        }
        if (eventType == null || eventType.trim().isEmpty()) {
            throw new IllegalArgumentException("Event type cannot be empty");
        }
        if (organizerId <= 0) {
            throw new IllegalArgumentException("Organizer ID must be positive");
        }
        if (startTime != null && !startTime.matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}")) {
            throw new IllegalArgumentException("Start time format should be YYYY-MM-DDThh:mm");
        }
        if (endTime != null) {
            if (!endTime.matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}")) {
                throw new IllegalArgumentException("End time format should be YYYY-MM-DDThh:mm");
            }
            if (startTime != null && endTime.compareTo(startTime) <= 0) {
                throw new IllegalArgumentException("End time must be after start time");
            }
        }

        this.eventId = eventId;
        this.title = title.trim();
        this.description = description.trim();
        this.location = location != null ? location.trim() : "TBA";
        this.startTime = startTime;
        this.endTime = endTime;
        this.capacity = capacity;
        this.eventStatus = eventStatus;
        this.eventType = eventType.trim();
        this.organizerId = organizerId;
        this.createdAt = java.time.LocalDate.now().toString();
    }

    // Overloaded constructor with validation for required fields
    public Event(int eventId, String title, String description, int organizerId) {
        this(eventId, title, description, "TBA", null, null, 0, EventStatus.OPEN, "General", organizerId);
    }

    // Getters with protective logic
    public int getEventId() { 
        return eventId; 
    }

    public String getTitle() { 
        return title; 
    }

    public String getDescription() { 
        return description; 
    }

    public String getLocation() { 
        return location; 
    }

    public int getCapacity() { 
        return capacity; 
    }

    public EventStatus getEventStatus() { 
        return eventStatus; 
    }

    public int getOrganizerId() { 
        return organizerId; 
    }

    // Only return time details if event is confirmed or open
    public String getStartTime() { 
        if (eventStatus == EventStatus.CANCELLED) {
            return null;
        }
        return startTime; 
    }

    public String getEndTime() { 
        if (eventStatus == EventStatus.CANCELLED) {
            return null;
        }
        return endTime; 
    }

    public String getCreatedAt() { 
        return createdAt; 
    }

    public String getEventType() { 
        return eventType; 
    }

    // Setters with validation
    public void setTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Event title cannot be empty");
        }
        this.title = title.trim();
    }

    public void setDescription(String description) {
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Event description cannot be empty");
        }
        this.description = description.trim();
    }

    public void setLocation(String location) {
        if (location != null) {
            this.location = location.trim();
        }
    }

    public void setEventStatus(EventStatus eventStatus) {
        if (eventStatus == null) {
            throw new IllegalArgumentException("Event status cannot be null");
        }
        this.eventStatus = eventStatus;
    }

    public void setStartTime(String startTime) {
        if (startTime != null && !startTime.matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}")) {
            throw new IllegalArgumentException("Start time format should be YYYY-MM-DDThh:mm");
        }
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        if (endTime != null) {
            if (!endTime.matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}")) {
                throw new IllegalArgumentException("End time format should be YYYY-MM-DDThh:mm");
            }
            if (startTime != null && endTime.compareTo(startTime) <= 0) {
                throw new IllegalArgumentException("End time must be after start time");
            }
        }
        this.endTime = endTime;
    }

    public void setCapacity(int capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException("Capacity cannot be negative");
        }
        this.capacity = capacity;
    }

    public void setEventType(String eventType) {
        if (eventType == null || eventType.trim().isEmpty()) {
            throw new IllegalArgumentException("Event type cannot be empty");
        }
        this.eventType = eventType.trim();
    }

    public void setOrganizerId(int organizerId) {
        if (organizerId <= 0) {
            throw new IllegalArgumentException("Invalid organizer ID");
        }
        this.organizerId = organizerId;
    }

    public void setCreatedAt(String createdAt) {
        if (createdAt != null && !createdAt.matches("\\d{4}-\\d{2}-\\d{2}")) {
            throw new IllegalArgumentException("Created at date format should be YYYY-MM-DD");
        }
        this.createdAt = createdAt;
    }

    @Override
    public int getId() { return eventId; }
}