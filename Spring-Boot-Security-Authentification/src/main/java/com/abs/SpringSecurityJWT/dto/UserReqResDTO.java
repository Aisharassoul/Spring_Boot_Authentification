package com.abs.SpringSecurityJWT.dto;

import com.abs.SpringSecurityJWT.enitty.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserReqResDTO {

    private int statusCode;

    private String error;
    private String message;
    private String token;
    private String refreshToken;
    private String expirationTime;
    private String prenom;
    private String nom;
    private String login;
    private String tel;
    private String password;
    private String role;
    private String etat;
    //private List<Product> products;
    private User users;

}
