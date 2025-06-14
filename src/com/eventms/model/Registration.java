package com.eventms.model;

/**
 * Model Class: Represents a registration linking a User to an Event.
 * All logic is in RegistrationService.
 */
public class Registration {

    private int registrationId;
    private int userId;
    private int eventId;
    private String registrationDate;
    private String checkinTime;
    private String status; // e.g., "Pending", "Confirmed", "Cancelled"

    private static int nextRegistrationId = 1;

    public Registration(int userId, int eventId, String registrationDate) {
        this.registrationId = nextRegistrationId++;
        this.userId = userId;
        this.eventId = eventId;
        this.registrationDate = registrationDate;
        this.status = "Pending"; // Default status
    }

    // --- Getters ---
    public int getRegistrationId() { return registrationId; }
    public int getUserId() { return userId; }
    public int getEventId() { return eventId; }
    public String getStatus() { return status; }
    
    // --- Setters ---
    public void setStatus(String status) {
        if (status == null || status.isEmpty()) {
            throw new IllegalArgumentException("Status cannot be null or empty");  // alwasys validate input
        }
        this.status = status;
    }

    public void setCheckinTime(String checkinTime) {
        this.checkinTime = checkinTime;
    }
}