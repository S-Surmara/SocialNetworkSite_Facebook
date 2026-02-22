package org.example;


import org.example.Entity.*;
import org.example.Observer.EmailNotificationObserver;
import org.example.Observer.InAppNotificationObserver;
import org.example.Persistance.ConnectionRepository;
import org.example.Persistance.NotificationRepository;
import org.example.Persistance.PostRepository;
import org.example.Persistance.UserRepository;
import org.example.Service.*;
import org.example.strategy.FeedStrategy;

import java.util.List;

public class SocialNetworkManager {

    private static volatile SocialNetworkManager instance;

    private final AuthenticationService authenticationService;
    private final ConnectionService connectionService;
    private final PostService postService;
    private final NewsFeedService newsFeedService;
    private final NotificationService notificationService;
    private final NotificationRepository notificationRepository;

    private SocialNetworkManager() {
        UserRepository userRepo = new UserRepository();
        PostRepository postRepo = new PostRepository();
        ConnectionRepository connRepo = new ConnectionRepository();
        notificationRepository = new NotificationRepository();

        notificationService = new NotificationService();
        notificationService.addObserver(new InAppNotificationObserver(notificationRepository));
        notificationService.addObserver(new EmailNotificationObserver());

        authenticationService = new AuthenticationService(userRepo);
        connectionService = new ConnectionService(connRepo, notificationService);
        postService = new PostService(postRepo, notificationService);
        newsFeedService = new NewsFeedService(postRepo, connectionService);
    }

    public static SocialNetworkManager getInstance() {
        if (instance == null) {
            synchronized (SocialNetworkManager.class) {
                if (instance == null) instance = new SocialNetworkManager();
            }
        }
        return instance;
    }

    // ── Auth ──────────────────────────────────────────────
    public void signUp(User user) {
        authenticationService.signUp(user);
    }

    public AuthToken login(String username, String passwordHash) {
        return authenticationService.login(username, passwordHash);
    }

    public void logout(String tokenId) {
        authenticationService.logout(tokenId);
    }

    public String validateToken(String tokenId) {
        return authenticationService.validateToken(tokenId);
    }

    // ── Connections ───────────────────────────────────────
    public void sendRequest(String fromId, String toId) {
        connectionService.sendRequest(fromId, toId);
    }

    public void acceptRequest(String fromId, String toId) {
        connectionService.acceptRequest(fromId, toId);
    }

    public void rejectRequest(String fromId, String toId) {
        connectionService.rejectRequest(fromId, toId);
    }

    public List<String> getFriends(String userId) {
        return connectionService.getFriends(userId);
    }

    // ── Posts ─────────────────────────────────────────────
    public void createPost(Post post) {
        postService.createPost(post);
    }

    public void deletePost(String postId) {
        postService.deletePost(postId);
    }

    public Post getPost(String postId) { return postService.getPost(postId); }

    public List<Post> getPostsByUser(String userId) { return postService.getPostsByUser(userId); }

    public void likePost(String postId, String likerUserId) {
        postService.likePost(postId, likerUserId);
    }

    public void commentPost(String postId, Comment comment) {
        postService.commentPost(postId, comment);
    }

    // ── Feed ──────────────────────────────────────────────
    public List<Post> getNewsFeed(String userId, FeedStrategy strategy) {
        return newsFeedService.getNewsFeed(userId, strategy);
    }

    // ── Notifications ─────────────────────────────────────
    public List<Notification> getNotifications(String userId) {
        return notificationRepository.findByUserId(userId);
    }

    public List<Notification> getUnreadNotifications(String userId) {
        return notificationRepository.findUnreadByUserId(userId);
    }
}
