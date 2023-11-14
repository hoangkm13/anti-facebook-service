package com.example.antifacebookservice.controller.request.auth.out.post;

import lombok.Data;

@Data
public  class UserOut {
    private String id;
    private String username;
    private String avatar;
    private int coins;
}
