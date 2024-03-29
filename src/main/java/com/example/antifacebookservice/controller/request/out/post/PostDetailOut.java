package com.example.antifacebookservice.controller.request.out.post;

import com.example.antifacebookservice.controller.request.out.user.AuthorOut;
import com.example.antifacebookservice.entity.Category;
import com.example.antifacebookservice.entity.Image;
import com.example.antifacebookservice.entity.Video;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PostDetailOut {

    private String id;

    private String name;

    private String createdAt;

    private String described;

    private AuthorOut author;

    private String modifiedAt;

    private String fake;

    private String trust;

    private String kudos;

    private String disappointed;

    private String isRated;

    private String isMarked;

    private String isBlocked;

    private String banned;

    private String canEdit;

    private String canMark;

    private String canRate;

    private List<String> messages;

    private String url;

    private List<Image> images;

    private Video video;

    private Category category;

}


