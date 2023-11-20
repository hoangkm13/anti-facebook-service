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

    private Integer fake;

    private Integer trust;

    private Integer kudos;

    private Integer disappointed;

    private Boolean isRated;

    private Boolean isMarked;

    private Boolean isBlocked;

    private Boolean banned;

    private Boolean canEdit;

    private String canRate;

    private String url;

    private List<Image> images;

    private Video video;

    private Category category;

}


