package com.example.antifacebookservice.controller.request.auth;

import com.example.antifacebookservice.controller.request.auth.validation.ValidationPatterns;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordDTO {
    @NotBlank
    private String currentPassword;

    @NotBlank
    @Pattern(regexp = ValidationPatterns.PASSWORD_VALIDATION, message = "Password contains special character!")
    @Length(max = 20, min = 6)
    private String password;
}
