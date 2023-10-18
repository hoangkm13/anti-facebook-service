package com.example.antifacebookservice.controller.request.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpDTO {

    @NotBlank(message = "UUID thiết bị không được bỏ trống")
    private String deviceUUID;

    @NotBlank(message = "Mail không được để trống !")
    @Email(message = "Sai định dạng email !")
    @Size(max = 256, message = "Mail Tối đa 256 ký tự !")
    private String email;

    @NotNull
    @Length(max = 10, min = 6)
    private String password;

}
