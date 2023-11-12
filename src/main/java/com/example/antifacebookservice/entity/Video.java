package com.example.antifacebookservice.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "video")
public class Video {
    private String id;
    private String url;
    private String thumb;
    private String postId;
}
