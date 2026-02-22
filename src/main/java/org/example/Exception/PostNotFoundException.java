package org.example.Exception;

public class PostNotFoundException extends RuntimeException {
    public PostNotFoundException(String postId) { super("Post not found: " + postId); }
}
