package com.example.antifacebookservice.constant;

import lombok.Getter;

@Getter
public enum ReportType {
    OFFENSIVE("OFFENSIVE"),
    VIOLENT("VIOLENT"),
    AGE_RESTRICTION("AGE_RESTRICTION"),
    RACIALISM("RACIALISM");

    private final String describe;

    ReportType(String describe) {
        this.describe = describe;
    }
}
