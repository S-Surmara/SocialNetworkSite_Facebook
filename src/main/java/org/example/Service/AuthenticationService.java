package org.example.Service;

import org.example.Entity.AuthToken;
import org.example.Entity.User;
import org.example.Exception.UnauthorizedException;
import org.example.Exception.UserNotFoundException;
import org.example.Persistance.UserRepository;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class AuthenticationService {
    private final UserRepository userRepository;

    // Active sessions: tokenId → AuthToken
    private final ConcurrentHashMap<String, AuthToken> activeSessions = new ConcurrentHashMap<>();

    public AuthenticationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void signUp(User user) {
        userRepository.save(user);
    }

    public AuthToken login(String username, String passwordHash) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        if (!user.getPasswordHash().equals(passwordHash)) {
            throw new UnauthorizedException("Invalid credentials.");
        }

        AuthToken token = new AuthToken(
                UUID.randomUUID().toString(),
                user.getUserId(),
                LocalDateTime.now().plusHours(24)
        );
        activeSessions.put(token.getTokenId(), token);
        return token;
    }

    public void logout(String tokenId) {
        activeSessions.remove(tokenId);
    }

    public String validateToken(String tokenId) {
        AuthToken token = activeSessions.get(tokenId);
        if (token == null || token.isExpired()) {
            activeSessions.remove(tokenId);
            throw new UnauthorizedException("Session expired. Please login again.");
        }
        return token.getUserId();
    }
}

