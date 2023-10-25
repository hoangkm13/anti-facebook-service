package com.example.antifacebookservice.controller.request.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckCodeVerifyRequest {
    @NotBlank(message = "Mail không được để trống !")
    @Email(message = "Sai định dạng email !")
    @Size(max = 256, message = "Mail Tối đa 256 ký tự !")
    private String email;

    @NotNull
    private Integer codeVerify;

}
