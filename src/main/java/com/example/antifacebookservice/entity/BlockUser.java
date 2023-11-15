package com.example.antifacebookservice.entity;

import lombok.Builder;
import lombok.Data;
import nonapi.io.github.classgraph.json.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document("block-user")
public class BlockUser {
    @Id
    private String id;
    private String blockerId;
    private String blockedId;
}
