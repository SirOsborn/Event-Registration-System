package com.eventms.model;

//Model Class: Represents an admin user with elevated privileges.
public class Admin extends User {

    private String adminLevel;

    public Admin(int userId, String fullName, String email, String contactNumber, String password,
                 String occupation, String dob, char gender) {
        super(userId, fullName, email, contactNumber, password, occupation, dob, gender, Role.ADMIN);
        setRole(Role.ADMIN); // Only Admin can call this
        this.adminLevel = "standard"; // Default admin level
    }

    // Getter and Setter for adminLevel with validation
    public String getAdminLevel() {
        return adminLevel;
    }

    public void setAdminLevel(String adminLevel) {
        if (adminLevel == null || adminLevel.trim().isEmpty()) {
            throw new IllegalArgumentException("Admin level cannot be empty");
        }
        if (!adminLevel.matches("(?i)(standard|senior|super)")) {
            throw new IllegalArgumentException("Admin level must be 'standard', 'senior', or 'super'");
        }
        this.adminLevel = adminLevel.toLowerCase();
    }

    // Only admin can promote a user to admin with validation
    public void promoteUserToAdmin(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        if (user.getRole() == Role.ADMIN) {
            throw new IllegalArgumentException("User is already an admin");
        }
        if (!this.isVerified()) {
            throw new IllegalStateException("Unverified admins cannot promote users");
        }
        if (!this.adminLevel.equals("super") && !this.adminLevel.equals("senior")) {
            throw new IllegalStateException("Only senior or super admins can promote users");
        }
        
        user.setRole(Role.ADMIN);
        System.out.println("User " + user.getFullName() + " has been promoted to ADMIN.");
    }
    @Override
    public String toString(){
        return super.toString() + ", Admin Level: " + adminLevel;
    }

    @Override
    public void signUp() {
    super.signUp(); 
    System.out.println("As admin" + getRole()); 
    }

}
