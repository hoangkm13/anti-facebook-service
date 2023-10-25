package com.example.antifacebookservice.controller.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckVerifyCodeResponse {
    private String id;
    private Boolean active;
}
