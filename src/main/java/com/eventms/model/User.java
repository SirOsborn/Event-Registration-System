package com.eventms.model;

//Model Class: Represents a user with verification and role attributes.

public class User {

    private int userId;
    private String fullName;
    private String email;
    private String contactNumber;
    private String password;
    private String language;
    private String createdAt;
    private String occupation;
    private String dob;
    private char gender;
    private Role role;
    private boolean isVerified;
    private VerificationStatus verificationStatus;
    private String organizationIdCardUrl;

    // Constructor
    public User(int userId, String fullName, String email, String contactNumber, String password, String language,
                String occupation, String dob, char gender) {
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.contactNumber = contactNumber;
        this.password = password;
        this.language = language;
        this.occupation = occupation;
        this.dob = dob;
        this.gender = gender;
        this.createdAt = java.time.LocalDate.now().toString();

        // Set default values for new attributes
        this.role = Role.USER;
        this.isVerified = false;
        this.verificationStatus = VerificationStatus.NOT_REQUESTED;
    }

    // Getters for all fields
    public int getUserId() { return userId; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getContactNumber() { return contactNumber; }
    public String getLanguage() { return language; }
    public String getOccupation() { return occupation; }
    public Role getRole() { return role; }
    public boolean isVerified() { return isVerified; }
    public VerificationStatus getVerificationStatus() { return verificationStatus; }
    public String getOrganizationIdCardUrl() { return organizationIdCardUrl; }

    // Setters for all fields
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }
    public void setLanguage(String language) { this.language = language; }
    public void setOccupation(String occupation) { this.occupation = occupation; }
    public void setPassword(String newPassword) { this.password = newPassword; }
    public void setRole(Role role) { this.role = role; }
    public void setVerified(boolean verified) { isVerified = verified; }
    public void setVerificationStatus(VerificationStatus status) { this.verificationStatus = status; }
    public void setOrganizationIdCardUrl(String url) { this.organizationIdCardUrl = url; }
    
    public boolean isPasswordCorrect(String inputPassword) {
        return this.password.equals(inputPassword);
    }
}