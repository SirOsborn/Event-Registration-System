package com.eventms.service;

import com.eventms.model.Event;
import com.eventms.model.User;
import com.eventms.model.Role;
import com.eventms.model.VerificationStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


// Manages all business logic for User entities, including the verification process.
public class UserService {

    private final Map<Integer, User> users = new HashMap<>();
    private int nextUserId = 1;


    // Creates a new user. All new users default to the USER role and are not verified.
    public User createUser(String fullName, String email, String contactNumber, String password, String language, String occupation, String dob, char gender) {
        // Pass null for role, as the constructor will set the default to USER
        User newUser = new User(nextUserId, fullName, email, contactNumber, password, language, occupation, dob, gender);
        users.put(nextUserId, newUser);
        System.out.println("User created successfully: " + fullName + " with ID: " + nextUserId);
        nextUserId++;
        return newUser;
    }


    // Submits a verification request for a user.
    public void requestVerification(int userId, String idCardUrl) {
        User user = findUserById(userId);
        if (user != null && user.getVerificationStatus() == VerificationStatus.NOT_REQUESTED) {
            user.setOrganizationIdCardUrl(idCardUrl);
            user.setVerificationStatus(VerificationStatus.PENDING_REVIEW);
            System.out.println("User " + user.getFullName() + " has submitted a verification request.");
        } else {
            System.out.println("Error: User not found or a verification request is already in progress.");
        }
    }


    // An ADMIN approves a user's verification request.
    public void approveVerification(User admin, int targetUserId) {
        if (admin.getRole() != Role.ADMIN) {
            System.out.println("ACTION DENIED: Only ADMINs can approve verification requests.");
            return;
        }
        User targetUser = findUserById(targetUserId);
        if (targetUser != null && targetUser.getVerificationStatus() == VerificationStatus.PENDING_REVIEW) {
            targetUser.setVerified(true);
            targetUser.setVerificationStatus(VerificationStatus.APPROVED);
            System.out.println("SUCCESS: " + targetUser.getFullName() + "'s verification has been approved by " + admin.getFullName());
        } else {
            System.out.println("Error: Target user not found or is not pending review.");
        }
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