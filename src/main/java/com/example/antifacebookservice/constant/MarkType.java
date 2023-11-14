package com.example.antifacebookservice.constant;

import lombok.Getter;

@Getter
public enum MarkType {
    TRUST(1, "TRUST"),
    FAKE(0, "FAKE");

    private final Integer value;
    private final String describe;

    MarkType(Integer value, String describe) {
        this.value = value;
        this.describe = describe;
    }
}
