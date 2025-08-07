package com.eventms.model;

// Model Class: Represents an event.
public class Event implements Displayable {
    @Override
    public void displayInfo() {
        System.out.println("[EVENT DISPLAY]");
        System.out.println("Event ID: " + getEventId());
        System.out.println("Title: " + getTitle());
        System.out.println("Description: " + getDescription());
        System.out.println("Location: " + getLocation());
        System.out.println("Start: " + getStartTime());
        System.out.println("End: " + getEndTime());
        System.out.println("Capacity: " + getCapacity());
        System.out.println("Status: " + getEventStatus());
        System.out.println("Type: " + getEventType());
        System.out.println("Organizer ID: " + getOrganizerId());
        System.out.println("Created At: " + getCreatedAt());
        System.out.println("Token ID: " + getTokenId());
    }

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
    private int tokenId;

    // Main constructor with validation
    public Event(int eventId, String title, String description, String location, String startTime, String endTime,
                 int capacity, EventStatus eventStatus, String eventType, int organizerId, int tokenId) {
        this.eventId = validateId(eventId);
        this.title = validateTitle(title);
        this.description = validateDescription(description);
        this.location = (location != null) ? location.trim() : "TBA";
        this.startTime = validateStartTime(startTime);
        this.endTime = validateEndTime(endTime, startTime);
        this.capacity = validateCapacity(capacity);
        this.eventStatus = validateEventStatus(eventStatus);
        this.eventType = validateEventType(eventType);
        this.organizerId = validateOrganizerId(organizerId);
        this.tokenId = tokenId;
        this.createdAt = java.time.LocalDateTime.now().toString();
    }

    // Overloaded constructor with validation for required fields
    public Event(int eventId, String title, String description, int organizerId) {
        this(eventId, title, description, "TBA", null, null, 0, EventStatus.OPEN, "General", organizerId, 0);
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

    public int getTokenId() {
        return tokenId;
    }

    // Setters with validation
    public void setTitle(String title) {
        this.title = validateTitle(title);
    }

    public void setDescription(String description) {
        this.description = validateDescription(description);
    }

    public void setLocation(String location) {
        this.location = (location != null) ? location.trim() : this.location;
    }

    public void setEventStatus(EventStatus eventStatus) {
        this.eventStatus = validateEventStatus(eventStatus);
    }

    public void setStartTime(String startTime) {
        this.startTime = validateStartTime(startTime);
    }

    public void setEndTime(String endTime) {
        this.endTime = validateEndTime(endTime, this.startTime);
    }

    public void setCapacity(int capacity) {
        this.capacity = validateCapacity(capacity);
    }

    public void setEventType(String eventType) {
        this.eventType = validateEventType(eventType);
    }

    public void setOrganizerId(int organizerId) {
        this.organizerId = validateOrganizerId(organizerId);
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = validateCreatedAt(createdAt);
    }

    public void setTokenId(int tokenId) {
        this.tokenId = tokenId;
    }

    // --- Validation helpers ---
    private static int validateId(int id) {
        if (id <= 0) throw new IllegalArgumentException("Event ID must be positive");
        return id;
    }
    private static String validateTitle(String title) {
        if (title == null || title.trim().isEmpty()) throw new IllegalArgumentException("Event title cannot be empty");
        return title.trim();
    }
    private static String validateDescription(String description) {
        if (description == null || description.trim().isEmpty()) throw new IllegalArgumentException("Event description cannot be empty");
        return description.trim();
    }
    private static int validateCapacity(int capacity) {
        if (capacity < 0) throw new IllegalArgumentException("Capacity cannot be negative");
        return capacity;
    }
    private static EventStatus validateEventStatus(EventStatus status) {
        if (status == null) throw new IllegalArgumentException("Event status cannot be null");
        return status;
    }
    private static String validateEventType(String eventType) {
        if (eventType == null || eventType.trim().isEmpty()) throw new IllegalArgumentException("Event type cannot be empty");
        return eventType.trim();
    }
    private static int validateOrganizerId(int organizerId) {
        if (organizerId <= 0) throw new IllegalArgumentException("Organizer ID must be positive");
        return organizerId;
    }
    private static String validateStartTime(String startTime) {
        if (startTime != null && !startTime.matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}"))
            throw new IllegalArgumentException("Start time format should be YYYY-MM-DDThh:mm");
        return startTime;
    }
    private static String validateEndTime(String endTime, String startTime) {
        if (endTime != null) {
            if (!endTime.matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}"))
                throw new IllegalArgumentException("End time format should be YYYY-MM-DDThh:mm");
            if (startTime != null && endTime.compareTo(startTime) <= 0)
                throw new IllegalArgumentException("End time must be after start time");
        }
        return endTime;
    }
    private static String validateCreatedAt(String createdAt) {
        if (createdAt != null && !createdAt.matches("\\d{4}-\\d{2}-\\d{2}"))
            throw new IllegalArgumentException("Created at date format should be YYYY-MM-DD");
        return createdAt;
    }
}