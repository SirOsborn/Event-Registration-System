package com.eventms.service;

import com.eventms.model.EventToken;
import com.eventms.model.User;
import java.util.HashMap;
import java.util.UUID;

// Service for Admin-specific actions. Admins can generate event creation tokens for users
// All operations require authentication. Extends AuthService for inheritance
public class AdminService extends AuthService {
    private final HashMap<String, EventToken> issuedTokens = new HashMap<>();
    private int nextTokenId = 1;

    // Generates a unique event creation token. Only authenticated Admins can call this
    // @param sessionId The session ID of the admin
    // @return the generated EventToken, or null if not authorized
    public EventToken generateEventToken(String sessionId) {
        try {
            // Use inherited method to require admin authentication
            User admin = requireAdmin(sessionId);
            
            String tokenString = "TOKEN" + UUID.randomUUID().toString().replace("-", "").substring(0, 10);
            EventToken token = new EventToken(nextTokenId++, tokenString, admin.getUserId());
            issuedTokens.put(tokenString, token);
            System.out.println("Event creation token generated: " + tokenString + " by admin " + admin.getUserId());
            return token;
        } catch (SecurityException e) {
            handleAuthenticationError(e, "Token generation");
            return null;
        }
    }

    // Checks if a token was issued and is valid
    public boolean isValidToken(String tokenString) {
        EventToken token = issuedTokens.get(tokenString);
        return token != null; // Tokens can be reused multiple times
    }

    // Increments token usage count (tokens can be reused)
    public boolean useToken(String tokenString) {
        EventToken token = issuedTokens.get(tokenString);
        if (token != null) {
            token.incrementUseCount();
            return true;
        }
        return false;
    }

    // Gets a token by its string value
    public EventToken getToken(String tokenString) {
        return issuedTokens.get(tokenString);
    }
}
