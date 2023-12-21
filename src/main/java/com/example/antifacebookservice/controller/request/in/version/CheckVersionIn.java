package com.example.antifacebookservice.controller.request.in.version;

import lombok.Data;

@Data
public class CheckVersionIn {
    private String token;
    private String lastUpdate;
}
