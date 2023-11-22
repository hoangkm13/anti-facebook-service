package com.example.antifacebookservice.controller.request.out.user;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserInfoOut extends BaseUserOut{
    private String description;
    private String created;
    private String coverImage;
    private String address;
    private String country;
    private String city;
    private String online;
    private String link;
    private String listing;
    private String isFriend;
    private String coins;
}
