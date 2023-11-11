package com.example.antifacebookservice.controller.request.auth.in.post;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class CreatePostIn {

    private List<String> images;

    @NotBlank
    private String described;

    private String status;
}
