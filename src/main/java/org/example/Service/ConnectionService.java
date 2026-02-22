package org.example.Service;

import org.example.Entity.Connection;
import org.example.Enums.ConnectionStatus;
import org.example.Enums.NotificationType;
import org.example.Exception.ConnectionNotFoundException;
import org.example.Persistance.ConnectionRepository;

import java.util.List;

public class ConnectionService {
    private final ConnectionRepository connectionRepository;
    private final NotificationService  notificationService;

    public ConnectionService(ConnectionRepository connectionRepository,
                             NotificationService notificationService) {
        this.connectionRepository = connectionRepository;
        this.notificationService  = notificationService;
    }

    public void sendRequest(String fromId, String toId) {
        Connection connection = new Connection(fromId, toId);
        connectionRepository.save(connection);

        notificationService.notify(
                toId,
                NotificationType.FRIEND_REQUEST_RECEIVED,
                fromId,
                "You have a new friend request from user: " + fromId
        );
    }

    public void acceptRequest(String fromId, String toId) {
        Connection connection = connectionRepository.find(fromId, toId)
                .orElseThrow(() -> new ConnectionNotFoundException(fromId + " → " + toId));

        connection.setStatus(ConnectionStatus.ACCEPTED);
        connectionRepository.save(connection);

        notificationService.notify(
                fromId,
                NotificationType.FRIEND_REQUEST_ACCEPTED,
                toId,
                "User " + toId + " accepted your friend request."
        );
    }

    public void rejectRequest(String fromId, String toId) {
        Connection connection = connectionRepository.find(fromId, toId)
                .orElseThrow(() -> new ConnectionNotFoundException(fromId + " → " + toId));

        connection.setStatus(ConnectionStatus.REJECTED);
        connectionRepository.save(connection);
    }

    public List<String> getFriends(String userId) {
        return connectionRepository.findFriends(userId);
    }
}

