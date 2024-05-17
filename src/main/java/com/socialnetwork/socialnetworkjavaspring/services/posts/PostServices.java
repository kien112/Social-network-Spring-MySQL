package com.socialnetwork.socialnetworkjavaspring.services.posts;

import com.socialnetwork.socialnetworkjavaspring.models.Post;
import com.socialnetwork.socialnetworkjavaspring.repositories.IPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostServices implements IPostServices{
    @Autowired
    private IPostRepository postRepository;
    public List<Post> findAllPostForNewsFeed(String userId) {
        return postRepository.findAllPostForNewsFeed(userId);
    }

    @Override
    public Optional<Post> save(Post object) {
        return Optional.empty();
    }

    @Override
    public Optional<Post> delete(Post object) {
        return Optional.empty();
    }
}
