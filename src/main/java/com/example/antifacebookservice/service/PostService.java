package com.example.antifacebookservice.service;

import com.example.antifacebookservice.constant.FeelType;
import com.example.antifacebookservice.controller.request.auth.in.post.*;
import com.example.antifacebookservice.controller.request.auth.out.post.PostDetailOut;
import com.example.antifacebookservice.controller.request.auth.out.post.PostResponseCUD;
import com.example.antifacebookservice.controller.request.auth.out.post.ReactOut;
import com.example.antifacebookservice.controller.request.auth.out.post.SearchListPostOut;
import com.example.antifacebookservice.entity.Post;
import com.example.antifacebookservice.entity.Post;
import com.example.antifacebookservice.entity.Search;
import com.example.antifacebookservice.exception.CustomException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.List;

public interface PostService {

    PostResponseCUD createPost(CreatePostIn createPostIn, MultipartFile video) throws CustomException;

    PostDetailOut getPostDetail(String token, String id) throws CustomException;

    PostResponseCUD editPost(String token, String id, UpdatePostIn updatePostIn) throws CustomException;

    PostResponseCUD deletePost(String token, String id) throws CustomException;

    void reportPost(String token, String id, String subject, String details) throws CustomException;

    ReactOut reactPost(String token, String id, FeelType feelType) throws CustomException;

    Integer checkNewItem(String lastId, String categoryId);

    List<SearchListPostOut> searchPost(SearchPostIn searchPostIn) throws CustomException;
    List<Search> getListSearch(GetListSearchIn getListSearchIn) throws CustomException;
    void deleteSavedSearch(DeleteSearchIn deleteSearchIn) throws CustomException;

}
