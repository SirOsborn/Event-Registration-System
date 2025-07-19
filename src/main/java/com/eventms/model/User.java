package com.eventms.model;

import java.util.Scanner;

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

    // Constructor with validation
    public User(int userId, String fullName, String email, String contactNumber, String password,
                String occupation, String dob, char gender, Role role) {
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new IllegalArgumentException("Full name cannot be empty");
        }
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Invalid email format");
        }
        if (password == null || password.length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters");
        }

        this.userId = userId;
        this.fullName = fullName.trim();
        this.email = email.toLowerCase();
        this.contactNumber = contactNumber;
        this.password = password;
        this.occupation = occupation;
        this.dob = dob;
        this.gender = gender;

        // Set default values for new attributes
        this.role = Role.USER;
        this.isVerified = false;
    }

    // Getters with conditions
    public int getUserId() { return userId; }
    
    public String getFullName() { return fullName; }
    
    public String getEmail() { 
        if (!isVerified) {
            return "Email hidden - User not verified";
        }
        return email; 
    }
    
    public String getContactNumber() { 
        if (!isVerified) {
            return "Contact hidden - User not verified";
        }
        return contactNumber; 
    }
    
    // Password getter is protected - only classes in same package can access
    protected String getPassword() { return password; }
    
    public String getDob() { return dob; }
    public char getGender() { return gender; }
    public String getOccupation() { return occupation; }
    public Role getRole() { return role; }
    public boolean isVerified() { return isVerified; }

    // Setters with validation
    public void setFullName(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new IllegalArgumentException("Full name cannot be empty");
        }
        this.fullName = fullName.trim();
    }

    public void setContactNumber(String contactNumber) {
        if (contactNumber != null && !contactNumber.matches("\\d+")) {
            throw new IllegalArgumentException("Contact number must contain only digits");
        }
        this.contactNumber = contactNumber;
    }

    public void setEmail(String email) {
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Invalid email format");
        }
        this.email = email.toLowerCase();
    }

    public void setOccupation(String occupation) {
        if (occupation != null) {
            this.occupation = occupation.trim();
        }
    }

    public void setPassword(String newPassword) {
        if (newPassword == null || newPassword.length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters");
        }
        this.password = newPassword;
    }

    // Protected setters - only accessible within package and by subclasses
    protected void setRole(Role role) {
        if (role == null) {
            throw new IllegalArgumentException("Role cannot be null");
        }
        this.role = role;
    }

    protected void setVerified(boolean verified) { 
        this.isVerified = verified;
    }

    public void setDob(String dob) {
        if (dob != null && !dob.matches("\\d{4}-\\d{2}-\\d{2}")) {
            throw new IllegalArgumentException("Date format should be YYYY-MM-DD");
        }
        this.dob = dob;
    }

    public void setGender(char gender) {
        if (gender != 'M' && gender != 'F' && gender != 'O') {
            throw new IllegalArgumentException("Gender must be 'M', 'F', or 'O'");
        }
        this.gender = gender;
    }

    @Override
    public int getId() { return userId; }
    // Display user information
    @Override
    public String toString() {
    return "User{" +
            "userId=" + userId +
            ", fullName='" + fullName + '\'' +
            ", email='" + email + '\'' +
            ", contactNumber='" + contactNumber + '\'' +
            ", occupation='" + occupation + '\'' +
            ", dob='" + dob + '\'' +
            '}';
    }

    // Method to Signup
    public void signUp() {
        
        Scanner scanner = new Scanner(System.in);
        System.out.println("Signup with your email");

        System.out.print("Enter your name: ");
        String fullname = scanner.nextLine();

        System.out.print("Enter your email: ");
        String email = scanner.nextLine();

        System.out.print("Enter your password: ");
        String password = scanner.nextLine();
        System.out.println("Registration successful for: " + fullname + " with email: " + email);
    }

}