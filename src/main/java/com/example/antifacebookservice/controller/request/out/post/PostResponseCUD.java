package com.example.antifacebookservice.controller.request.out.post;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostResponseCUD {
    private String id;
    private String url;
    private String coins;
}
