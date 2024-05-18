package com.socialnetwork.socialnetworkjavaspring.services.posts;

import com.socialnetwork.socialnetworkjavaspring.DTOs.posts.CreatePostRequest;
import com.socialnetwork.socialnetworkjavaspring.DTOs.posts.PostResponse;
import com.socialnetwork.socialnetworkjavaspring.models.Post;
import com.socialnetwork.socialnetworkjavaspring.services.IGeneralService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IPostServices extends IGeneralService<Post, String> {
    List<Post> findAllPostForNewsFeed(String userId);
    PostResponse createPost(CreatePostRequest request,
                            List<MultipartFile> files,
                            String userId);
}
