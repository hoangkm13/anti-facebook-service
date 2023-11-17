package com.example.antifacebookservice.controller.request.auth.out.post;

import com.example.antifacebookservice.entity.Image;
import com.example.antifacebookservice.entity.User;
import com.example.antifacebookservice.entity.Video;
import lombok.Data;

@Data
public class SearchListPostOut {
    private String id;

    private String name;

    private Image image;

    private Video video;

    private Integer feel;

    private Integer markComment;

    private Boolean isFelt;

    private User author;

    private String described;

    private String createdAt;

    private String modifiedAt;
}
