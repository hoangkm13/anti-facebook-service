package com.example.antifacebookservice.controller.request.in.user;

import lombok.Data;

@Data
public class DeleteConversation {
    private String id;
    private Integer index;
    private Integer count;
}
