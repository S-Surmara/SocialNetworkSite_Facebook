package org.example.Observer;

import org.example.Entity.Notification;

public interface NotificationObserver {
    void onNotify(Notification notification);
}
