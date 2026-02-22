package org.example.Persistance;

import org.example.Entity.Notification;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class NotificationRepository {
    private final ConcurrentHashMap<String, List<Notification>> userNotifMap = new ConcurrentHashMap<>();

    public void save(Notification notification) {
        userNotifMap.computeIfAbsent(notification.getToUserId(), k -> new ArrayList<>())
                .add(notification);
    }

    public List<Notification> findByUserId(String userId) {
        return userNotifMap.getOrDefault(userId, new ArrayList<>());
    }

    public List<Notification> findUnreadByUserId(String userId) {
        return findByUserId(userId).stream()
                .filter(n -> !n.isRead())
                .collect(Collectors.toList());
    }
}

