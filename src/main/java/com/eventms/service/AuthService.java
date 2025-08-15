package com.eventms.service;

import com.eventms.model.Role;
import com.eventms.model.User;

// Base service class that provides authentication functionality to all services
public abstract class AuthService {
    protected static UserService userService; // Static to share across all services

    // Sets the user service for authentication (dependency injection)
    public static void setUserService(UserService userService) {
        AuthService.userService = userService;
    }

    public static void userService(UserService userService) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    // Protected method to require authentication - throws exception if not logged in
    protected User requireAuthentication(String sessionId) {
        if (userService == null) {
            throw new SecurityException("User service not initialized");
        }
        User user = userService.getCurrentUser(sessionId);
        if (user == null) {
            throw new SecurityException("Authentication required. Please log in.");
        }
        return user;
    }

    // Protected method to require admin privileges - throws exception if not admin
    protected User requireAdmin(String sessionId) {
        User user = requireAuthentication(sessionId);
        if (user.getRole() != Role.ADMIN) {
            throw new SecurityException("Admin privileges required.");
        }
        return user;
    }

    // Protected method to require verified account - throws exception if not verified
    protected User requireVerified(String sessionId) {
        User user = requireAuthentication(sessionId);
        if (!user.isVerified()) {
            throw new SecurityException("Account verification required.");
        }
        return user;
    }

    // Protected method to check if user is authenticated
    protected boolean isAuthenticated(String sessionId) {
        return userService != null && userService.isLoggedIn(sessionId);
    }

    // Protected method to check if user is admin
    protected boolean isAdmin(String sessionId) {
        if (userService == null) return false;
        User user = userService.getCurrentUser(sessionId);
        return user != null && user.getRole() == Role.ADMIN;
    }

    // Protected method to check if user is verified
    protected boolean isVerified(String sessionId) {
        if (userService == null) return false;
        User user = userService.getCurrentUser(sessionId);
        return user != null && user.isVerified();
    }

    // Protected method to get current user
    protected User getCurrentUser(String sessionId) {
        return userService != null ? userService.getCurrentUser(sessionId) : null;
    }

    // Protected method to check if user can access another user's data
    protected boolean canAccessUserData(String sessionId, int targetUserId) {
        if (userService == null) return false;
        User currentUser = userService.getCurrentUser(sessionId);
        if (currentUser == null) return false;
        
        // User can access their own data or admin can access any data
        return currentUser.getUserId() == targetUserId || currentUser.getRole() == Role.ADMIN;
    }

    // Protected helper method to handle authentication errors consistently
    protected void handleAuthenticationError(SecurityException e, String operation) {
        System.out.println(operation + " failed: " + e.getMessage());
    }
}
