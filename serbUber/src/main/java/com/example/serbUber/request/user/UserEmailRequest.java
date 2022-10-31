package com.example.serbUber.request.user;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class UserEmailRequest {

    @NotBlank(message = "Email must exist!")
    @Size(max = 1024, message = "Email length is too long.")
    @Email(message = "Email is in wrong format")
    private final String email;

    public UserEmailRequest(final String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
