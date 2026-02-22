package org.example.Entity;

import org.example.Enums.PostVisibility;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Post {
    private final String postId;
    private final String userId;
    private final LocalDateTime timestamp;
    private final String img;
    private final String videoUrl;
    private final String description;
    private final PostVisibility visibility;
    private final Set<String> likedByUserIds; // prevents duplicate likes
    private final List<Comment> comments;

    // Private constructor — use Builder
    private Post(Builder builder) {
        this.postId = builder.postId;
        this.userId = builder.userId;
        this.timestamp = builder.timestamp;
        this.img = builder.img;
        this.videoUrl = builder.videoUrl;
        this.description = builder.description;
        this.visibility = builder.visibility;
        this.likedByUserIds = new HashSet<>();
        this.comments = new ArrayList<>();
    }

    public String getPostId() {
        return postId;
    }

    public String getUserId() {
        return userId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getImg() {
        return img;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public String getDescription() {
        return description;
    }

    public PostVisibility getVisibility() {
        return visibility;
    }

    public Set<String> getLikedByUserIds() {
        return likedByUserIds;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public int getLikeCount() {
        return likedByUserIds.size();
    }

    public boolean addLike(String userId) {
        return likedByUserIds.add(userId); // returns false if already liked
    }

    public void addComment(Comment comment) {
        comments.add(comment);
    }

    // ── Builder ──────────────────────────────────────
    public static class Builder {
        private final String postId;
        private final String userId;
        private final LocalDateTime timestamp;
        private String img = null;
        private String videoUrl = null;
        private String description = "";
        private PostVisibility visibility = PostVisibility.PUBLIC;

        public Builder(String postId, String userId) {
            this.postId = postId;
            this.userId = userId;
            this.timestamp = LocalDateTime.now();
        }

        public Builder img(String img) {
            this.img = img;
            return this;
        }

        public Builder videoUrl(String videoUrl) {
            this.videoUrl = videoUrl;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder visibility(PostVisibility v) {
            this.visibility = v;
            return this;
        }

        public Post build() {
            return new Post(this);
        }
    }
}

