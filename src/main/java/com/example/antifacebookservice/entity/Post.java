package com.example.antifacebookservice.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "post")
public class Post {

    private String id;

    private String userId;

    private String name;

    private String token;

    private String described;

    private String status;

    private List<String> commentIds;

    private List<String> userLikeIds;

    private String videoId;

    private String categoryId;

    private boolean isRestriction;
}
