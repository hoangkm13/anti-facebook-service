package com.example.antifacebookservice.exception;

import com.example.antifacebookservice.constant.ResponseCode;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CustomException extends Exception {
    private String message;
    private Integer errorCode;

    public CustomException(ResponseCode responseCode, String... args) {
        super(String.format(responseCode.getMessage(), args));

        if (args != null && args.length > 0) this.message = String.join(", ", args);
        else this.message = responseCode.getMessage();

        this.errorCode = responseCode.getCode();
    }
}
