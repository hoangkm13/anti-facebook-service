package com.example.antifacebookservice.controller.request.out.notification;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UnreadNotificationOut {
    private String badge;
    private String lastUpdated;
}
