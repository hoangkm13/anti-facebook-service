package com.example.antifacebookservice.service.impl;

import com.example.antifacebookservice.constant.FeelType;
import com.example.antifacebookservice.constant.ReportType;
import com.example.antifacebookservice.constant.ResponseCode;
import com.example.antifacebookservice.controller.request.auth.in.post.CreatePostIn;
import com.example.antifacebookservice.controller.request.auth.in.post.UpdatePostIn;
import com.example.antifacebookservice.controller.request.auth.out.post.PostDetailOut;
import com.example.antifacebookservice.controller.request.auth.out.post.PostResponseCUD;
import com.example.antifacebookservice.entity.*;
import com.example.antifacebookservice.exception.CustomException;
import com.example.antifacebookservice.repository.*;
import com.example.antifacebookservice.service.PostService;
import com.example.antifacebookservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {


    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;
    private final ReactRepository reactRepository;
    private final VideoRepository videoRepository;
    private final CategoryRepository categoryRepository;
    private final ReportPostRepository reportPostRepository;
    private final UserService userService;


    @Override
    public PostResponseCUD createPost(CreatePostIn createPostIn, MultipartFile video) throws CustomException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findById(authentication.getName());

        try {
            Post post = new Post();
            String postId = UUID.randomUUID().toString();
            post.setId(postId);
            post.setUserId(authentication.getName());
            post.setDescribed(createPostIn.getDescribed());
            post.setStatus(createPostIn.getStatus());
//            post.setImageIds(createPostIn.getImages());
            post.setVideoId(video.getBytes().toString());
            postRepository.save(post);
            return new PostResponseCUD(postId, "http://anti.facebook.com/post?id=" + postId, user.getCoins() - 1);
        } catch (IOException e) {
            throw new CustomException(ResponseCode.SERVER_ERROR);
        }
    }

    @Override
    public PostDetailOut getPostDetail(String token, String id) throws CustomException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUsername(authentication.getName());

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new CustomException(ResponseCode.NOT_FOUND, "Post not found!"));
        Video video = videoRepository.findById(post.getVideoId())
                .orElseThrow(() -> new CustomException(ResponseCode.NOT_FOUND, "Video not found!"));
        Category category = categoryRepository.findById(post.getVideoId())
                .orElseThrow(() -> new CustomException(ResponseCode.NOT_FOUND, "Category not found!"));

        return PostDetailOut.builder()
                .id(post.getId())
                .name(post.getName())
                .described(post.getDescribed())
                .category(category)
                .author(user)
                .fake("").trust("").kudos("").disappointed("")
                .createdAt("").modifiedAt("")
                .isRated("").isMarked("").isBlocked("").canEdit("")
                .url("http://anti.facebook.com/post?id=" + post.getId())
                .images(imageRepository.findAllByPostId(post.getId()))
                .video(video)
                .build();
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
            throw new CustomException(ResponseCode.NOT_FOUND, "Post not found!");
        }

        return new PostResponseCUD(null, null, user.getCoins() - 1);
    }

    @Override
    public void reportPost(String token, String id, String subject, String details) throws CustomException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findById(authentication.getName());

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new CustomException(ResponseCode.NOT_FOUND, "Post not found!"));

        if (reportPostRepository.existsByPostIdAndUserId(post.getId(), user.getId())) {
            throw new CustomException(ResponseCode.EXISTED, "You are already report this post!");
        }

        if (post.isRestriction()) {
            throw new CustomException(ResponseCode.RESTRICTION);
        }

        reportPostRepository.save(ReportPost.builder()
                .postId(post.getId())
                .reportType(ReportType.valueOf(subject))
                .describe(details)
                .createdAt(null)
                .build());
    }

    @Override
    public void reactPost(String token, String id, FeelType feelType) throws CustomException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findById(authentication.getName());

        Optional<React> oldReaction = reactRepository.findByPostIdAndUserId(id, user.getId());

        oldReaction.ifPresent(reactRepository::delete);

        React newReact = new React();
        newReact.setPostId(id);
        newReact.setUserId(user.getId());
        newReact.setFeelType(feelType);

        reactRepository.save(newReact);
    }
}
