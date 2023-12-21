package com.example.antifacebookservice.constant;

import lombok.Getter;

@Getter
public enum MarkVerifyType {
    NEW_MARK(1, "NEW_MARK"),
    MARK_AGAIN(2, "MARK_AGAIN"),
    ALREADY_MARK(0, "ALREADY_MARK"),
    CANNOT_MARK_YOUR_OWN_POST(-1, "CANNOT_MARK_YOUR_OWN_POST"),
    AUTHOR_DEACTIVE(-2, "AUTHOR_DEACTIVE"),
    NOT_MARK(-3, "NOT_MARK"),
    NOT_ENOUGH_COIN(-4, "NOT_ENOUGH_COIN"),
    CANNOT_MARK(-5, "CANNOT_MARK");

    private final int value;
    private final String describe;

    MarkVerifyType(int value, String describe) {
        this.value = value;
        this.describe = describe;
    }
}
