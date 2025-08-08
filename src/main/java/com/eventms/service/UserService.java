
package com.eventms.service;

import com.eventms.model.Event;
import com.eventms.model.User;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// Manages all business logic for User entities, including user management and authentication
public class UserService {
    private final HashMap<Integer, User> users = new HashMap<>();
    private final HashMap<String, User> loggedInUsers = new HashMap<>(); // sessionId -> User
    private final HashMap<String, Integer> emailToUserId = new HashMap<>(); // email -> userId for login
    private int nextUserId = 1;

    // Creates a new user. All new users default to the USER role and are not verified
    // Passwords are automatically hashed for security
    public User createUser(String fullName, String email, String contactNumber, String password, String language, String occupation, String dob, char gender) {
        // Check if email already exists
        if (emailToUserId.containsKey(email.toLowerCase())) {
            throw new IllegalArgumentException("User with this email already exists");
        }
        
        String hashedPassword = hashPassword(password);
        User newUser = new User(nextUserId, fullName, email, contactNumber, hashedPassword, occupation, dob, gender);
        users.put(nextUserId, newUser);
        emailToUserId.put(email.toLowerCase(), nextUserId);
        System.out.println("User created successfully: " + fullName + " with ID: " + nextUserId);
        nextUserId++;
        return newUser;
    }

    // Finds a user by their ID
    public User findUserById(int userId) {
        return users.get(userId);
    }

    // Updates a user's profile information
    public void updateUserProfile(int userId, String newFullName, String newContactNumber, String newOccupation) {
        User user = findUserById(userId);
        if (user != null) {
            user.setFullName(newFullName);
            user.setContactNumber(newContactNumber);
            user.setOccupation(newOccupation);
            System.out.println("User profile for ID " + userId + " updated.");
        } else {
            System.out.println("Error: Could not update profile. User not found.");
        }
    }

    // Verifies a user account
    public void verifyUser(int userId) {
        User user = findUserById(userId);
        if (user != null) {
            user.setVerified(true);
            System.out.println("User " + userId + " has been verified.");
        } else {
            System.out.println("Error: User not found.");
        }
    }

    // Gets all users in the system
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    // Gets past events for a user (placeholder implementation)
    public List<Event> getPastEvents(int userId) {
        System.out.println("Placeholder for getPastEvents(). Returning empty list.");
        return new ArrayList<>();
    }

    // ================== AUTHENTICATION METHODS ==================

    // Authenticates a user with email and password
    // Returns a session ID if successful, null if authentication fails
    public String login(String email, String password) {
        if (email == null || password == null) {
            System.out.println("Login failed: Email and password cannot be null");
            return null;
        }

        Integer userId = emailToUserId.get(email.toLowerCase());
        if (userId == null) {
            System.out.println("Login failed: User not found with email: " + email);
            return null;
        }

        User user = users.get(userId);
        if (user == null) {
            System.out.println("Login failed: User data inconsistency");
            return null;
        }

        if (!verifyPassword(password, user.getPassword())) {
            System.out.println("Login failed: Invalid password for user: " + email);
            return null;
        }

        // Generate session ID and store logged-in user
        String sessionId = generateSessionId();
        loggedInUsers.put(sessionId, user);
        System.out.println("Login successful: " + user.getFullName() + " (ID: " + user.getUserId() + ")");
        return sessionId;
    }

    // Logs out a user by removing their session
    public boolean logout(String sessionId) {
        if (sessionId == null) {
            return false;
        }

        User user = loggedInUsers.remove(sessionId);
        if (user != null) {
            System.out.println("Logout successful: " + user.getFullName());
            return true;
        }
        return false;
    }

    // Gets the currently logged-in user for a session
    public User getCurrentUser(String sessionId) {
        return loggedInUsers.get(sessionId);
    }

    // Checks if a user is currently logged in
    public boolean isLoggedIn(String sessionId) {
        return sessionId != null && loggedInUsers.containsKey(sessionId);
    }

    // Changes a user's password (requires current password verification)
    public boolean changePassword(String sessionId, String currentPassword, String newPassword) {
        User user = getCurrentUser(sessionId);
        if (user == null) {
            System.out.println("Password change failed: User not logged in");
            return false;
        }

        if (!verifyPassword(currentPassword, user.getPassword())) {
            System.out.println("Password change failed: Current password is incorrect");
            return false;
        }

        try {
            String hashedNewPassword = hashPassword(newPassword);
            user.setPassword(hashedNewPassword);
            System.out.println("Password changed successfully for user: " + user.getFullName());
            return true;
        } catch (IllegalArgumentException e) {
            System.out.println("Password change failed: " + e.getMessage());
            return false;
        }
    }

    // Initiates password reset for a user
    public String initiatePasswordReset(String email) {
        Integer userId = emailToUserId.get(email.toLowerCase());
        if (userId == null) {
            System.out.println("Password reset failed: User not found with email: " + email);
            return null;
        }

        String resetToken = generateSessionId(); // Using session ID generator for simplicity
        System.out.println("Password reset initiated for: " + email + " (Reset token: " + resetToken + ")");
        return resetToken;
    }

    // Resets password using a reset token (simplified implementation)
    public boolean resetPassword(String email, String resetToken, String newPassword) {
        Integer userId = emailToUserId.get(email.toLowerCase());
        if (userId == null) {
            System.out.println("Password reset failed: User not found");
            return false;
        }

        // For simplicity, we're just checking if the token exists
        User user = users.get(userId);
        if (user != null && resetToken != null && !resetToken.isEmpty()) {
            try {
                String hashedNewPassword = hashPassword(newPassword);
                user.setPassword(hashedNewPassword);
                System.out.println("Password reset successful for: " + email);
                return true;
            } catch (IllegalArgumentException e) {
                System.out.println("Password reset failed: " + e.getMessage());
                return false;
            }
        }
        
        System.out.println("Password reset failed: Invalid reset token");
        return false;
    }

    // ================== HELPER METHODS ==================

    // Hashes a password using SHA-256
    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }

    // Verifies a plain text password against a hashed password
    private boolean verifyPassword(String plainPassword, String hashedPassword) {
        return hashPassword(plainPassword).equals(hashedPassword);
    }

    // Generates a unique session ID
    private String generateSessionId() {
        return "session_" + System.currentTimeMillis() + "_" + Math.random();
    }
}