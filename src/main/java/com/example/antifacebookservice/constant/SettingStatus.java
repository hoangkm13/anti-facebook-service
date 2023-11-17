
package com.example.antifacebookservice.constant;

import lombok.Getter;

@Getter
public enum SettingStatus {
    OFF(0, "OFF"),
    ON(1, "ON");

    private final Integer value;
    private final String describe;

    SettingStatus(Integer value, String describe) {
        this.value = value;
        this.describe = describe;
    }
}
