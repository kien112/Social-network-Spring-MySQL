package com.socialnetwork.socialnetworkjavaspring.controllers.apis;

import com.socialnetwork.socialnetworkjavaspring.DTOs.posts.CreatePostRequest;
import com.socialnetwork.socialnetworkjavaspring.controllers.ApplicationController;
import com.socialnetwork.socialnetworkjavaspring.services.posts.IPostServices;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController extends ApplicationController {

    @Autowired
    private IPostServices postServices;
    @PostMapping(value = "/create", consumes = "multipart/form-data")
    public ResponseEntity<?> createPost(@RequestParam("access") String access,
                                         @RequestParam("content") String content,
                                         @RequestParam("postType") String postType,
                                         @RequestPart(value = "files", required = false) List<MultipartFile> files,
                                         @RequestParam(value = "userTagIds", required = false) List<String> userTagIds,
                                         @RequestParam(value = "hagTags", required = false) List<String> hagTags){
        return new ResponseEntity<>(
                postServices.createPost(access, content, postType,
                        files, userTagIds, currentUser.getUserId(), hagTags),
                HttpStatus.CREATED);
    }
}
