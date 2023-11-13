package com.example.antifacebookservice.security.context;

import com.example.antifacebookservice.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class DataContextHelper {
    public static void setDataContext(DataContext dataContext) {
        DataContextHolder.setDataContext(dataContext);
    }

    public static DataContext getDataContext() {
        return DataContextHolder.getDataContext();
    }

    public static void setAuthentication(Authentication authentication, String accessToken) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getCredentials();
        DataContextHolder.setDataContext(DataContext.builder()
                .accessToken(accessToken)
                .userName(user.getUsername())
                .userId(authentication.getName())
                .build());
    }

    public static void clear() {
        DataContextHolder.clear();
    }


    public static String getAccessToken() {
        return getDataContext().getAccessToken();
    }

    public static String getUserName() {
        return getDataContext().getUserName();
    }

    public static String getUserId() {
        return getDataContext().getUserId();
    }
}
