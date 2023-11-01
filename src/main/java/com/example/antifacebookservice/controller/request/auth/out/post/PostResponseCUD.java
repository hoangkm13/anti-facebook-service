package com.example.antifacebookservice.controller.request.auth.out.post;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostResponseCUD {
    private String id;
    private String url;
    private int coins;
}
