package org.example.Service;

import org.example.Entity.Post;
import org.example.Enums.PostVisibility;
import org.example.Persistance.PostRepository;
import org.example.strategy.FeedStrategy;

import java.util.ArrayList;
import java.util.List;

public class NewsFeedService {
    private final PostRepository postRepository;
    private final ConnectionService connectionService;

    public NewsFeedService(PostRepository postRepository,
                           ConnectionService connectionService) {
        this.postRepository = postRepository;
        this.connectionService = connectionService;
    }

    public List<Post> getNewsFeed(String userId, FeedStrategy feedStrategy) {
        List<Post> feed = new ArrayList<>();

        // Own posts
        feed.addAll(postRepository.findByUserId(userId));

        // Friends' posts filtered by visibility
        List<String> friends = connectionService.getFriends(userId);
        friends.forEach(friendId ->
                postRepository.findByUserId(friendId).stream()
                        .filter(post -> post.getVisibility() != PostVisibility.PRIVATE)
                        .forEach(feed::add)
        );

        return feedStrategy.sort(feed);
    }
}

