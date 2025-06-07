package com.eventms.model;

/**
 * Represents a user of the system.
 * Corresponds to requirements in section 3.1.1 (User Account Management).
 */
public class User {
    private static int idCounter = 0;
    
    private final int id;
    private final String username;
    private final String email;
    private String password; // Note: In a real system, this should be securely hashed (NFR-SEC-001).
    private final Role role;

    // Guest-specific profile information as per FR-UAM-004
    private String fullName;
    private String contactNumber;
    private String socialMediaLink;

    /**
     * Constructor for a new User.
     * @param username The username for login.
     * @param password The user's password.
     * @param email The user's email for notifications.
     * @param role The role of the user (ORGANIZER or GUEST).
     */
    public User(String username, String password, String email, Role role) {
        this.id = ++idCounter;
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    // --- Getters ---
    public int getId() { return id; }
    public String getUsername() { return username; }
    public Role getRole() { return role; }

    // --- We will add more methods here later ---
}