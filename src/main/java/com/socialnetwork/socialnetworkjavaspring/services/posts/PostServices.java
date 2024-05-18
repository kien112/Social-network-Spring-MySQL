package com.socialnetwork.socialnetworkjavaspring.services.posts;

import com.socialnetwork.socialnetworkjavaspring.DTOs.posts.CreatePostRequest;
import com.socialnetwork.socialnetworkjavaspring.DTOs.posts.PostResponse;
import com.socialnetwork.socialnetworkjavaspring.DTOs.users.UserParticipantDTO;
import com.socialnetwork.socialnetworkjavaspring.models.*;
import com.socialnetwork.socialnetworkjavaspring.models.enums.MediaType;
import com.socialnetwork.socialnetworkjavaspring.models.enums.PostAccess;
import com.socialnetwork.socialnetworkjavaspring.models.enums.PostType;
import com.socialnetwork.socialnetworkjavaspring.models.key.PostHashtagId;
import com.socialnetwork.socialnetworkjavaspring.models.key.UserTagId;
import com.socialnetwork.socialnetworkjavaspring.repositories.IHashtagRepository;
import com.socialnetwork.socialnetworkjavaspring.repositories.IMediaRepository;
import com.socialnetwork.socialnetworkjavaspring.repositories.IPostRepository;
import com.socialnetwork.socialnetworkjavaspring.repositories.IUserRepository;
import com.socialnetwork.socialnetworkjavaspring.services.medias.IMediaService;
import com.socialnetwork.socialnetworkjavaspring.utils.ConvertUtils;
import com.socialnetwork.socialnetworkjavaspring.utils.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class PostServices implements IPostServices{

    @Autowired
    private IPostRepository postRepository;
    @Autowired
    private IHashtagRepository hashtagRepository;
    @Autowired
    private IMediaService mediaService;
    @Autowired
    private IUserRepository userRepository;

    public List<Post> findAllPostForNewsFeed(String userId) {
        return postRepository.findAllPostForNewsFeed(userId);
    }

    @Override
    @Transactional
    public PostResponse createPost(CreatePostRequest request,
                                   List<MultipartFile> files,
                                   String userId) {

        Post post = new Post();
        post.setPostContent(request.getContent());
        post.setPostType(request.getPostType());
        post.setAccess(request.getAccess());
        post.setPostId(UUID.randomUUID().toString());
        post.setUser(new User(userId));
        List<UserParticipantDTO> userTags = setUserTags(post, request.getUserTagIds());
        setPostHagTags(post, request.getHagTags());

        postRepository.save(post);

        List<String> medias = mediaService.saveList(files, post.getPostId(), userId);

        PostResponse postResponse = ConvertUtils.convert(post, PostResponse.class);
        postResponse.setCreateAt(DateFormatUtils.getCurrentDateTimeInVn());
        postResponse.setMedias(medias);
        postResponse.setUserTags(userTags);

        return postResponse;
    }

    private List<UserParticipantDTO> setUserTags(Post post, List<String> userTagIds) {
        List<UserTag> userTags = new ArrayList<>();
        List<UserParticipantDTO> userParticipantDTOS = new ArrayList<>();

        if(userTagIds == null)
            return userParticipantDTOS;

        for (String userId : userTagIds) {
            User user = userRepository.findById(userId).orElseThrow(
                    () -> new NullPointerException("User not found!")
            );

            UserTagId userTagId = new UserTagId(userId, post.getPostId());
            UserTag userTag = new UserTag(userTagId, user, post);

            userTags.add(userTag);
            userParticipantDTOS.add(new UserParticipantDTO(
                    userTag.getUser().getUserId(),
                    userTag.getUser().getFullName(),
                    userTag.getUser().getAvatar()
            ));
        }
        post.setUserTags(userTags);

        return userParticipantDTOS;
    }

    private void setPostHagTags(Post post, List<String> hagTags) {
        post.setPostHashtags(new ArrayList<>());
        if(hagTags == null)
            return;

        for (String hagTag : hagTags) {
            Hashtag existHashtag = hashtagRepository.findByHashtag(hagTag)
                    .orElse(null);

            if(existHashtag == null){
                existHashtag = new Hashtag();
                existHashtag.setHashtag(hagTag);
                hashtagRepository.save(existHashtag);
            }

            PostHashtag postHashtag = new PostHashtag();
            postHashtag.setPostHashtagId(new PostHashtagId(
                    post.getPostId(),
                    existHashtag.getHashtagId()
            ));
            postHashtag.setHashtag(existHashtag);
            postHashtag.setPost(post);
            post.getPostHashtags().add(postHashtag);
        }
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
