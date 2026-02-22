package org.example.Entity;

import org.example.Enums.NotificationType;

import java.time.LocalDateTime;

public class Notification {
    private final String notificationId;
    private final String toUserId;
    private final NotificationType type;
    private final String referenceId; // postId or connectionId
    private final String message;
    private boolean isRead;
    private final LocalDateTime createdAt;

    public Notification(String notificationId, String toUserId,
                        NotificationType type, String referenceId, String message) {
        this.notificationId = notificationId;
        this.toUserId = toUserId;
        this.type = type;
        this.referenceId = referenceId;
        this.message = message;
        this.isRead = false;
        this.createdAt = LocalDateTime.now();
    }

    public String getNotificationId() {
        return notificationId;
    }

    public String getToUserId() {
        return toUserId;
    }

    public NotificationType getType() {
        return type;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public String getMessage() {
        return message;
    }

    public boolean isRead() {
        return isRead;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void markRead() {
        this.isRead = true;
    }
}

