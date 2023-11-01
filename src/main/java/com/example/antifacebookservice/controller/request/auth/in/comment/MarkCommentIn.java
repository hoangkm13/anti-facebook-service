package com.example.antifacebookservice.controller.request.auth.in.comment;

import lombok.Data;

@Data
public class MarkCommentIn {

    private String token;
    private String id;
    private String content;
    private String index;
    private String count;
    private String markId;
    private String type;
}
