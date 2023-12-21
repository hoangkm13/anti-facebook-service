package com.example.antifacebookservice.entity;

import lombok.Data;

@Data
public class DevToken {
    private String id;
    private String token;
    private Integer devType;
    private String devToken;
    private String userId;
}
