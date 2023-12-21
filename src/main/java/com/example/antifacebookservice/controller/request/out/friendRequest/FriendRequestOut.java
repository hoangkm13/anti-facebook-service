package com.example.antifacebookservice.controller.request.out.friendRequest;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FriendRequestOut {
    private String numberOfPendingFriendRequest;
}