package com.example.antifacebookservice.service;

import com.example.antifacebookservice.controller.request.auth.in.comment.MarkCommentIn;
import com.example.antifacebookservice.controller.request.auth.out.comment.MarkCommentOut;
import com.example.antifacebookservice.exception.CustomException;

public interface MarkCommentService {
    MarkCommentOut setMarkComment(MarkCommentIn markCommentIn) throws CustomException;
    MarkCommentOut getMarkComment(String id, int index, int count) throws CustomException;
}
