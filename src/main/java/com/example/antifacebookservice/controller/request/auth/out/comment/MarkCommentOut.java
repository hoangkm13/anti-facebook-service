package com.example.antifacebookservice.controller.request.auth.out.comment;

import com.example.antifacebookservice.constant.MarkType;
import com.example.antifacebookservice.controller.request.auth.out.post.UserOut;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MarkCommentOut {
    private String id;
    private String markContent;
    private MarkType typeOfMark;
    private UserOut poster;
    private List<MarkCommentOut> comments;
}
