package com.example.antifacebookservice.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "user")

public class User {

    @Id
    private String id;

    private String username;

//    private String firstName;
//
//    private String lastName;

    private String avatar;
//    private Boolean avatar;

//    @NotBlank(message = "Giới tính không được để trống !")
//    private String gender;
//
//    private String birthOfDate;
//
//    private String mobile;
//
    private String passwordHash;

    private Boolean isVerified;

    private String email;

    private int coins;

    private Boolean active;

    private List<String> postLists;

//    private String country;

}
