package com.eventms.model;

// Enhanced enum for user roles with admin levels
public enum Role {
    USER(0, "Regular User"),
    ADMIN_STANDARD(1, "Standard Administrator"),
    ADMIN_SENIOR(2, "Senior Administrator"),
    ADMIN_SUPER(3, "Super Administrator");

    private final int accessLevel;
    private final String displayName;

    Role(int accessLevel, String displayName) {
        this.accessLevel = accessLevel;
        this.displayName = displayName;
    }

    public int getAccessLevel() {
        return accessLevel;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean isAdmin() {
        return this != USER;
    }

    public boolean canPromoteUsers() {
        return this == ADMIN_SENIOR || this == ADMIN_SUPER;
    }

    public static Role getAdminRole(String adminLevel) {
        switch (adminLevel.toLowerCase().trim()) {
            case "standard":
                return ADMIN_STANDARD;
            case "senior":
                return ADMIN_SENIOR;
            case "super":
                return ADMIN_SUPER;
            default:
                throw new IllegalArgumentException("Invalid admin level: " + adminLevel);
        }
    }
}