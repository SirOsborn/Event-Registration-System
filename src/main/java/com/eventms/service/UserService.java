package com.eventms.service;

import com.eventms.model.Event;
import com.eventms.model.Role;
import com.eventms.model.User;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


// Manages all business logic for User entities, including the verification process.
public class UserService {

    private final HashMap<Integer, User> users = new HashMap<>();
    private int nextUserId = 1;


    // Creates a new user. All new users default to the USER role and are not verified.
    public User createUser(String fullName, String email, String contactNumber, String password, String language, String occupation, String dob, char gender, Role role) {
        // Pass null for role, as the constructor will set the default to USER
        User newUser = new User(nextUserId, fullName, email, contactNumber, password, occupation, dob, gender, null);
        users.put(nextUserId, newUser);
        System.out.println("User created successfully: " + fullName + " with ID: " + nextUserId);
        nextUserId++;
        return newUser;
    }

    // Find this admin by id
    // Behavior: findUserById()
    public User findUserById(int userId) {
        return users.get(userId);
    }
    

    // Behavior: updateUserProfile()
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


    // Behavior: getPastEvents()
    public List<Event> getPastEvents(int userId) {
        System.out.println("Placeholder for getPastEvents(). Returning empty list.");
        return new ArrayList<>();
    }
}