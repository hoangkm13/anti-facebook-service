package com.example.antifacebookservice.controller.request.auth.in.user;

import com.example.antifacebookservice.constant.BlockType;
import lombok.Data;

@Data
public class BlockUserIn {
    private String token;
    private String userId;
    private BlockType blockType;
}
