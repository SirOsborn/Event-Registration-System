package com.eventms.model;

import java.util.Scanner;

//Model Class: Represents a user with verification and role attributes.

public class User implements Identifier {

    // Fundamental identifier - cannot be changed after creation
    private final int userId;
    
    // Personal information - can be modified with proper validation
    private String fullName;
    private String email;
    private String contactNumber;
    
    // Additional information
    private String occupation;
    private String dob;
    private char gender;
    
    // Security and role management - protected access
    protected Role role;
    protected boolean isVerified;

    // Reintroduced password field
    private String password;

    // Constructor using direct field assignments to avoid overridable method calls
    public User(int userId, String fullName, String email, String contactNumber, String password,
                String occupation, String dob, char gender, Role role) {
        if (userId <= 0) {
            throw new IllegalArgumentException("User ID must be positive");
        }
        this.userId = userId;

        // Direct assignments with validation
        this.fullName = validateFullName(fullName);
        this.email = validateEmail(email);
        this.contactNumber = validateContactNumber(contactNumber);
        this.occupation = validateOccupation(occupation);
        this.dob = validateDob(dob);
        this.gender = validateGender(gender);
        this.role = role != null ? role : Role.USER;

        this.isVerified = false;

        // Password is set only during signup, not through constructor
    }

