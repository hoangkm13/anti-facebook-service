package com.example.antifacebookservice.entity;

import com.example.antifacebookservice.constant.FeelType;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "react")
public class React {
    private String postId;
    private String userId;
    private FeelType feelType;
}
