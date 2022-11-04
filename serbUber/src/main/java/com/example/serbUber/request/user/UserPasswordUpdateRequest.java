package com.example.serbUber.request.user;

import com.example.serbUber.util.Constants;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import static com.example.serbUber.exception.ErrorMessagesConstants.*;

public class UserPasswordUpdateRequest {

    @Email(message = WRONG_EMAIL)
    @NotBlank(message = EMPTY_EMAIL)
    @Size(max = 60, message = TOO_LONG_EMAIL)
    private final String email;

    @NotBlank(message = WRONG_PASSWORD)
    @Pattern(regexp = Constants.LEGIT_PASSWORD_REG,
            message = WRONG_PASSWORD)
    private final String currentPassword;

    @NotBlank(message = WRONG_PASSWORD)
    @Pattern(regexp = Constants.LEGIT_PASSWORD_REG,
            message = WRONG_PASSWORD)
    private final String newPassword;

    @NotBlank(message = WRONG_PASSWORD)
    @Pattern(regexp = Constants.LEGIT_PASSWORD_REG,
            message = WRONG_PASSWORD)
    private final String confirmPassword;

    public UserPasswordUpdateRequest(
            final String email,
            final String currentPassword,
            final String newPassword,
            final String confirmPassword) {
        this.email = email;
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
        this.confirmPassword = confirmPassword;
    }

    public String getEmail() {
        return email;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }
}
