package org.example.Entity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class Comment {
    private final String        commentId;
    private final String        userId;
    private final String        description;
    private final LocalDateTime timestamp;
    private final Set<String>   likedByUserIds; // consistent with Post

    public Comment(String commentId, String userId, String description) {
        this.commentId      = commentId;
        this.userId         = userId;
        this.description    = description;
        this.timestamp      = LocalDateTime.now();
        this.likedByUserIds = new HashSet<>();
    }

    public String        getCommentId()   { return commentId; }
    public String        getUserId()      { return userId; }
    public String        getDescription() { return description; }
    public LocalDateTime getTimestamp()   { return timestamp; }
    public int           getLikeCount()   { return likedByUserIds.size(); }

    public boolean addLike(String userId) { return likedByUserIds.add(userId); }
}

