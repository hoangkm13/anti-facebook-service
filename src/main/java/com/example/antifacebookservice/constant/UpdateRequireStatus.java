package com.example.antifacebookservice.constant;

import lombok.Getter;

@Getter
public enum UpdateRequireStatus {
    REQUIRE(0, "Bắt buộc"),
    NOT_REQUIRE(1, "Không bắt buộc");

    private final Integer value;
    private final String describe;

    UpdateRequireStatus(Integer value, String describe) {
        this.value = value;
        this.describe = describe;
    }
}
