package com.socialnetwork.socialnetworkjavaspring.controllers;

import com.socialnetwork.socialnetworkjavaspring.models.Post;
import com.socialnetwork.socialnetworkjavaspring.models.User;
import com.socialnetwork.socialnetworkjavaspring.services.posts.IPostServices;
import com.socialnetwork.socialnetworkjavaspring.services.sessions.SessionService;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@RequestMapping("/new-feed")
public class NewFeedController {
    @Autowired
    private SessionService sessionService;
    @Autowired
    private IPostServices postServices;

    @GetMapping
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("new_feeds/index");
        User user = sessionService.currentUser();
        List<Post> posts = postServices.findAllPostForNewsFeed(user.getUserId());

        modelAndView.addObject("posts", posts);
        return modelAndView;
    }
}
