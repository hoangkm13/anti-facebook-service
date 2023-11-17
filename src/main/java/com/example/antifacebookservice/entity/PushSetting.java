package com.example.antifacebookservice.entity;

import com.example.antifacebookservice.constant.SettingStatus;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Data
@Document("push-setting")
public class PushSetting {
    private String id;
    private String userId;
    private Map<String, SettingStatus> settings;
}
