package com.example.antifacebookservice.service.impl;

import com.example.antifacebookservice.constant.FeelType;
import com.example.antifacebookservice.constant.ReportType;
import com.example.antifacebookservice.constant.ResponseCode;
import com.example.antifacebookservice.controller.request.in.post.*;
import com.example.antifacebookservice.controller.request.out.post.PostDetailOut;
import com.example.antifacebookservice.controller.request.out.post.PostResponseCUD;
import com.example.antifacebookservice.controller.request.out.post.ReactOut;
import com.example.antifacebookservice.controller.request.out.post.SearchListPostOut;
import com.example.antifacebookservice.controller.request.out.user.AuthorOut;
import com.example.antifacebookservice.entity.*;
import com.example.antifacebookservice.exception.CustomException;
import com.example.antifacebookservice.helper.Common;
import com.example.antifacebookservice.repository.*;
import com.example.antifacebookservice.security.context.DataContextHelper;
import com.example.antifacebookservice.service.PostService;
import com.example.antifacebookservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    private final ModelMapper mapper;
    private final SearchRepository searchRepository;
    private final MarkRepository markRepository;


    @Override
    public PostResponseCUD createPost(CreatePostIn createPostIn, MultipartFile video) throws CustomException {
        User user = userService.findByUsername(DataContextHelper.getUserName());

        if (user.getCoins() <= 0) {
            throw new CustomException(ResponseCode.NOT_ENOUGH_COINS);
        }

        String newPostId = Common.generateUUID();

        Video newVideo = new Video();
        newVideo.setId(Common.generateUUID());
        newVideo.setUrl(createPostIn.getVideoUrl());
        newVideo.setThumb("Upload video");
        newVideo.setPostId(newPostId);

        if (video != null) {
            //Luu vao cloud
            String vId = Common.generateUUID();
            String url = String.format("http://anti-facebook-cloud/%s-%s.com", vId, LocalDate.now());
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
                .autoAccept(true)
                .build());

        int coinsLeft = user.getCoins() - 1;
        user.setCoins(coinsLeft);
        userRepository.save(user);

        return new PostResponseCUD(newPostId, "http://anti.facebook.com/post?id=" + newPostId, coinsLeft);
    }

    @Override
    public PostDetailOut getPostDetail(String token, String id) throws CustomException {
        User user = userService.findByUsername(DataContextHelper.getUserName());
        Category category = null;
        Video video = null;

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new CustomException(ResponseCode.NOT_FOUND, "Post not found!"));

        if (post.getVideoId() != null) {
            video = videoRepository.findById(post.getVideoId())
                    .orElseThrow(() -> new CustomException(ResponseCode.NOT_FOUND, "Video not found!"));
        }

        if (post.getCategoryId() != null) {
            category = categoryRepository.findById(post.getCategoryId())
                    .orElseThrow(() -> new CustomException(ResponseCode.NOT_FOUND, "Category not found!"));
        }

        AuthorOut author = mapper.map(user, AuthorOut.class);

        return PostDetailOut.builder()
                .id(post.getId())
                .name(post.getName())
                .described(post.getDescribed())
                .category(category)
                .author(author)
                .fake("").trust("").kudos("").disappointed("")
                .createdAt(LocalDateTime.now().toString()).modifiedAt(null)
                .isRated("").isMarked("").isBlocked("").canEdit("")
                .url("http://anti.facebook.com/post?id=" + post.getId())
                .images(imageRepository.findAllByPostId(post.getId()))
                .video(video)
                .build();
    }

    @Override
    public PostResponseCUD editPost(String token, String id, UpdatePostIn updatePostIn) throws CustomException {
        User user = userService.findByUsername(DataContextHelper.getUserName());

        if (user.getCoins() <= 0) {
            throw new CustomException(ResponseCode.NOT_ENOUGH_COINS);
        }

        Post updatePost = postRepository.findById(id)
                .orElseThrow(() -> new CustomException(ResponseCode.NOT_FOUND, "Post not found!"));

        updatePost.setDescribed(updatePostIn.getDescribed());
        updatePost.setStatus(updatePostIn.getStatus());
        updatePost.setImageIds(updatePostIn.getImages());
        updatePost.setAutoAccept(updatePost.isAutoAccept());
        updatePost.setModifiedAt(LocalDateTime.now().toString());

        if (updatePostIn.getImagesDel() != null) {
            updatePostIn.getImagesDel().forEach(imageId -> {
                try {
                    Image image = imageRepository.findById(imageId).orElseThrow(() ->
                            new CustomException(ResponseCode.NOT_FOUND));

                    updatePost.getImageIds().remove(imageId);
                    imageRepository.delete(image);
                } catch (CustomException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        int coinLeft = user.getCoins() - 1;
        user.setCoins(coinLeft);

        userRepository.save(user);
        postRepository.save(updatePost);

        return new PostResponseCUD(null, null, coinLeft);
    }

    @Override
    public PostResponseCUD deletePost(String token, String id) throws CustomException {
        User user = userService.findByUsername(DataContextHelper.getUserName());

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
        User user = userService.findByUsername(DataContextHelper.getUserName());

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
                .createdAt(LocalDateTime.now().toString())
                .build());
    }

    @Override
    public ReactOut reactPost(String token, String id, FeelType feelType) throws CustomException {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new CustomException(ResponseCode.NOT_FOUND, "Post not found!"));

        if (post.isRestriction()) {
            throw new CustomException(ResponseCode.RESTRICTION);
        }

        Optional<React> oldReaction = reactRepository.findByPostIdAndUserId(id, DataContextHelper.getUserId());

        oldReaction.ifPresent(reactRepository::delete);

        React newReact = new React();
        newReact.setPostId(id);
        newReact.setUserId(DataContextHelper.getUserId());
        newReact.setFeelType(feelType);

        reactRepository.save(newReact);

        int disappointed = reactRepository.countAllByFeelTypeAndPostId(FeelType.DISAPPOINTED, id);
        int kudos = reactRepository.countAllByFeelTypeAndPostId(FeelType.KUDOS, id);

        return new ReactOut(disappointed, kudos);
    }

    @Override
    public Integer checkNewItem(String lastId, String categoryId) {
        Post post = postRepository.findTopByIdAndCategoryIdOrderByCreatedAtDesc(lastId, categoryId != null ? categoryId : "0")
                .orElse(null);

        if (post == null) {
            return 0;
        }

        return postRepository.countAllByCreatedAtAfter(post.getCreatedAt());
    }

    @Override
    public List<SearchListPostOut> searchPost(SearchPostIn searchPostIn) throws CustomException {
        var searchedPost = this.postRepository.searchPostByDescribedLike(searchPostIn.getKeyword());

        Common.checkValidIndexCount(searchPostIn.getCount(), searchPostIn.getIndex(), searchedPost.size());

        var search = Search.builder().build();
        search.setKeyword(searchPostIn.getKeyword());
        search.setCreatedAt(LocalDateTime.now().toString());
        search.setId(Common.generateUUID());

        this.searchRepository.save(search);

        List<SearchListPostOut> searchListPostOuts = new ArrayList<>();
        var subList = searchedPost.subList(searchPostIn.getIndex(), searchPostIn.getIndex() + searchPostIn.getCount());
        for (Post post : subList) {
            new SearchListPostOut();
            SearchListPostOut searchListPostOut;
            searchListPostOut = mapper.map(post, SearchListPostOut.class);

            var author = this.userService.findById(post.getUserId());
            searchListPostOut.setAuthor(author);

            searchListPostOut.setMarkComment(this.markRepository.findByPostId(post.getId()).size());
            searchListPostOut.setFeel(this.reactRepository.findByPostId(post.getId()).size());

            searchListPostOut.setIsFelt(this.reactRepository.findByPostIdAndUserId(post.getId(), DataContextHelper.getUserId()).isPresent());

            searchListPostOuts.add(searchListPostOut);
        }
        return searchListPostOuts;
    }

    @Override
    public List<Search> getListSearch(GetListSearchIn getListSearchIn) throws CustomException {
        var searchedResults = this.searchRepository.findAll();

        Common.checkValidIndexCount(getListSearchIn.getCount(), getListSearchIn.getIndex(), searchedResults.size());

        return searchedResults.subList(getListSearchIn.getIndex(), getListSearchIn.getIndex() + getListSearchIn.getCount());
    }

    @Override
    public void deleteSavedSearch(DeleteSearchIn deleteSearchIn) throws CustomException {
        if (!deleteSearchIn.getAll() && deleteSearchIn.getSearchId() == null) {
            throw new CustomException(ResponseCode.METHOD_IS_INVALID);
        }
        if (!deleteSearchIn.getAll()) {
            this.searchRepository.deleteById(deleteSearchIn.getSearchId());
        } else {
            this.searchRepository.deleteAll();
        }

    }
}
