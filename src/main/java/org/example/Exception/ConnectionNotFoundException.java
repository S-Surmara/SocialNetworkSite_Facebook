package org.example.Exception;

public class ConnectionNotFoundException extends RuntimeException {
    public ConnectionNotFoundException(String msg) { super("Connection not found: " + msg); }
}
