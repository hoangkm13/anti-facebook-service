package com.example.antifacebookservice.controller.request.in.comment;

import com.example.antifacebookservice.constant.MarkType;
import lombok.Data;

@Data
public class MarkCommentIn {

    private String token;
    private String id;
    private String content;
    private int index;
    private int count;
    private String markId;
    private MarkType type;
}
