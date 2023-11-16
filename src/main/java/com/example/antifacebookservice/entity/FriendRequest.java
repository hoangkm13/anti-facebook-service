package com.example.antifacebookservice.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "friend-request")
public class FriendRequest {
    private String userSentId;
    private String userReceiveId;
    private Boolean isAccepted;
}
