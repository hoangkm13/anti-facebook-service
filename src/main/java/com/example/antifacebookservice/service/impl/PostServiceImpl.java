package com.example.antifacebookservice.service.impl;

import com.example.antifacebookservice.constant.ResponseCode;
import com.example.antifacebookservice.controller.request.auth.in.post.CreatePostIn;
import com.example.antifacebookservice.controller.request.auth.in.post.UpdatePostIn;
import com.example.antifacebookservice.controller.request.auth.out.post.PostDetailOut;
import com.example.antifacebookservice.controller.request.auth.out.post.PostResponseCUD;
import com.example.antifacebookservice.entity.Post;
import com.example.antifacebookservice.entity.User;
import com.example.antifacebookservice.exception.CustomException;
import com.example.antifacebookservice.repository.PostRepository;
import com.example.antifacebookservice.repository.UserRepository;
import com.example.antifacebookservice.service.PostService;
import com.example.antifacebookservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Override
    public PostResponseCUD createPost(CreatePostIn createPostIn) throws CustomException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findById(authentication.getName());

        Post post = new Post();
        String postId = UUID.randomUUID().toString();
        post.setId(postId);
        post.setUserId(authentication.getName());
        post.setDescribed(createPostIn.getDescribed());
        postRepository.save(post);

        return new PostResponseCUD(postId, "http://anti.facebook.com/post?id=" + postId, user.getCoins() - 1);
    }

    @Override
    public PostDetailOut getPostDetail(String token, String id) {
        return null;
    }

    @Override
    public PostResponseCUD editPost(UpdatePostIn updatePostIn) {
        return null;
    }

    @Override
    public PostResponseCUD deletePost(String token, String id) throws CustomException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findById(authentication.getName());

        Optional<Post> post = postRepository.findById(id);

        if (post.isEmpty()) {
            throw new CustomException(ResponseCode.POST_IS_NOT_EXISTED);
        }

        return new PostResponseCUD(null, null, user.getCoins() - 1);
    }
}
