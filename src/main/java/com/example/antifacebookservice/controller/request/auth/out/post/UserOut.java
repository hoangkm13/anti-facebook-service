package com.example.antifacebookservice.controller.request.auth.out.post;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public  class UserOut {
    private String id;
    private String name;
    private String avatar;
    private int coins;
}
