package com.example.serbUber.request.user;

import javax.validation.constraints.Email;

public class UserEmailRequest {

    @Email
    private final String email;

    public UserEmailRequest(final String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
