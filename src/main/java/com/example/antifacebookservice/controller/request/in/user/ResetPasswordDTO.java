package com.example.antifacebookservice.controller.request.in.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordDTO {
    @NotBlank
    private String password;
}
