package org.example.Observer;


import org.example.Entity.Notification;
import org.example.Persistance.NotificationRepository;

public class InAppNotificationObserver implements NotificationObserver {
    private final NotificationRepository notificationRepository;

    public InAppNotificationObserver(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    public void onNotify(Notification notification) {
        notificationRepository.save(notification);
        System.out.println("[IN-APP] " + notification.getMessage()
                + " → User: " + notification.getToUserId());
    }
}

