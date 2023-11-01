package com.example.antifacebookservice.controller.request.auth.out.post;

import lombok.Data;

import java.util.List;

@Data
public class PostDetailOut {

    private String id;

    private String name;

    private String createdAt;

    private String described;

    private String modifiedAt;

    private String fake;

    private String trust;

    private String kudos;

    private String disappointed;

    private String isRated;

    private String isMarked;

    private List<String> imageIds;
}
