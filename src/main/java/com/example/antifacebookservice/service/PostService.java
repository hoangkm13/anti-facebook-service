package com.example.antifacebookservice.service;

import com.example.antifacebookservice.controller.request.auth.in.post.CreatePostIn;
import com.example.antifacebookservice.controller.request.auth.in.post.UpdatePostIn;
import com.example.antifacebookservice.controller.request.auth.out.post.PostDetailOut;
import com.example.antifacebookservice.controller.request.auth.out.post.PostResponseCUD;
import com.example.antifacebookservice.exception.CustomException;

public interface PostService {

    PostResponseCUD createPost(CreatePostIn createPostIn) throws CustomException;
    PostDetailOut getPostDetail(String token, String id);

    PostResponseCUD editPost(UpdatePostIn updatePostIn);

    PostResponseCUD deletePost(String token, String id) throws CustomException;

}
