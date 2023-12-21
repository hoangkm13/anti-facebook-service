package com.example.antifacebookservice.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("post-verifier")
public class PostVerifier {
    private String id;
    private Boolean isTrust;
    private String postId;
    private String userId;
}
