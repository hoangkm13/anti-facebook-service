package com.example.antifacebookservice.security.context;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DataContext {
    private String accessToken;
    private String userId;
    private String userName;
    private boolean isActive;

}
