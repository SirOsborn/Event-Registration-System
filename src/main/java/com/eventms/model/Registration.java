package com.eventms.model;

//Model Class: Represents a registration
public class Registration implements Identifier {

    private int registrationId;
    private int userId;
    private int eventId;
    private String registrationDate;
    private String checkinTime;
    private RegistrationStatus status;

    private static int nextRegistrationId = 1;

    // checkinTime is null by default, status is PENDING by default
    public Registration(int userId, int eventId, String registrationDate) {
        this.registrationId = nextRegistrationId++;
        this.userId = userId;
        this.eventId = eventId;
        this.registrationDate = registrationDate;
        this.checkinTime = null;
        this.status = RegistrationStatus.PENDING;
    }

    // Getters
    public int getRegistrationId() { return registrationId; }
    public int getUserId() { return userId; }
    public int getEventId() { return eventId; }
    public RegistrationStatus getStatus() { return status; }
    public String getRegistrationDate() { return registrationDate; }
    public String getCheckinTime() { return checkinTime; }
    
    // Setters
    public void setStatus(RegistrationStatus status) {
        this.status = status;
    }
    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }
    public void setCheckinTime(String checkinTime) {
        this.checkinTime = checkinTime;
    }

    @Override
    public int getId() { return registrationId; }
}