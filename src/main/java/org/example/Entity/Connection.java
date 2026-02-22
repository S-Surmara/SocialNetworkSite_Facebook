package org.example.Entity;

import org.example.Enums.ConnectionStatus;

public class Connection {
    private final String fromId;
    private final String toId;
    private ConnectionStatus status;

    public Connection(String fromId, String toId) {
        this.fromId = fromId;
        this.toId = toId;
        this.status = ConnectionStatus.PENDING;
    }

    public String getFromId() {
        return fromId;
    }

    public String getToId() {
        return toId;
    }

    public ConnectionStatus getStatus() {
        return status;
    }

    public void setStatus(ConnectionStatus status) {
        this.status = status;
    }
}

