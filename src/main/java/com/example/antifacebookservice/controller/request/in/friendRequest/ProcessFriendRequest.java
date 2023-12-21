package com.example.antifacebookservice.controller.request.in.friendRequest;

import lombok.Data;

@Data
public class ProcessFriendRequest {
    private String userSentId;
    private Boolean isAccept;
}
