package com.example.antifacebookservice.controller;

import com.example.antifacebookservice.constant.FeelType;
import com.example.antifacebookservice.constant.ResponseCode;
import com.example.antifacebookservice.controller.request.auth.in.comment.MarkCommentIn;
import com.example.antifacebookservice.controller.request.auth.in.post.CreatePostIn;
import com.example.antifacebookservice.controller.request.auth.in.post.ListPostIn;
import com.example.antifacebookservice.controller.request.auth.in.post.UpdatePostIn;
import com.example.antifacebookservice.controller.request.auth.out.post.PostDetailOut;
import com.example.antifacebookservice.controller.request.auth.out.post.PostResponseCUD;
import com.example.antifacebookservice.exception.CustomException;
import com.example.antifacebookservice.model.ApiResponse;
import com.example.antifacebookservice.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("v1/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping(path = "/create-new", consumes = {"multipart/form-data"})
    public ApiResponse<?> createPost(@RequestPart CreatePostIn createPostIn, @RequestPart(required = false) MultipartFile video) throws CustomException {
        try {
            PostResponseCUD createPostOut = postService.createPost(createPostIn, video);
            return ApiResponse.successWithResult(createPostOut);
        } catch (Exception e) {
            throw new CustomException(ResponseCode.SERVER_ERROR, e.getMessage());
        }
    }

    @PostMapping("/edit-post")
    public ApiResponse<?> editPost(String token, String id, @RequestBody UpdatePostIn updatePostIn) throws CustomException {
        try {
            return ApiResponse.successWithResult(postService.editPost(token, id, updatePostIn));
        } catch (Exception e) {
            throw new CustomException(ResponseCode.SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("/detail")
    public ApiResponse<?> getPost(String token, String id) throws CustomException {
        try {
            PostDetailOut postDetailOut = postService.getPostDetail(token, id);

            return ApiResponse.successWithResult(postDetailOut);
        } catch (Exception e) {
            throw new CustomException(ResponseCode.SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("/list-post")
    public ApiResponse<?> getListPosts(ListPostIn listPostIn) {
        return null;
    }

    @DeleteMapping("/delete")
    public ApiResponse<?> deletePost(String token, String id) throws CustomException {
        try {
            return ApiResponse.successWithResult(postService.deletePost(token, id));
        } catch (Exception e) {
            throw new CustomException(ResponseCode.SERVER_ERROR, e.getMessage());
        }
    }


    @PostMapping("/react-post")
    public ApiResponse<?> reactPost(String token, String id, FeelType feelType) throws CustomException {
        try {
            return ApiResponse.successWithResult(postService.reactPost(token, id, feelType), "Reacted!");
        } catch (Exception e) {
            throw new CustomException(ResponseCode.SERVER_ERROR, e.getMessage());
        }
    }

    @PostMapping("/report")
    public ApiResponse<?> reportPost(String token, String id, String subject, String details) throws CustomException {
        try {
            postService.reportPost(token, id, subject, details);
            return ApiResponse.successWithResult(null, "Report success!");
        } catch (Exception e) {
            throw new CustomException(ResponseCode.SERVER_ERROR, e.getMessage());
        }
    }

    @PostMapping("/check-new-item")
    public ApiResponse<?> checkNewItem(String lastId, @RequestParam(required = false) String categoryId) throws CustomException {
        try {
            return ApiResponse.successWithResult(postService.checkNewItem(lastId, categoryId));
        } catch (Exception e) {
            throw new CustomException(ResponseCode.SERVER_ERROR, e.getMessage());
        }
    }

}
