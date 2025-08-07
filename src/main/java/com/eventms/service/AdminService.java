package com.eventms.service;

import com.eventms.model.EventToken;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Service for Admin-specific actions. Admins can generate event creation tokens for users.
 */
public class AdminService {
    private final Map<String, EventToken> issuedTokens = new HashMap<>();
    private int nextTokenId = 1;

    /**
     * Generates a unique event creation token. Only Admins should call this.
     * @param adminId The ID of the admin creating the token
     * @return the generated EventToken
     */
    public EventToken generateEventToken(int adminId) {
        String tokenString = "TOKEN" + UUID.randomUUID().toString().replace("-", "").substring(0, 10);
        EventToken token = new EventToken(nextTokenId++, tokenString, adminId);
        issuedTokens.put(tokenString, token);
        System.out.println("Event creation token generated: " + tokenString + " by admin " + adminId);
        return token;
    }

    /**
     * Checks if a token was issued and is valid.
     */
    public boolean isValidToken(String tokenString) {
        EventToken token = issuedTokens.get(tokenString);
        return token != null; // Tokens can be reused multiple times
    }

    /**
     * Increments token usage count (tokens can be reused).
     */
    public boolean useToken(String tokenString) {
        EventToken token = issuedTokens.get(tokenString);
        if (token != null) {
            token.incrementUseCount();
            return true;
        }
        return false;
    }

    /**
     * Gets a token by its string value.
     */
    public EventToken getToken(String tokenString) {
        return issuedTokens.get(tokenString);
    }
}
