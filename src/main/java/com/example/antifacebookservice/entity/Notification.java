package com.example.antifacebookservice.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("notification")
public class Notification {
    private String notificationId;
    private String type;
    private String objectId;
    private String title;
    private String createdAt;
    private String avatar;
    private String userId;
    private Integer group;
    private Boolean isRead;
    private String badge;
    private String lastUpdated;
}
