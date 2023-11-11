package com.example.antifacebookservice.entity;

import com.example.antifacebookservice.constant.ReportType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReportPost {
    private String id;

    private String postId;

    private String userId;

    private ReportType reportType;

    private String createdAt;

    private String describe;
}
