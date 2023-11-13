package com.example.antifacebookservice.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "image")
public class Image {
    private String id;
    private String url;
    private String postId;
}
