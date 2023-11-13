package com.example.antifacebookservice.entity;

import com.example.antifacebookservice.constant.ReportType;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document(collection = "report-post")
public class ReportPost {
    private String id;

    private String postId;

    private String userId;

    private ReportType reportType;

    private String createdAt;

    private String describe;
}
