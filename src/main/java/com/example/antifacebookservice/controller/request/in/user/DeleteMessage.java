package com.example.antifacebookservice.controller.request.in.user;

import lombok.Data;

@Data
public class DeleteMessage {
    private String id;
    private Integer index;
    private Integer count;
}
