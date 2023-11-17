package com.example.antifacebookservice.controller;

import com.example.antifacebookservice.controller.request.auth.in.comment.MarkCommentIn;
import com.example.antifacebookservice.exception.CustomException;
import com.example.antifacebookservice.model.ApiResponse;
import com.example.antifacebookservice.service.MarkCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/mark")
@RequiredArgsConstructor
public class MarkController {

    private final MarkCommentService markCommentService;

    @GetMapping("/get-mark-comment")
    public ApiResponse<?> getMarkComment(String id, int index, int count) throws CustomException {
        return ApiResponse.successWithResult(markCommentService.getMarkComment(id, index, count));
    }

    @PostMapping("/set-mark-comment")
    public ApiResponse<?> setMarkComment(@RequestBody MarkCommentIn markCommentIn) throws CustomException {
        return ApiResponse.successWithResult(markCommentService.setMarkComment(markCommentIn));
    }
}
