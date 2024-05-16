package com.socialnetwork.socialnetworkjavaspring.services.new_feeds;

import com.socialnetwork.socialnetworkjavaspring.models.Post;
import com.socialnetwork.socialnetworkjavaspring.repositories.IPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewFeedServices {
    @Autowired
    private IPostRepository postRepository;

    public List<Post> findAllPostForNewsFeed(String userId){
        return postRepository.findAllPostForNewsFeed(userId);
    }
}
