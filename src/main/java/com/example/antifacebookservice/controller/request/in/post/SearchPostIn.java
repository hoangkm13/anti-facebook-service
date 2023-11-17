package com.example.antifacebookservice.controller.request.in.post;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class SearchPostIn {
    @NotBlank
    private String keyword;

    private Integer index;
    private Integer count;

}
