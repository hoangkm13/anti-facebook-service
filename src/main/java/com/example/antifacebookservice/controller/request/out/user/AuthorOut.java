package com.example.antifacebookservice.controller.request.out.user;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AuthorOut extends UserOut{
    private int coins;
}