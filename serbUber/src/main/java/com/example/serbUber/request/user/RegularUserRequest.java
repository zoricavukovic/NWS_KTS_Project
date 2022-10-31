package com.example.serbUber.request.user;

import com.example.serbUber.util.Constants;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import static com.example.serbUber.exception.ErrorMessagesConstants.WRONG_PASSWORD;

public class RegularUserRequest extends UserRequest{

    @NotBlank(message = WRONG_PASSWORD)
    @Pattern(regexp = Constants.LEGIT_PASSWORD_REG,
            message = WRONG_PASSWORD)
    private final String confirmationPassword;

    public RegularUserRequest(
            final String email,
            final String password,
            final String name,
            final String surname,
            final String phoneNumber,
            final String city,
            final String profilePicture,
            final String confirmPassword
    ) {
        super(email, password, name, surname, phoneNumber, city, profilePicture);
        this.confirmationPassword = confirmPassword;
    }

    public String getConfirmationPassword() {
        return confirmationPassword;
    }
}
