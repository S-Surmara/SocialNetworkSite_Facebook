package org.example.Persistance;

import org.example.Entity.Post;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class PostRepository {
    private final ConcurrentHashMap<String, Post> postMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, List<Post>> userPostMap = new ConcurrentHashMap<>();

    public void save(Post post) {
        postMap.put(post.getPostId(), post);
        userPostMap.computeIfAbsent(post.getUserId(), k -> new ArrayList<>()).add(post);
    }

    public Optional<Post> findById(String postId) {
        return Optional.ofNullable(postMap.get(postId));
    }

    public List<Post> findByUserId(String userId) {
        return userPostMap.getOrDefault(userId, new ArrayList<>());
    }

    public void remove(String postId) {
        Post post = postMap.remove(postId);
        if (post != null) {
            List<Post> userPosts = userPostMap.get(post.getUserId());
            if (userPosts != null) userPosts.remove(post);
        }
    }
}

