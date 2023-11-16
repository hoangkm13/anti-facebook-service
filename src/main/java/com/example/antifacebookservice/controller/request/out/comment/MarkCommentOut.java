package com.example.antifacebookservice.controller.request.out.comment;

import com.example.antifacebookservice.constant.MarkType;
import com.example.antifacebookservice.controller.request.out.user.AuthorOut;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MarkCommentOut {
    private String id;
    private String markContent;
    private MarkType typeOfMark;
    private AuthorOut poster;
    private List<MarkCommentOut> comments;
}
