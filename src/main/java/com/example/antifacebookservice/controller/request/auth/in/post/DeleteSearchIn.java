package com.example.antifacebookservice.controller.request.auth.in.post;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class DeleteSearchIn {
    private String searchId;

    @NotBlank
    private Boolean all;
}