    // Validation methods for direct assignments
    private String validateFullName(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new IllegalArgumentException("Full name cannot be empty");
        }
        if (fullName.trim().length() < 2) {
            throw new IllegalArgumentException("Full name must be at least 2 characters long");
        }
        if (!fullName.matches("^[\\p{L}\\s'.,-]+$")) {
            throw new IllegalArgumentException("Full name can only contain letters, spaces, and basic punctuation");
        }
        return fullName.trim();
    }

    private String validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        if (!email.matches(emailRegex)) {
            throw new IllegalArgumentException("Invalid email format");
        }
        return email.toLowerCase().trim();
    }

    private String validateContactNumber(String contactNumber) {
        if (contactNumber == null) {
            throw new IllegalArgumentException("Contact number cannot be null");
        }
        if (!contactNumber.matches("^\\d{8,15}$")) {
            throw new IllegalArgumentException("Contact number must be between 8 and 15 digits");
        }
        return contactNumber;
    }

    private String validateOccupation(String occupation) {
        if (occupation == null || occupation.trim().isEmpty()) {
            return null;
        }
        if (!occupation.matches("^[\\p{L}\\s'.,-]+$")) {
            throw new IllegalArgumentException("Occupation can only contain letters, spaces, and basic punctuation");
        }
        return occupation.trim();
    }

    private String validateDob(String dob) {
        if (dob == null) {
            throw new IllegalArgumentException("Date of birth cannot be null");
        }
        if (!dob.matches("\\d{4}-\\d{2}-\\d{2}")) {
            throw new IllegalArgumentException("Date format must be YYYY-MM-DD");
        }

        int year = Integer.parseInt(dob.substring(0, 4));
        int month = Integer.parseInt(dob.substring(5, 7));
        int day = Integer.parseInt(dob.substring(8, 10));

        if (year < 1900 || year > 2025) {
            throw new IllegalArgumentException("Year must be between 1900 and 2025");
        }
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("Month must be between 1 and 12");
        }
        if (day < 1 || day > 31) {
            throw new IllegalArgumentException("Day must be between 1 and 31");
        }

        return dob;
    }

    private char validateGender(char gender) {
        gender = Character.toUpperCase(gender);
        if (gender != 'M' && gender != 'F' && gender != 'O') {
            throw new IllegalArgumentException("Gender must be 'M' (Male), 'F' (Female), or 'O' (Other)");
        }
        return gender;
    }

    // Getters with enhanced validation and privacy protection
    @Override
    public int getId() { return userId; }
    
    public String getFullName() { 
        if (fullName == null) {
            throw new IllegalStateException("Full name has not been set");
        }
        return fullName;
    }
    
    public String getEmail() { 
        if (!isVerified) {
            throw new SecurityException("Email access denied - User not verified");
        }
        if (email == null) {
            throw new IllegalStateException("Email has not been set");
        }
        return email; 
    }
    
    public String getContactNumber() { 
        if (!isVerified) {
            throw new SecurityException("Contact number access denied - User not verified");
        }
        if (contactNumber == null) {
            throw new IllegalStateException("Contact number has not been set");
        }
        return contactNumber; 
    }
    
    // Remove password getter entirely - passwords should never be retrievable
    
    public String getDob() { 
        if (!isVerified) {
            throw new SecurityException("DOB access denied - User not verified");
        }
        if (dob == null) {
            throw new IllegalStateException("Date of birth has not been set");
        }
        return dob; 
    }
    
    public char getGender() { 
        if (!isVerified) {
            throw new SecurityException("Gender access denied - User not verified");
        }
        if (gender != 'M' && gender != 'F' && gender != 'O') {
            throw new IllegalStateException("Invalid gender value stored");
        }
        return gender; 
    }

    public String getOccupation() { 
        return occupation != null ? occupation : "Not specified";
    }
    
    // Role and verification status - public access
    public Role getRole() { 
        if (role == null) {
            throw new IllegalStateException("Role has not been set");
        }
        return role; 
    }
    
    public boolean isVerified() { return isVerified; }

    // Setters with enhanced validation
    public void setFullName(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new IllegalArgumentException("Full name cannot be empty");
        }
        if (fullName.trim().length() < 2) {
            throw new IllegalArgumentException("Full name must be at least 2 characters long");
        }
        if (!fullName.matches("^[\\p{L}\\s'.,-]+$")) {
            throw new IllegalArgumentException("Full name can only contain letters, spaces, and basic punctuation");
        }
        this.fullName = fullName.trim();
    }

    public void setContactNumber(String contactNumber) {
        if (contactNumber == null) {
            throw new IllegalArgumentException("Contact number cannot be null");
        }
        if (!contactNumber.matches("^\\d{8,15}$")) {
            throw new IllegalArgumentException("Contact number must be between 8 and 15 digits");
        }
        this.contactNumber = contactNumber;
    }

    public void setEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        if (!email.matches(emailRegex)) {
            throw new IllegalArgumentException("Invalid email format");
        }
        this.email = email.toLowerCase().trim();
    }

    // Method to validate and set password
    public void setPassword(String newPassword) {
        if (newPassword == null || newPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        if (newPassword.length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters long");
        }
        if (!newPassword.matches(".*[A-Z].*")) {
            throw new IllegalArgumentException("Password must contain at least one uppercase letter");
        }
        if (!newPassword.matches(".*[a-z].*")) {
            throw new IllegalArgumentException("Password must contain at least one lowercase letter");
        }
        if (!newPassword.matches(".*\\d.*")) {
            throw new IllegalArgumentException("Password must contain at least one number");
        }
        this.password = newPassword;
    }

    // Method to verify password during login
    public boolean verifyPassword(String inputPassword) {
        if (inputPassword == null) {
            throw new IllegalArgumentException("Password cannot be null");
        }
        return this.password.equals(inputPassword);
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
        if (dob == null) {
            throw new IllegalArgumentException("Date of birth cannot be null");
        }
        if (!dob.matches("\\d{4}-\\d{2}-\\d{2}")) {
            throw new IllegalArgumentException("Date format must be YYYY-MM-DD");
        }
        
        // Parse and validate date components
        int year = Integer.parseInt(dob.substring(0, 4));
        int month = Integer.parseInt(dob.substring(5, 7));
        int day = Integer.parseInt(dob.substring(8, 10));
        
        if (year < 1900 || year > 2025) {
            throw new IllegalArgumentException("Year must be between 1900 and 2025");
        }
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("Month must be between 1 and 12");
        }
        if (day < 1 || day > 31) {
            throw new IllegalArgumentException("Day must be between 1 and 31");
        }
        
        this.dob = dob;
    }

    public void setGender(char gender) {
        gender = Character.toUpperCase(gender);
        if (gender != 'M' && gender != 'F' && gender != 'O') {
            throw new IllegalArgumentException("Gender must be 'M' (Male), 'F' (Female), or 'O' (Other)");
        }
        this.gender = gender;
    }

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
        String inputName = scanner.nextLine();
        setFullName(inputName);

        System.out.print("Enter your email: ");
        String inputEmail = scanner.nextLine();
        setEmail(inputEmail);

        System.out.print("Enter your password: ");
        String inputPassword = scanner.nextLine();
        setPassword(inputPassword);
        
        System.out.println("Registration successful for: " + getFullName() + " with email: " + getEmail());
    }

// equal checking method
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;

        if (!(obj instanceof User)) return false;
        User user = (User) obj;
        return userId == user.userId &&
                fullName.equals(user.fullName) &&
                email.equals(user.email);
    }


    @Override
    public int hashCode() {
        int result = Integer.hashCode(userId);
        result = 31 * result + fullName.hashCode();
        result = 31 * result + email.hashCode();
        return result;
    }

    public static void greeting() {
        System.out.println("Welcome to Event Management System!");
    }

}