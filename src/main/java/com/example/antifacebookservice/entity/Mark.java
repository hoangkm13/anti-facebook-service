package com.example.antifacebookservice.entity;

import com.example.antifacebookservice.constant.MarkType;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document("mark")
public class Mark {
    private String id;
    private String markContent;
    private MarkType typeOfMark;
    private String userId;
    private String postId;
    private String createdAt;
    private List<String> childComments;
}
