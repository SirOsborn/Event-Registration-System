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

    // Constructor with validation for external IDs and date format
    public Registration(int userId, int eventId, String registrationDate) {
        // Validate external IDs (userId and eventId are from other entities)
        if (userId <= 0) {
            throw new IllegalArgumentException("User ID must be positive");
        }
        if (eventId <= 0) {
            throw new IllegalArgumentException("Event ID must be positive");
        }
        // Validate date format
        if (registrationDate == null || !registrationDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
            throw new IllegalArgumentException("Registration date format should be YYYY-MM-DD");
        }

        // Auto-generate the registration ID
        this.registrationId = nextRegistrationId++;
        // Set the validated fields
        this.userId = userId;
        this.eventId = eventId;
        this.registrationDate = registrationDate;
        // Initialize default values
        this.checkinTime = null;
        this.status = RegistrationStatus.PENDING;
    }

    // Getters with validation
    public int getRegistrationId() { 
        return registrationId; 
    }

    public int getUserId() { 
        return userId; 
    }

    public int getEventId() { 
        return eventId; 
    }

    public RegistrationStatus getStatus() { 
        return status; 
    }

    public String getRegistrationDate() { 
        return registrationDate; 
    }

    // Only return checkin time if status is CONFIRMED
    public String getCheckinTime() { 
        if (status != RegistrationStatus.CONFIRMED) {
            return null;  // Hide checkin time for non-confirmed registrations
        }
        return checkinTime; 
    }
    
    // Setters with validation
    public void setStatus(RegistrationStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Registration status cannot be null");
        }
        this.status = status;
    }

    public void setRegistrationDate(String registrationDate) {
        if (registrationDate == null || !registrationDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
            throw new IllegalArgumentException("Registration date format should be YYYY-MM-DD");
        }
        this.registrationDate = registrationDate;
    }

    public void setCheckinTime(String checkinTime) {
        if (checkinTime != null && !checkinTime.matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}")) {
            throw new IllegalArgumentException("Checkin time format should be YYYY-MM-DDThh:mm");
        }
        this.checkinTime = checkinTime;
    }

    @Override
    public int getId() { return registrationId; }
}