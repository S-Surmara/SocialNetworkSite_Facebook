package org.example.Service;

import org.example.Entity.Post;
import org.example.Enums.NotificationType;
import org.example.Exception.PostNotFoundException;
import org.example.Persistance.PostRepository;
import org.example.Entity.Comment;

import java.util.List;

public class PostService {
    private final PostRepository postRepository;
    private final NotificationService notificationService;

    public PostService(PostRepository postRepository,
                       NotificationService notificationService) {
        this.postRepository = postRepository;
        this.notificationService = notificationService;
    }

    public void createPost(Post post) {
        postRepository.save(post);
    }

    public void deletePost(String postId) {
        postRepository.remove(postId);
    }

    public void likePost(String postId, String likerUserId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));

        boolean added = post.addLike(likerUserId);
        if (!added) return; // already liked — idempotent, no duplicate notification

        // Notify post owner only if someone else liked it
        if (!post.getUserId().equals(likerUserId)) {
            notificationService.notify(
                    post.getUserId(),
                    NotificationType.POST_LIKED,
                    postId,
                    "User " + likerUserId + " liked your post."
            );
        }
    }

    public void commentPost(String postId, Comment comment) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));

        post.addComment(comment);

        if (!post.getUserId().equals(comment.getUserId())) {
            notificationService.notify(
                    post.getUserId(),
                    NotificationType.POST_COMMENTED,
                    postId,
                    "User " + comment.getUserId() + " commented on your post."
            );
        }
    }

    public Post getPost(String postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));
    }

    public List<Post> getPostsByUser(String userId) {
        return postRepository.findByUserId(userId);
    }
}

