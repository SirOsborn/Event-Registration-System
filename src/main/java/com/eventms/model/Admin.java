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

    // Getter and Setter for adminLevel
    public String getAdminLevel() {
        return adminLevel;
    }

    public void setAdminLevel(String adminLevel) {
        this.adminLevel = adminLevel;
    }

    // Only admin can promote a user to admin
    public void promoteUserToAdmin(User user) {
        if (user != null) {
            user.setRole(Role.ADMIN);
            System.out.println("User " + user.getFullName() + " has been promoted to ADMIN.");
        } else {
            System.out.println("Error: User not found.");
        }
    }
}