package com.example.antifacebookservice.controller.request.auth.out.user;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AuthorOut extends UserOut{
    private int coins;
}
