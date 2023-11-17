package com.example.antifacebookservice.constant;

import lombok.Getter;

@Getter
public enum ResponseCode {
    OK(1000, "OK"),
    RESTRICTION(1010, "The post is violates community standards or country restrictions"),
    NOT_ENOUGH_COINS(9991, "Not enough coins!"),
    NOT_FOUND(9992, "Not found!"),
    CODE_VERIFY_IS_INCORRECT(9993, "Code verify is incorrect"),
    USER_IS_NOT_VALIDATED(9995, "User is not validated"),
    EXISTED(9996, "User existed"),
    FRIEND_REQUEST_EXISTED(9993, "Friend Request Existed"),
    NOT_EXISTED(9995, "User not existed"),
    METHOD_IS_INVALID(9997, "Method is invalid"),
    TOKEN_IS_INVALID(9998, "Token is invalid"),
    SERVER_ERROR(9999, "Server error"),
    INDEX_CANNOT_BIGGER_THAN_COUNT(1003, "Index cao hơn số đếm"),
    COUNT_CANNOT_BIGGER_THAN_RESULT_SIZE(1003, "Số đếm không được nhiều hơn số kết quả"),
    PARAMETER_VALUE_IS_INVALID(1004, "Paramenter value is invalid"),
    WARNING(1111, "WARNING");

    private final String message;
    private final Integer code;

    ResponseCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
