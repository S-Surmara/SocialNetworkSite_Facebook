package org.example.strategy;


import org.example.Entity.Post;

import java.util.List;

public interface FeedStrategy {
    List<Post> sort(List<Post> posts);
}

