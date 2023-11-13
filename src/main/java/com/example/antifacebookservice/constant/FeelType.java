package com.example.antifacebookservice.constant;

import lombok.Getter;

@Getter
public enum FeelType {
    DISAPPOINTED(0, "DISAPPOINTED"),
    KUDOS(0, "KUDOS");

    private final Integer value;
    private final String describe;

    FeelType(Integer value, String describe) {
        this.value = value;
        this.describe = describe;
    }
}
