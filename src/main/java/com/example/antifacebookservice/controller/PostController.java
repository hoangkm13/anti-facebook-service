package com.example.antifacebookservice.controller;

import com.example.antifacebookservice.constant.FeelType;
import com.example.antifacebookservice.constant.ResponseCode;
import com.example.antifacebookservice.controller.request.in.post.*;
import com.example.antifacebookservice.controller.request.out.post.PostDetailOut;
import com.example.antifacebookservice.controller.request.out.post.PostResponseCUD;
import com.example.antifacebookservice.controller.request.out.post.SearchListPostOut;
import com.example.antifacebookservice.entity.Search;
import com.example.antifacebookservice.exception.CustomException;
import com.example.antifacebookservice.model.ApiResponse;
import com.example.antifacebookservice.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("v1/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping(path = "/create-new", consumes = {"multipart/form-data"})
    public ApiResponse<?> createPost(@RequestPart CreatePostIn createPostIn, @RequestPart(required = false) MultipartFile video) throws CustomException {
        PostResponseCUD createPostOut = postService.createPost(createPostIn, video);
        return ApiResponse.successWithResult(createPostOut);
    }

    @PostMapping("/edit-post")
    public ApiResponse<?> editPost(String token, String id, @RequestBody UpdatePostIn updatePostIn) throws CustomException {
        return ApiResponse.successWithResult(postService.editPost(token, id, updatePostIn));
    }

    @PostMapping("/search-post")
    public ApiResponse<List<SearchListPostOut>> searchPost(@RequestBody SearchPostIn searchPostIn) throws CustomException {
        return ApiResponse.successWithResult(postService.searchPost(searchPostIn));
    }

    @PostMapping("/get-saved-search")
    public ApiResponse<List<Search>> getListSearch(@RequestBody GetListSearchIn getListSearchIn) throws CustomException {
        return ApiResponse.successWithResult(postService.getListSearch(getListSearchIn));
    }

    @PostMapping("/delete-search")
    public ApiResponse<?> deleteSavedSearch(@RequestBody DeleteSearchIn deleteSearchIn) throws CustomException {
        postService.deleteSavedSearch(deleteSearchIn);
        return ApiResponse.successWithResult(null, "Delete success!");
    }

    @GetMapping("/detail")
    public ApiResponse<?> getPost(String token, String id) throws CustomException {
        PostDetailOut postDetailOut = postService.getPostDetail(token, id);

        return ApiResponse.successWithResult(postDetailOut);
    }

    @GetMapping("/list-post")
    public ApiResponse<?> getListPosts(ListPostIn listPostIn) {
        return null;
    }

    @DeleteMapping("/delete")
    public ApiResponse<?> deletePost(String token, String id) throws CustomException {
        return ApiResponse.successWithResult(postService.deletePost(token, id));
    }


    @PostMapping("/react-post")
    public ApiResponse<?> reactPost(String token, String id, FeelType feelType) throws CustomException {
        return ApiResponse.successWithResult(postService.reactPost(token, id, feelType), "Reacted!");
    }

    @PostMapping("/report")
    public ApiResponse<?> reportPost(String token, String id, String subject, String details) throws CustomException {
        postService.reportPost(token, id, subject, details);
        return ApiResponse.successWithResult(null, "Report success!");
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
