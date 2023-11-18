package com.example.antifacebookservice.controller.request.in.user;

import lombok.Data;

@Data
public class GetConversation {
    private String id;
    private Integer index;
    private Integer count;
}
