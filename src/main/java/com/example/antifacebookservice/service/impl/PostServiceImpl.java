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
import java.time.LocalDate;
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

        if (user.getCoins() <= 0) {
            throw new CustomException(ResponseCode.NOT_ENOUGH_COINS);
        }

        String newPostId = UUID.randomUUID().toString();

        Video newVideo = new Video();
        newVideo.setId(UUID.randomUUID().toString());
        newVideo.setUrl(createPostIn.getVideoUrl());
        newVideo.setThumb("Upload video");
        newVideo.setPostId(newPostId);

        if (!video.isEmpty()) {
            //Luu vao cloud
            String vId = UUID.randomUUID().toString();
            String url = "http://anti-facebook-cloud/%s-%s.com".formatted(vId, LocalDate.now());
            newVideo.setUrl(url);
            //
        }
        videoRepository.save(newVideo);

        postRepository.save(Post.builder()
                .id(newPostId)
                .userId(user.getId())
                .described(createPostIn.getDescribed())
                .status(createPostIn.getStatus())
                .videoId(newVideo.getId())
                .build());

        int coinsLeft = user.getCoins() - 1;
        user.setCoins(coinsLeft);
        userRepository.save(user);

        return new PostResponseCUD(newPostId, "http://anti.facebook.com/post?id=" + newPostId, coinsLeft);
    }

    @Override
    public PostDetailOut getPostDetail(String token, String id) throws CustomException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findById(authentication.getName());

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new CustomException(ResponseCode.NOT_FOUND, "Post not found!"));
        Video video = videoRepository.findById(post.getVideoId())
                .orElseThrow(() -> new CustomException(ResponseCode.NOT_FOUND, "Video not found!"));
        Category category = categoryRepository.findById(post.getCategoryId())
                .orElseThrow(() -> new CustomException(ResponseCode.NOT_FOUND, "Video not found!"));

        PostDetailOut.AuthorOut author = new PostDetailOut.AuthorOut();
        author.setId(user.getId());
        author.setName(user.getUsername());
        author.setAvatar(user.getAvatar());
        author.setCoins(user.getCoins());

        return PostDetailOut.builder()
                .id(post.getId())
                .name(post.getName())
                .described(post.getDescribed())
                .category(category)
                .author(author)
                .fake("").trust("").kudos("").disappointed("")
                .createdAt("").modifiedAt("")
                .isRated("").isMarked("").isBlocked("").canEdit("")
                .url("http://anti.facebook.com/post?id=" + post.getId())
                .images(imageRepository.findAllByPostId(post.getId()))
                .video(video)
                .build();
    }

    @Override
    public PostResponseCUD editPost(String token, String id, UpdatePostIn updatePostIn) {
        return null;
    }

    @Override
    public PostResponseCUD deletePost(String token, String id) throws CustomException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findById(authentication.getName());

        if (user.getCoins() <= 0) {
            throw new CustomException(ResponseCode.NOT_ENOUGH_COINS);
        }

        Optional<Post> post = postRepository.findById(id);

        if (post.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND, "Post not found!");
        }

        user.setCoins(user.getCoins() - 1);
        userRepository.save(user);
        postRepository.delete(post.get());

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
