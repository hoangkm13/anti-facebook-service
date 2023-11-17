package com.example.antifacebookservice.entity;

import com.example.antifacebookservice.constant.UpdateRequireStatus;
import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("app-version")
public class AppVersion {
    private String id;

    @Indexed(unique = true)
    private String appName;

    private String description;

    @Indexed(unique = true)
    private String versionNumber;

    private UpdateRequireStatus require;

    private String downloadUrl;

    private String releaseDate;
}
