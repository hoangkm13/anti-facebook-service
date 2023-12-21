
package com.example.antifacebookservice.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("message")
public class Message {
    private String id;
    private String content;
    private String created;
    private Boolean unread;

}
