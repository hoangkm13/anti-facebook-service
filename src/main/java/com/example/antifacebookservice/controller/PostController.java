package com.example.antifacebookservice.controller;

import com.example.antifacebookservice.constant.FeelType;
import com.example.antifacebookservice.controller.request.auth.in.post.CreatePostIn;
import com.example.antifacebookservice.controller.request.auth.in.post.ListPostIn;
import com.example.antifacebookservice.controller.request.auth.in.comment.MarkCommentIn;
import com.example.antifacebookservice.controller.request.auth.in.post.UpdatePostIn;
import com.example.antifacebookservice.controller.request.auth.out.post.PostResponseCUD;
import com.example.antifacebookservice.controller.request.auth.out.post.PostDetailOut;
import com.example.antifacebookservice.exception.CustomException;
import com.example.antifacebookservice.model.ApiResponse;
import com.example.antifacebookservice.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequestMapping("/v1/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/create-new")
    public ApiResponse<?> createPost(@RequestBody CreatePostIn createPostIn, MultipartFile video) throws CustomException {
        PostResponseCUD createPostOut = postService.createPost(createPostIn, video);
        return ApiResponse.successWithResult(createPostOut);
    }

    @PostMapping("/edit-post")
    public ApiResponse<?> editPost(String token, String id, @RequestBody UpdatePostIn updatePostIn){
        return null;
    }

    @GetMapping("/detail")
    public ApiResponse<?> getPost(String token, String id) throws CustomException, IOException {
        PostDetailOut postDetailOut = postService.getPostDetail(token, id);

        return ApiResponse.successWithResult(postDetailOut);
    }

    @GetMapping("/list-post")
    public ApiResponse<?> getListPosts(ListPostIn listPostIn){
        return null;
    }

    @DeleteMapping("/delete")
    public ApiResponse<?> deletePost(String token, String id) throws CustomException {
        return ApiResponse.successWithResult(postService.deletePost(token, id));
    }


    @PostMapping("/react-post")
    public ApiResponse<?> reactPost(String token, String id, FeelType feelType){
        return null;
    }

    @PostMapping("/report")
    public ApiResponse<?> reportPost(String token, String id, String subject, String details) throws CustomException {
        postService.reportPost(token, id, subject, details);
        return ApiResponse.successWithResult(null, "Report success!");
    }

    @PostMapping("/mark-comment")
    public ApiResponse<?> setMarkComment(MarkCommentIn markCommentIn){
        return null;
    }


    @GetMapping("/mark-comment")
    public ApiResponse<?> getMarkComment(String id, String index, String count){
        return null;
    }
}
