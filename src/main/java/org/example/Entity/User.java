package org.example.Entity;

public class User {
    private final String userId;
    private final String email;
    private final String username;
    private String passwordHash;
    private Profile profile;

    public User(String userId, String email, String username, String passwordHash, Profile profile) {
        this.userId = userId;
        this.email = email;
        this.username = username;
        this.passwordHash = passwordHash;
        this.profile = profile;
    }

    public String getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }
}
