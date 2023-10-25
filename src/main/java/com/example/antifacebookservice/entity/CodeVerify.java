package com.example.antifacebookservice.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "code-verify")

public class CodeVerify {
    @Id
    private String id;
    private String username;

    private Integer codeVerify;

    private LocalTime createdTime;

}
