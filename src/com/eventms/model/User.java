package com.eventms.model;

/**
 * Model Class: Represents a user in the system.
 * This class only holds data. All logic is in UserService.
 */
public class User {

    private int userId;
    private String fullName;
    private String email;
    private String contactNumber;
    private String password; // Should be hashed
    private String language;
    private String createdAt;
    private String occupation;
    private String dob;
    private String role; // e.g., "ORGANIZER" or "GUEST"
    private char gender;

    // Constructor to initialize a User object
    public User(int userId, String fullName, String email, String contactNumber, String password, String language,
                String occupation, String dob, char gender, String role) {
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.contactNumber = contactNumber;
        this.password = password;
        this.language = language;
        this.occupation = occupation;
        this.dob = dob;
        this.gender = gender;
        this.role = role;
        this.createdAt = java.time.LocalDate.now().toString();
    }

    // --- Getters ---
    public int getUserId() { return userId; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getContactNumber() { return contactNumber; }
    public String getLanguage() { return language; }
    public String getOccupation() { return occupation; }
    public String getRole() { return role; }

    // --- Setters ---
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }
    public void setLanguage(String language) { this.language = language; }
    public void setOccupation(String occupation) { this.occupation = occupation; }
    public void setPassword(String newPassword) { this.password = newPassword; }
    
    // Special getter for password validation
    public boolean isPasswordCorrect(String inputPassword) {
        return this.password.equals(inputPassword);
    }
}