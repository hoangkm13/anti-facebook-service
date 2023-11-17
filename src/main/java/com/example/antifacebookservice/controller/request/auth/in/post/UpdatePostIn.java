package com.example.antifacebookservice.controller.request.auth.in.post;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class UpdatePostIn extends CreatePostIn {
    private List<String> imagesDel;
    private List<String> imageSort;
    private String autoAccept;
}
