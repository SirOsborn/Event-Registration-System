package com.eventms.model;

public class Event {

    public int eventId; 
    public String title;
    public String description;
    public String location;
    public String startTime;
    public String endTime;
    public String organizerId;
    public int capacity;
    public String preferredOccupation;
    private String eventStatus;
    public String createdAt;
    public String eventType;
    public String language;

    // Constructor for creating event
    public Event(int eventId, String title, String description, String location, String startTime, String endTime,
            int capacity, String preferredOccupation, String eventStatus, String eventType, String language) {
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
    }

    public static void createEvent(){

    }

    public static void updateEvent(){

    }

    public static void cancelEvent(){

    }

    //  constructor Find event
    public Event(int eventId, String title) {
        this.eventId = eventId;
        this.title = title;
    }

    public String getOpenEvents() {
        
    }
}