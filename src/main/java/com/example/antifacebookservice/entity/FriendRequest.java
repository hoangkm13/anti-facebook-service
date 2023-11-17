package com.example.antifacebookservice.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "friend-request")
public class FriendRequest {
    @Id
    private String id;

    private String userSentId;
    private String userReceiveId;
    private Boolean isAccepted;
}
