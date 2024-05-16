package com.socialnetwork.socialnetworkjavaspring.controllers;

import com.socialnetwork.socialnetworkjavaspring.models.User;
import com.socialnetwork.socialnetworkjavaspring.services.new_feeds.NewFeedServices;
import com.socialnetwork.socialnetworkjavaspring.services.sessions.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/new-feed")
public class NewFeedController {
    @Autowired
    private SessionService sessionService;
    @Autowired
    private NewFeedServices newFeedServices;

    @GetMapping("/index")
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("new_feeds/index");
        User user = sessionService.currentUser();
        modelAndView.addObject("posts", newFeedServices.findAllPostForNewsFeed(user.getUserId()));

        return modelAndView;
    }
}
