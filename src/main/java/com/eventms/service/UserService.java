
package com.eventms.service;

import com.eventms.model.Event;
import com.eventms.model.User;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Manages all business logic for User entities, including user management.
 */
public class UserService {
    private final HashMap<Integer, User> users = new HashMap<>();
    private int nextUserId = 1;

    /**
     * Creates a new user. All new users default to the USER role and are not verified.
     */
    public User createUser(String fullName, String email, String contactNumber, String password, String language, String occupation, String dob, char gender) {
        User newUser = new User(nextUserId, fullName, email, contactNumber, password, occupation, dob, gender);
        users.put(nextUserId, newUser);
        System.out.println("User created successfully: " + fullName + " with ID: " + nextUserId);
        nextUserId++;
        return newUser;
    }

    /**
     * Finds a user by their ID.
     */
    public User findUserById(int userId) {
        return users.get(userId);
    }

    /**
     * Updates a user's profile information.
     */
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

    /**
     * Verifies a user account.
     */
    public void verifyUser(int userId) {
        User user = findUserById(userId);
        if (user != null) {
            user.setVerified(true);
            System.out.println("User " + userId + " has been verified.");
        } else {
            System.out.println("Error: User not found.");
        }
    }

    /**
     * Gets all users in the system.
     */
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    /**
     * Gets past events for a user (placeholder implementation).
     */
    public List<Event> getPastEvents(int userId) {
        System.out.println("Placeholder for getPastEvents(). Returning empty list.");
        return new ArrayList<>();
    }
}