package com.eventms.model;

// Represents an event creation token issued by admins
public class EventToken implements Displayable {
    private int tokenId;
    private String tokenString;
    private boolean isUsed;
    private String createdAt;
    private int createdBy;
    private int useCount;

    public EventToken(int tokenId, String tokenString, int createdBy) {
        if (tokenId <= 0) {
            throw new IllegalArgumentException("Token ID must be positive");
        }
        if (tokenString == null || tokenString.trim().isEmpty()) {
            throw new IllegalArgumentException("Token string cannot be empty");
        }
        if (createdBy <= 0) {
            throw new IllegalArgumentException("Created by ID must be positive");
        }
        
        this.tokenId = tokenId;
        this.tokenString = tokenString.trim();
        this.isUsed = false;
        this.createdAt = java.time.LocalDateTime.now().toString();
        this.createdBy = createdBy;
        this.useCount = 0;
    }

    // Getters
    public int getTokenId() { return tokenId; }
    public String getTokenString() { return tokenString; }
    public boolean isUsed() { return isUsed; }
    public String getCreatedAt() { return createdAt; }
    public int getCreatedBy() { return createdBy; }
    public int getUseCount() { return useCount; }

    // Setters
    public void setUsed(boolean used) { this.isUsed = used; }
    public void incrementUseCount() { this.useCount++; }

    @Override
    public void displayInfo() {
        System.out.println("[TOKEN DISPLAY]");
        System.out.println("Token ID: " + tokenId);
        System.out.println("Token String: " + tokenString);
        System.out.println("Is Used: " + isUsed);
        System.out.println("Created At: " + createdAt);
        System.out.println("Created By: " + createdBy);
        System.out.println("Use Count: " + useCount);
    }

    @Override
    public String toString() {
        return "EventToken{" +
                "tokenId=" + tokenId +
                ", tokenString='" + tokenString + '\'' +
                ", isUsed=" + isUsed +
                ", useCount=" + useCount +
                '}';
    }
}
