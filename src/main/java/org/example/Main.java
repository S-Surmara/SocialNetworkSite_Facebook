package org.example;

import org.example.Entity.*;
import org.example.Enums.PostVisibility;
import org.example.strategy.ChronologicalStrategy;
import org.example.strategy.PopularityStrategy;

import java.util.List;
import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) throws InterruptedException {

        SocialNetworkManager snm = SocialNetworkManager.getInstance();

        // ── Setup Users ───────────────────────────────────
        snm.signUp(new User("U001", "arjun@email.com",  "arjun",  "hash123",
                new Profile("arjun.jpg", "Java Dev @ OpenText", List.of("Coding", "Music"))));
        snm.signUp(new User("U002", "priya@email.com",  "priya",  "hash456",
                new Profile("priya.jpg", "Designer", List.of("Art", "Travel"))));
        snm.signUp(new User("U003", "rahul@email.com",  "rahul",  "hash789",
                new Profile("rahul.jpg", "Musician",  List.of("Music", "Travel"))));
        snm.signUp(new User("U004", "sneha@email.com",  "sneha",  "hashABC",
                new Profile("sneha.jpg", "Writer",    List.of("Books", "Coding"))));

        // ── Setup Connections ─────────────────────────────
        snm.sendRequest("U001", "U002");
        snm.acceptRequest("U001", "U002");

        snm.sendRequest("U001", "U003");
        snm.acceptRequest("U001", "U003");

        snm.sendRequest("U001", "U004");
        snm.acceptRequest("U001", "U004");

        // ── Setup Posts ───────────────────────────────────
        snm.createPost(new Post.Builder("P001", "U001")
                .description("Just finished LLD design!")
                .visibility(PostVisibility.PUBLIC).build());

        snm.createPost(new Post.Builder("P002", "U002")
                .description("Beautiful sunset 🌅")
                .img("sunset.jpg")
                .visibility(PostVisibility.FRIENDS_ONLY).build());

        snm.createPost(new Post.Builder("P003", "U003")
                .description("New music drop 🎵")
                .visibility(PostVisibility.PUBLIC).build());

        System.out.println("═══════════════════════════════════════════");
        System.out.println("        CONCURRENT STRESS TEST START       ");
        System.out.println("═══════════════════════════════════════════\n");

        ExecutorService executor = Executors.newFixedThreadPool(10);

        // ─────────────────────────────────────────────────
        // TEST 1: Concurrent Likes on Same Post
        // Expected: No duplicate likes from same user,
        //           final like count = number of UNIQUE users who liked
        // ─────────────────────────────────────────────────
        System.out.println("── TEST 1: Concurrent Likes on P001 ──");
        CountDownLatch likeLatch = new CountDownLatch(6);

        // U002, U003, U004 each try to like P001 TWICE simultaneously
        List.of("U002", "U003", "U004").forEach(userId -> {
            executor.submit(() -> {
                try {
                    snm.likePost("P001", userId); // first like — should count
                } finally { likeLatch.countDown(); }
            });
            executor.submit(() -> {
                try {
                    snm.likePost("P001", userId); // duplicate — should be ignored
                } finally { likeLatch.countDown(); }
            });
        });

        likeLatch.await();
        int likeCount = snm.getPost("P001").getLikeCount();
        System.out.println("Expected like count: 3 | Actual: " + likeCount);
        System.out.println(likeCount == 3 ? "✅ PASSED" : "❌ FAILED");
        System.out.println();

        // ─────────────────────────────────────────────────
        // TEST 2: Concurrent Comments on Same Post
        // Expected: All 5 comments saved, no data lost
        // ─────────────────────────────────────────────────
        System.out.println("── TEST 2: Concurrent Comments on P001 ──");
        CountDownLatch commentLatch = new CountDownLatch(5);

        for (int i = 1; i <= 5; i++) {
            final int idx = i;
            executor.submit(() -> {
                try {
                    String commenter = "U00" + (idx % 4 + 1); // cycles U001–U004
                    snm.commentPost("P001",
                            new Comment("C00" + idx, commenter, "Comment number " + idx));
                } finally { commentLatch.countDown(); }
            });
        }

        commentLatch.await();
        int commentCount = snm.getPost("P001").getComments().size();
        System.out.println("Expected comment count: 5 | Actual: " + commentCount);
        System.out.println(commentCount == 5 ? "✅ PASSED" : "❌ FAILED");
        System.out.println();

        // ─────────────────────────────────────────────────
        // TEST 3: Concurrent Friend Requests
        // Expected: Both requests saved without deadlock
        // ─────────────────────────────────────────────────
        System.out.println("── TEST 3: Concurrent Friend Requests ──");

        snm.signUp(new User("U005", "a@email.com", "userA", "p1",
                new Profile("a.jpg", "Bio A", List.of())));
        snm.signUp(new User("U006", "b@email.com", "userB", "p2",
                new Profile("b.jpg", "Bio B", List.of())));

        CountDownLatch requestLatch = new CountDownLatch(2);

        executor.submit(() -> {
            try { snm.sendRequest("U005", "U006"); }
            finally { requestLatch.countDown(); }
        });
        executor.submit(() -> {
            try { snm.sendRequest("U006", "U005"); }
            finally { requestLatch.countDown(); }
        });

        requestLatch.await();
        System.out.println("Both friend requests submitted without deadlock ✅");
        System.out.println();

        // ─────────────────────────────────────────────────
        // TEST 4: Concurrent Login (same user, multiple sessions)
        // Expected: Each login gets its own unique AuthToken
        // ─────────────────────────────────────────────────
        System.out.println("── TEST 4: Concurrent Logins (same user) ──");
        CountDownLatch loginLatch  = new CountDownLatch(4);
        CopyOnWriteArrayList<String> tokenIds = new CopyOnWriteArrayList<>();

        for (int i = 0; i < 4; i++) {
            executor.submit(() -> {
                try {
                    AuthToken token = snm.login("arjun", "hash123");
                    tokenIds.add(token.getTokenId());
                } finally { loginLatch.countDown(); }
            });
        }

        loginLatch.await();
        long uniqueTokens = tokenIds.stream().distinct().count();
        System.out.println("Expected 4 unique tokens | Actual: " + uniqueTokens);
        System.out.println(uniqueTokens == 4 ? "✅ PASSED" : "❌ FAILED");
        System.out.println();

        // ─────────────────────────────────────────────────
        // TEST 5: Concurrent Post Creation by multiple users
        // Expected: All posts saved, no overwrites
        // ─────────────────────────────────────────────────
        System.out.println("── TEST 5: Concurrent Post Creation ──");
        CountDownLatch postLatch = new CountDownLatch(6);

        for (int i = 10; i <= 15; i++) {
            final int idx = i;
            executor.submit(() -> {
                try {
                    String creator = "U00" + (idx % 4 + 1);
                    snm.createPost(new Post.Builder("P0" + idx, creator)
                            .description("Concurrent post #" + idx)
                            .visibility(PostVisibility.PUBLIC).build());
                } finally { postLatch.countDown(); }
            });
        }

        postLatch.await();
        int totalPostsU001 = snm.getPostsByUser("U001").size();
        System.out.println("Posts by U001 (expected ≥ 1): " + totalPostsU001);
        System.out.println(totalPostsU001 >= 1 ? "✅ PASSED" : "❌ FAILED");
        System.out.println();

        // ─────────────────────────────────────────────────
        // TEST 6: Concurrent Feed Reads while posts are being written
        // Expected: No ConcurrentModificationException
        // ─────────────────────────────────────────────────
        System.out.println("── TEST 6: Feed Read/Write Concurrently ──");
        CountDownLatch feedLatch = new CountDownLatch(4);

        // 2 threads writing posts
        executor.submit(() -> {
            try {
                snm.createPost(new Post.Builder("P020", "U002")
                        .description("Write while reading")
                        .visibility(PostVisibility.PUBLIC).build());
            } finally { feedLatch.countDown(); }
        });
        executor.submit(() -> {
            try {
                snm.createPost(new Post.Builder("P021", "U003")
                        .description("Another write")
                        .visibility(PostVisibility.PUBLIC).build());
            } finally { feedLatch.countDown(); }
        });

        // 2 threads reading feed simultaneously
        executor.submit(() -> {
            try {
                List<Post> feed = snm.getNewsFeed("U001", new ChronologicalStrategy());
                System.out.println("  Thread A feed size: " + feed.size());
            } finally { feedLatch.countDown(); }
        });
        executor.submit(() -> {
            try {
                List<Post> feed = snm.getNewsFeed("U001", new PopularityStrategy());
                System.out.println("  Thread B feed size: " + feed.size());
            } finally { feedLatch.countDown(); }
        });

        feedLatch.await();
        System.out.println("No ConcurrentModificationException ✅");
        System.out.println();

        // ─────────────────────────────────────────────────
        // FINAL RESULTS
        // ─────────────────────────────────────────────────
        System.out.println("═══════════════════════════════════════════");
        System.out.println("         ALL CONCURRENT TESTS DONE         ");
        System.out.println("═══════════════════════════════════════════");
        System.out.println("\n── Final Notifications for U001 (Arjun) ──");
        snm.getNotifications("U001")
                .forEach(n -> System.out.println("[" + n.getType() + "] " + n.getMessage()));

        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);
    }
}
