package com.example.antifacebookservice.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "comment")
public class Comment {
    private String id;
    private String content;
    private String createdAt;
    private String userId;
}
