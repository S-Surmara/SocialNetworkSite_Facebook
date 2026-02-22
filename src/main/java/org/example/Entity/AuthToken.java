package org.example.Entity;

import java.time.LocalDateTime;

public class AuthToken {
    private final String tokenId;
    private final String userId;
    private final LocalDateTime expiresAt;

    public AuthToken(String tokenId, String userId, LocalDateTime expiresAt) {
        this.tokenId   = tokenId;
        this.userId    = userId;
        this.expiresAt = expiresAt;
    }

    public String getTokenId()   { return tokenId; }
    public String  getUserId()    { return userId; }
    public LocalDateTime getExpiresAt() { return expiresAt; }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiresAt);
    }
}
