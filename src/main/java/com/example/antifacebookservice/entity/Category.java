package com.example.antifacebookservice.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "category")
public class Category {
    private String id;
    private String name;
    private String hasName;
}
