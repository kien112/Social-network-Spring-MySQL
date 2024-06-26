package com.socialnetwork.socialnetworkjavaspring.controllers;

import com.socialnetwork.socialnetworkjavaspring.models.*;
import com.socialnetwork.socialnetworkjavaspring.models.enums.RelationType;
import com.socialnetwork.socialnetworkjavaspring.services.post_interacts.IPostInteractService;
import com.socialnetwork.socialnetworkjavaspring.services.posts.IPostService;
import com.socialnetwork.socialnetworkjavaspring.services.profiles.IProfileService;
import com.socialnetwork.socialnetworkjavaspring.services.relations.IRelationService;
import com.socialnetwork.socialnetworkjavaspring.services.users.IUserService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/profile")
public class ProfileController extends ApplicationController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IPostService postService;

    @Autowired
    private IRelationService relationService;

    @Autowired
    private IProfileService profileService;

    @Autowired
    private IPostInteractService postInteractService;

    @GetMapping
    public ModelAndView index(@RequestParam("user-id") String userId) {
        ModelAndView modelAndView = new ModelAndView("/profiles/index");
        List<Post> posts = null;
        try {
            if (userId.equals(currentUser.getUserId())) {
                posts = setForProfileCurrentUser(modelAndView);
            } else {
                posts = setForProfileOther(modelAndView, userId);
            }
            List<PostInteract> postInteracts = postInteractService.findAllPostInteractSharedByUserIdProfile(userId);
            mergeListPostShareAndSort(posts, postInteracts);
            List<User> users = userService.findUsersByRelationType(userId, RelationType.FRIEND);
            Long followers = relationService.countFollower(userId);
            modelAndView.addObject("followers", followers);
            modelAndView.addObject("number_of_friends", users.size());
        } catch (Exception e) {
            modelAndView.setViewName("errors/server-error");
            e.printStackTrace();
        }
        modelAndView.addObject("posts", posts);
        setMedia(posts, modelAndView);
        return setAuthor(modelAndView);
    }

    private List<Post> setForProfileCurrentUser(ModelAndView modelAndView) {
        List<Post> posts = profileService.findAllPostForProfileMe(currentUser.getUserId());
        modelAndView.addObject("author", true);
        modelAndView.addObject("user_info", currentUser);
        return posts;
    }

    private List<Post> setForProfileOther(ModelAndView modelAndView, String userId) throws Exception {
        User user = userService.findById(userId);
        modelAndView.addObject("author", false);
        modelAndView.addObject("user_info", user);
        boolean isFriend = setRelationUser(modelAndView, userId);
        List<Post> posts;
        if (isFriend) {
            posts = profileService.findAllPostForProfile(userId, true, currentUser.getUserId());
        } else {
            posts = profileService.findAllPostForProfile(userId, false, currentUser.getUserId());
        }
        return posts;
    }

    @SneakyThrows
    private boolean setRelationUser(ModelAndView modelAndView, String userId) {
        boolean isFriend = false;
        boolean isRequest = false;
        boolean isFollow = false;
        if(isBlocked(userId)) {
            modelAndView.setViewName("errors/server-error");
            return false;
        }
        List<Relation> relations = relationService.findByUserIdAndUserTargetId(currentUser.getUserId(), userId);
        for (Relation relation : relations) {
            if (relation.getType() == RelationType.BLOCK) {
                modelAndView.setViewName("errors/server-error");
                break;
            }
            if (relation.getType() == RelationType.FOLLOW) {
                isFollow = true;
            }
            if (relation.getType() == RelationType.FRIEND) {
                isFriend = true;
            }
            if (relation.getType() == RelationType.REQUEST) {
                isRequest = true;
            }
        }
        modelAndView.addObject("isFriend", isFriend);
        modelAndView.addObject("isRequest", isRequest);
        modelAndView.addObject("isFollow", isFollow);
        return isFriend;
    }

    private boolean isBlocked(String userId) {
        List<Relation> relations = relationService.findByUserIdAndUserTargetId(userId, currentUser.getUserId());
        for (Relation relation : relations) {
            if (relation.getType() == RelationType.BLOCK) {
                return true;
            }
        }
        return false;
    }

    public void setMedia(List<Post> posts, ModelAndView modelAndView) {
        if (posts != null) {
            List<Media> medias = new ArrayList<>();
            for (Post post : posts) {
                medias.addAll(post.getMedias());
            }
            modelAndView.addObject("medias", medias);
        }
    }

    private void mergeListPostShareAndSort(List<Post> posts, List<PostInteract> postInteracts) {
        for (PostInteract postInteract : postInteracts) {
            Post postNew = new Post(postInteract.getPost());
            postNew.setShareInformation(postInteract);
            posts.add(postNew);
        }
        posts.sort((o1, o2) -> {
            Date o1SortValue = o1.getShareInformation() != null ? o1.getShareInformation().getInteractAt() : o1.getCreateAt();
            Date o2SortValue = o2.getShareInformation() != null ? o2.getShareInformation().getInteractAt() : o2.getCreateAt();
            return o2SortValue.compareTo(o1SortValue);
        });
    }
}
