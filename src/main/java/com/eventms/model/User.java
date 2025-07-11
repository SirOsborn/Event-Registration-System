package com.eventms.model;

//Model Class: Represents a user with verification and role attributes.

public class User implements Identifier {

    private int userId;
    private String fullName;
    private String email;
    private String contactNumber;
    private String password;
    private String occupation;
    private String dob;
    private char gender;
    private Role role;
    private boolean isVerified;

    // Constructor
    public User(int userId, String fullName, String email, String contactNumber, String password,
                String occupation, String dob, char gender) {
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.contactNumber = contactNumber;
        this.password = password;
        this.occupation = occupation;
        this.dob = dob;
        this.gender = gender;

        // Set default values for new attributes
        this.role = Role.USER;
        this.isVerified = false;
    }

    // Getters for all fields
    public int getUserId() { return userId; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getContactNumber() { return contactNumber; }
    public String getPassword() { return password; }
    public String getDob() { return dob; }
    public char getGender() { return gender; }
    public String getOccupation() { return occupation; }
    public Role getRole() { return role; }
    public boolean isVerified() { return isVerified; }

    // Setters for all fields
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }
    public void setEmail(String email) { this.email = email; }
    public void setOccupation(String occupation) { this.occupation = occupation; }
    public void setPassword(String newPassword) { this.password = newPassword; }
    public void setRole(Role role) { this.role = role; }
    public void setVerified(boolean verified) { isVerified = verified; }
    public void setDob(String dob) { this.dob = dob; }
    public void setGender(char gender) { this.gender = gender; }

    @Override
    public int getId() { return userId; }
}