package com.example.antifacebookservice.entity;

import com.example.antifacebookservice.controller.request.out.user.BaseUserOut;
import lombok.Data;
import nonapi.io.github.classgraph.json.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("conversation")
public class Conversation {
    @Id
    private String id;
    private BaseUserOut partner;
    private Message lastMessage;

}