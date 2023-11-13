package com.example.antifacebookservice.security.config;

import com.example.antifacebookservice.security.interception.DataContextInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebInterceptorConfig implements WebMvcConfigurer {

    private final DataContextInterceptor dataContextInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(dataContextInterceptor).order(1);
    }
}
