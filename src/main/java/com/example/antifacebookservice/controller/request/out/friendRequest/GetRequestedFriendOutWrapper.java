package com.example.antifacebookservice.controller.request.out.friendRequest;

import lombok.Data;

import java.util.List;

@Data
public class GetRequestedFriendOutWrapper {
    private String total;
    private List<GetRequestedFriendOutBase> request;
}
