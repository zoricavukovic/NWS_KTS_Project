package com.example.serbUber.request.user;

import com.example.serbUber.util.Constants;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import static com.example.serbUber.exception.ErrorMessagesConstants.WRONG_PASSWORD;

public class LoginRequest {

    @NotBlank(message = "Email must exist.")
    @Size(max = 1024, message = "Email length is too long.")
    @Email(message = "Email is in wrong format.")
    private String email;

    @NotBlank(message = WRONG_PASSWORD)
    @Pattern(regexp = Constants.LEGIT_PASSWORD_REG,
        message = WRONG_PASSWORD)
    private String password;

    public LoginRequest(final String email, final String password) {
        this.email = email;
        this.password = password;
    }

    public LoginRequest(){}

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
