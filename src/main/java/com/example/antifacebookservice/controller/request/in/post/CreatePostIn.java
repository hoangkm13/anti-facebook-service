package com.example.antifacebookservice.controller.request.in.post;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class CreatePostIn {

    private List<String> images;

    @NotBlank
    private String described;

    private String status;

    private String videoUrl;
}
