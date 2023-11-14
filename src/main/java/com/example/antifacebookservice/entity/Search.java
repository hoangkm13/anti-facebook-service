package com.example.antifacebookservice.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document(collection = "search")
public class Search {

    private String id;

    private String keyword;

    private String createdAt;
}
