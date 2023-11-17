package com.example.antifacebookservice.controller.request.auth.validation;

import lombok.Getter;

@Getter
public class ValidationPatterns {
    public static final String PASSWORD_VALIDATION = "^[\\w_\\d]+$";
}
