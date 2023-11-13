package com.example.antifacebookservice.security.context;

import org.springframework.security.core.Authentication;

public class DataContextHelper {
    public static void setDataContext(DataContext dataContext) {
        DataContextHolder.setDataContext(dataContext);
    }

    public static DataContext getDataContext() {
        return DataContextHolder.getDataContext();
    }

    public static void setAuthentication(Authentication authentication, String accessToken) {
        DataContextHolder.setDataContext(DataContext.builder()
                .accessToken(accessToken)
                .userId(authentication.getName())
                .build());
    }

    public static void clear() {
        DataContextHolder.clear();
    }


    public static String getAccessToken() {
        return getDataContext().getAccessToken();
    }

    public static String getUserId() {
        return getDataContext().getUserId();
    }
}
