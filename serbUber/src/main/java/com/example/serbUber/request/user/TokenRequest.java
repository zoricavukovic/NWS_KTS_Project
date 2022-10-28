package com.example.serbUber.request.user;

import javax.validation.constraints.NotBlank;

public class TokenRequest {

    @NotBlank(message = "Token must exist.")
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
