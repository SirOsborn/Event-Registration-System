package com.eventms.model;

//Model Class: Represents a registration
public class Registration {

    private int registrationId;
    private int userId;
    private int eventId;
    private String registrationDate;
    private String checkinTime;
    private RegistrationStatus status;

    private static int nextRegistrationId = 1;

    public Registration(int userId, int eventId, String registrationDate) {
        this.registrationId = nextRegistrationId++;
        this.userId = userId;
        this.eventId = eventId;
        this.registrationDate = registrationDate;
        this.status = RegistrationStatus.PENDING;
    }

    // Getters
    public int getRegistrationId() { return registrationId; }
    public int getUserId() { return userId; }
    public int getEventId() { return eventId; }
    public RegistrationStatus getStatus() { return status; }
    
    // Setters
    public void setStatus(RegistrationStatus status) {
        this.status = status;
    }

    public void setCheckinTime(String checkinTime) {
        this.checkinTime = checkinTime;
    }
}