package org.example.strategy;

import org.example.Entity.Post;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class PopularityStrategy implements FeedStrategy {
    @Override
    public List<Post> sort(List<Post> posts) {
        return posts.stream()
                .sorted(Comparator.comparingInt(Post::getLikeCount).reversed())
                .collect(Collectors.toList());
    }
}

