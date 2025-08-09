package com.eventms.model;


// User model with validation for all fields. Base class for Organizer and Admin
public class User implements Displayable {
    @Override
    public void displayInfo() {
        System.out.println("[USER DISPLAY]");
        System.out.println("ID: " + getUserId());
        System.out.println("Name: " + getFullName());
        System.out.println("Email: " + getEmail());
        System.out.println("Contact: " + getContactNumber());
        System.out.println("Occupation: " + getOccupation());
        System.out.println("DOB: " + getDob());
        System.out.println("Gender: " + getGender());
        System.out.println("Role: " + getRole());
        System.out.println("Created At: " + getCreatedAt());
    }

    protected int userId;
    protected String fullName;
    protected String email;
    protected String contactNumber;
    protected String password;
    protected String occupation;
    protected String dob;
    protected char gender;
    protected Role role;
    protected boolean isVerified;
    protected String createdAt;

    // Constructs a User with validation for all fields
    public User(int userId, String fullName, String email, String contactNumber, String password,
                String occupation, String dob, char gender) {
        // Direct field assignment with validation to avoid overridable method calls
        if (userId <= 0) {
            throw new IllegalArgumentException("User ID must be positive");
        }
        this.userId = userId;
        
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new IllegalArgumentException("Full name cannot be empty");
        }
        this.fullName = fullName.trim();
        
        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new IllegalArgumentException("Invalid email format");
        }
        this.email = email.toLowerCase();
        
        if (contactNumber == null || !contactNumber.matches("\\d{9,15}")) {
            throw new IllegalArgumentException("Contact number must be 9-15 digits");
        }
        this.contactNumber = contactNumber;
        
        if (password == null || password.length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters");
        }
        this.password = password;
        
        if (occupation == null || occupation.trim().isEmpty()) {
            throw new IllegalArgumentException("Occupation cannot be empty");
        }
        this.occupation = occupation.trim();
        
        if (dob == null || !dob.matches("\\d{4}-\\d{2}-\\d{2}")) {
            throw new IllegalArgumentException("Date of birth format should be YYYY-MM-DD");
        }
        this.dob = dob;
        
        if (gender != 'M' && gender != 'F' && gender != 'O') {
            throw new IllegalArgumentException("Gender must be 'M', 'F', or 'O'");
        }
        this.gender = gender;
        
        this.role = Role.USER;
        this.isVerified = false;
        this.createdAt = java.time.LocalDateTime.now().toString();
    }

    // Getters
    public int getUserId() { return userId; }
    public String getFullName() { return fullName; }
    public String getEmail() {
        return email;
    }
    // Raw email for persistence and internal logic (unmasked)
    public String getEmailRaw() { return email; }
    public String getContactNumber() {
        return contactNumber;
    }
    public String getPassword() { return password; }
    public String getDob() { return dob; }
    public char getGender() { return gender; }
    public String getOccupation() { return occupation; }
    public Role getRole() { return role; }
    public boolean isVerified() { return isVerified; }
    public String getCreatedAt() { return createdAt; }

    // Setters with validation
    public void setUserId(int userId) {
        if (userId <= 0) {
            throw new IllegalArgumentException("User ID must be positive");
        }
        this.userId = userId;
    }

    public void setFullName(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new IllegalArgumentException("Full name cannot be empty");
        }
        this.fullName = fullName.trim();
    }

    public void setContactNumber(String contactNumber) {
        if (contactNumber == null || !contactNumber.matches("\\d{9,15}")) {
            throw new IllegalArgumentException("Contact number must be 9-15 digits");
        }
        this.contactNumber = contactNumber;
    }

    public void setEmail(String email) {
        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new IllegalArgumentException("Invalid email format");
        }
        this.email = email.toLowerCase();
    }

    public void setOccupation(String occupation) {
        if (occupation == null || occupation.trim().isEmpty()) {
            throw new IllegalArgumentException("Occupation cannot be empty");
        }
        this.occupation = occupation.trim();
    }

    public void setPassword(String newPassword) {
        if (newPassword == null || newPassword.length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters");
        }
        this.password = newPassword;
    }

    public void setRole(Role role) {
        if (role == null) {
            throw new IllegalArgumentException("Role cannot be null");
        }
        this.role = role;
    }

    public void setVerified(boolean verified) {
        this.isVerified = verified;
    }

    public void setDob(String dob) {
        if (dob == null || !dob.matches("\\d{4}-\\d{2}-\\d{2}")) {
            throw new IllegalArgumentException("Date of birth format should be YYYY-MM-DD");
        }
        this.dob = dob;
    }

    public void setGender(char gender) {
        if (gender != 'M' && gender != 'F' && gender != 'O') {
            throw new IllegalArgumentException("Gender must be 'M', 'F', or 'O'");
        }
        this.gender = gender;
    }

    // Returns a string representation of the user (for debugging/logging)
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
}