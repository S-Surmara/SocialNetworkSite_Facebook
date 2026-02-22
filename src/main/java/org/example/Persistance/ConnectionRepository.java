package org.example.Persistance;

import org.example.Entity.Connection;
import org.example.Enums.ConnectionStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class ConnectionRepository {
    private final ConcurrentHashMap<String, Connection>       connectionMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, List<Connection>> fromMap       = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, List<Connection>> toMap         = new ConcurrentHashMap<>();

    public void save(Connection connection) {
        String key = buildKey(connection.getFromId(), connection.getToId());
        connectionMap.put(key, connection);
        fromMap.computeIfAbsent(connection.getFromId(), k -> new ArrayList<>()).add(connection);
        toMap.computeIfAbsent(connection.getToId(),   k -> new ArrayList<>()).add(connection);
    }

    public Optional<Connection> find(String fromId, String toId) {
        return Optional.ofNullable(connectionMap.get(buildKey(fromId, toId)));
    }

    public List<String> findFriends(String userId) {
        List<String> friends = new ArrayList<>();
        fromMap.getOrDefault(userId, new ArrayList<>()).stream()
                .filter(c -> c.getStatus() == ConnectionStatus.ACCEPTED)
                .forEach(c -> friends.add(c.getToId()));
        toMap.getOrDefault(userId, new ArrayList<>()).stream()
                .filter(c -> c.getStatus() == ConnectionStatus.ACCEPTED)
                .forEach(c -> friends.add(c.getFromId()));
        return friends;
    }

    private String buildKey(String fromId, String toId) { return fromId + ":" + toId; }
}

