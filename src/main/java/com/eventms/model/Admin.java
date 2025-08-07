package com.eventms.model;

/**
 * Admin class extends User with admin-specific functionality.
 */
public class Admin extends User implements Displayable {

    public Admin(int userId, String fullName, String email, String contactNumber, String password,
                 String occupation, String dob, char gender) {
        super(userId, fullName, email, contactNumber, password, occupation, dob, gender);
        // Direct field assignment to avoid overridable method call
        this.role = Role.ADMIN;
    }

    @Override
    public String toString(){
        return super.toString() + ", Role: ADMIN";
    }

    @Override
    public void displayInfo() {
        System.out.println("[ADMIN DISPLAY]");
        System.out.println("ID: " + getUserId());
        System.out.println("Name: " + getFullName());
        System.out.println("Email: " + getEmail());
        System.out.println("Contact: " + getContactNumber());
        System.out.println("Occupation: " + getOccupation());
        System.out.println("DOB: " + getDob());
        System.out.println("Gender: " + getGender());
        System.out.println("Role: ADMIN");
        System.out.println("Created At: " + getCreatedAt());
    }
}
