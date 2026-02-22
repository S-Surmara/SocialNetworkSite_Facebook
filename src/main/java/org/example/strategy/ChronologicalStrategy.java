package org.example.strategy;

import org.example.Entity.Post;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ChronologicalStrategy implements FeedStrategy {
    @Override
    public List<Post> sort(List<Post> posts) {
        return posts.stream()
                .sorted(Comparator.comparing(Post::getTimestamp).reversed())
                .collect(Collectors.toList());
    }
}

