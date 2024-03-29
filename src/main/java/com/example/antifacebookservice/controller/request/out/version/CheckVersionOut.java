package com.example.antifacebookservice.controller.request.out.version;

import com.example.antifacebookservice.controller.request.out.user.UserVersionOut;
import com.example.antifacebookservice.entity.AppVersion;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CheckVersionOut {
    private AppVersion appVersion;
    private UserVersionOut user;
    private String badge;
    private String unreadMessage;
    private String now;
}
