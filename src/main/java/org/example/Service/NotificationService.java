package org.example.Service;

import org.example.Entity.Notification;
import org.example.Enums.NotificationType;
import org.example.Observer.NotificationObserver;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NotificationService {
    private final List<NotificationObserver> observers = new ArrayList<>();

    public void addObserver(NotificationObserver observer) { observers.add(observer); }
    public void removeObserver(NotificationObserver observer) { observers.remove(observer); }

    public void notify(String toUserId, NotificationType type,
                       String referenceId, String message) {
        Notification notification = new Notification(
                UUID.randomUUID().toString(), toUserId, type, referenceId, message
        );
        observers.forEach(o -> o.onNotify(notification));
    }
}

