package com.eventms.model;

public class Admin extends User {
    // Constants
    private static final String ADMIN_EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@admin\\.com$";
    private static final String ADMIN_PASSWORD_PATTERN = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[!@#$%^&*]).{8,}$";

    public Admin(int userId, String fullName, String email, String contactNumber, String password,
                 String occupation, String dob, char gender) {
        super(userId, fullName, email, contactNumber, password, occupation, dob, gender, Role.ADMIN_STANDARD);

        // Set verified status to true as admins are trusted users
        setVerified(true);
    }

    // Override getters and setters with additional conditions
    @Override
    public void setEmail(String email) {
        if (email == null || !email.matches(ADMIN_EMAIL_PATTERN)) {
            throw new IllegalArgumentException("Invalid admin email format");
        }
        super.setEmail(email);
    }

    @Override
    public void setPassword(String password) {
        if (password == null || !password.matches(ADMIN_PASSWORD_PATTERN)) {
            throw new IllegalArgumentException("Invalid admin password format");
        }
        super.setPassword(password);
    }

    // Override equals method to reuse superclass implementation
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Admin)) return false;
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    // Implement static method for Admin
    public static void greeting() {
        System.out.println("Welcome Administrator! You have elevated privileges.");
        System.out.println("Please use your privileges responsibly.");
    }

    // Ensure admin levels are handled correctly
    public void upgradeAdminLevel(Role newRole) {
        if (newRole == null || !newRole.isAdmin()) {
            throw new IllegalArgumentException("Invalid admin role");
        }
        if (newRole.getAccessLevel() <= getRole().getAccessLevel()) {
            throw new IllegalArgumentException("Cannot downgrade or maintain the same level");
        }
        setRole(newRole);
        System.out.println(String.format("Admin level upgraded to: %s", newRole.getDisplayName()));
    }

    // Method to promote a user to admin
    public void promoteUserToAdmin(User user, Role targetAdminRole) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        if (targetAdminRole == null || !targetAdminRole.isAdmin()) {
            throw new IllegalArgumentException("Invalid target admin role");
        }

        // Debug statements to verify roles and access levels
        System.out.println("Promoter Role: " + this.getRole().getDisplayName() + " (Level: " + this.getRole().getAccessLevel() + ")");
        System.out.println("Target Role: " + targetAdminRole.getDisplayName() + " (Level: " + targetAdminRole.getAccessLevel() + ")");
        System.out.println("User's Current Role: " + (user.getRole() != null ? user.getRole().getDisplayName() : "None"));

        if (targetAdminRole.getAccessLevel() <= this.getRole().getAccessLevel()) {
            throw new SecurityException("Cannot promote to the same or lower admin level");
        }

        user.setRole(targetAdminRole);
        System.out.println(String.format("User %s promoted to %s", user.getFullName(), targetAdminRole.getDisplayName()));
    }
}