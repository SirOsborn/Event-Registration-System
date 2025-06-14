package com.eventms.service;

import com.eventms.model.User;
import com.eventms.model.Event;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


// Manages all business logic related to User entities.
public class UserService {

    private final Map<Integer, User> users = new HashMap<>();
    private int nextUserId = 1;

    
    // Behavior: createUser()
    public User createUser(String fullName, String email, String contactNumber, String password, String language, String occupation, String dob, char gender, String role) {
        User newUser = new User(nextUserId, fullName, email, contactNumber, password, language, occupation, dob, gender, role);
        users.put(nextUserId, newUser);
        System.out.println("User created successfully: " + fullName + " with ID: " + nextUserId);
        nextUserId++;
        return newUser;
    }


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
        // This requires collaboration with RegistrationService
        System.out.println("Placeholder for getPastEvents(). Returning empty list.");
        return new ArrayList<>();
    }


    // Behavior: changeLanguage()
    public void changeLanguage(int userId, String newLanguage) {
        User user = findUserById(userId);
        if (user != null) {
            user.setLanguage(newLanguage);
            System.out.println("Language preference for " + user.getFullName() + " changed to " + newLanguage);
        } else {
            System.out.println("Error: Could not change language. User not found.");
        }
    }
}