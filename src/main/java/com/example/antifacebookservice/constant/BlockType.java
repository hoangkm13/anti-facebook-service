package com.example.antifacebookservice.constant;

import lombok.Getter;

@Getter
public enum BlockType {
    BLOCK(0, "BLOCK"),
    UNBLOCK(1, "UNBLOCK");

    private final Integer value;
    private final String describe;

    BlockType(Integer value, String describe) {
        this.value = value;
        this.describe = describe;
    }
}
