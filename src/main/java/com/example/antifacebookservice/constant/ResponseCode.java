package com.example.antifacebookservice.constant;

import lombok.Getter;

@Getter
public enum ResponseCode {
    OK(1000, "OK"),
    POST_IS_NOT_EXISTED(9992, "Post is not existed"),
    CODE_VERIFY_IS_INCORRECT(9993, "Code verify is incorrect"),
    USER_IS_NOT_VALIDATED(9995, "User is not validated"),
    USER_EXISTED(9996, "User existed"),
    PARAMETER_VALUE_IS_INVALID(1004, "Paramenter value is invalid");

    private final String message;
    private final Integer code;

    ResponseCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
