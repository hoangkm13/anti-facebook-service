package com.example.antifacebookservice.constant;

import lombok.Getter;

@Getter
public enum RateVerifyType {
    NEW_RATE(1, "NEW_RATE"),
    RATE_AGAIN(2, "RATE_AGAIN"),
    ALREADY_RATE(0, "ALREADY_RATE"),
    CANNOT_RATE_YOUR_OWN_POST(-1, "CANNOT_RATE_YOUR_OWN_POST"),
    AUTHOR_DEACTIVE(-2, "AUTHOR_DEACTIVE"),
    NOT_RATE(-3, "NOT_RATE"),
    NOT_ENOUGH_COIN(-4, "NOT_ENOUGH_COIN"),
    CANNOT_RATE(-5, "CANNOT_RATE");

    private final int value;
    private final String describe;

    RateVerifyType(int value, String describe) {
        this.value = value;
        this.describe = describe;
    }
}
