package org.example;

import org.example.Entity.Comment;
import org.example.Entity.Post;
import org.example.Entity.Profile;
import org.example.Entity.User;
import org.example.Enums.PostVisibility;
import org.example.strategy.ChronologicalStrategy;
import org.example.strategy.PopularityStrategy;
import org.example.Entity.AuthToken;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        SocialNetworkManager snm = SocialNetworkManager.getInstance();

        // ── Sign Up ───────────────────────────────────────
        Profile arjunProfile = new Profile("arjun.jpg", "Java Dev @ OpenText", List.of("Coding", "Music"));
        User arjun = new User("U001", "arjun@email.com", "arjun", "hash123", arjunProfile);

        Profile priyaProfile = new Profile("priya.jpg", "Designer", List.of("Art", "Travel"));
        User priya = new User("U002", "priya@email.com", "priya", "hash456", priyaProfile);

        snm.signUp(arjun);
        snm.signUp(priya);

        // ── Login ─────────────────────────────────────────
        AuthToken arjunToken = snm.login("arjun", "hash123");
        System.out.println("Logged in. Token: " + arjunToken.getTokenId());

        // ── Token Validation ──────────────────────────────
        String userId = snm.validateToken(arjunToken.getTokenId());
        System.out.println("Token valid for userId: " + userId);

        // ── Friend Request ────────────────────────────────
        snm.sendRequest("U001", "U002");   // Arjun → Priya
        snm.acceptRequest("U001", "U002"); // Priya accepts

        System.out.println("Friends of Arjun: " + snm.getFriends("U001"));

        // ── Create Posts ──────────────────────────────────
        Post post1 = new Post.Builder("P001", "U001")
                .description("Just finished my LLD design!")
                .visibility(PostVisibility.PUBLIC)
                .build();

        Post post2 = new Post.Builder("P002", "U002")
                .description("Beautiful sunset today 🌅")
                .img("sunset.jpg")
                .visibility(PostVisibility.FRIENDS_ONLY)
                .build();

        snm.createPost(post1);
        snm.createPost(post2);

        // ── Like & Comment ────────────────────────────────
        snm.likePost("P001", "U002");
        snm.likePost("P001", "U002"); // duplicate — ignored

        Comment comment = new Comment("C001", "U002", "Great work Arjun!");
        snm.commentPost("P001", comment);

        // ── News Feed ─────────────────────────────────────
        System.out.println("\n── Chronological Feed for Arjun ──");
        snm.getNewsFeed("U001", new ChronologicalStrategy())
                .forEach(p -> System.out.println(p.getDescription() + " | Likes: " + p.getLikeCount()));

        System.out.println("\n── Popularity Feed for Arjun ──");
        snm.getNewsFeed("U001", new PopularityStrategy())
                .forEach(p -> System.out.println(p.getDescription() + " | Likes: " + p.getLikeCount()));

        // ── Notifications ─────────────────────────────────
        System.out.println("\n── Notifications for Arjun ──");
        snm.getNotifications("U001")
                .forEach(n -> System.out.println("[" + n.getType() + "] " + n.getMessage()));

        // ── Logout ────────────────────────────────────────
        snm.logout(arjunToken.getTokenId());
        System.out.println("\nLogged out successfully.");
    }
}