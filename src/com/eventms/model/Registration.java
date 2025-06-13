package com.eventms.model;
public class Registration {
    private static int nextRegistrationId = 1; 
    private static final String[] VALID_STATUSES = {"Pending", "Confirmed", "Cancelled"};

    private int registrationId; 
    private int userId; 
    private int eventId; 
    private String registrationDate; 
    private String checkinTime; 
    private String status; 

    // Constructor
    public Registration(int userId, int eventId, String registrationDate) {
        this.registrationId = nextRegistrationId++; 
        this.userId = userId;
        this.eventId = eventId;
        this.registrationDate = registrationDate;
        this.status = "Pending"; 
    }


    public void getActiveRegistration(){

    }
    
    public void setRegistration(){

    }
    public void cancelRegistration(){

    }
}