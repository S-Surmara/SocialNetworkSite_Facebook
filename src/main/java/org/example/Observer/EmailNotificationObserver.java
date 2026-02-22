package org.example.Observer;

import org.example.Entity.Notification;

public class EmailNotificationObserver implements NotificationObserver {
    @Override
    public void onNotify(Notification notification) {
        // In production: send via email gateway (SendGrid/SES)
        System.out.println("[EMAIL] " + notification.getMessage()
                + " → User: " + notification.getToUserId());
    }
}

