package com.example.antifacebookservice.controller.request.auth.out.version;

import com.example.antifacebookservice.controller.request.auth.out.user.UserVersionOut;
import com.example.antifacebookservice.entity.AppVersion;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CheckVersionOut {
    private AppVersion appVersion;
    private UserVersionOut user;
    private Integer badge;
    private Integer unreadMessage;
    private String now;
}
