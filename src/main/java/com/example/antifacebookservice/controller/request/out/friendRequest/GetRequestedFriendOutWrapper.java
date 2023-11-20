package com.example.antifacebookservice.controller.request.out.friendRequest;

import lombok.Data;

import java.util.List;

@Data
public class GetRequestedFriendOutWrapper {
    private Integer total;
    private List<GetRequestedFriendOutBase> request;
}
