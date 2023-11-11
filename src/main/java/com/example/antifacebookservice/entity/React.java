package com.example.antifacebookservice.entity;

import com.example.antifacebookservice.constant.FeelType;
import lombok.Data;

@Data
public class React {
    private String postId;
    private String userId;
    private FeelType feelType;
}
