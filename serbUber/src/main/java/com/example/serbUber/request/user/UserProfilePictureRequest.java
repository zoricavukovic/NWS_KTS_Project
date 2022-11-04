package com.example.serbUber.request.user;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import static com.example.serbUber.exception.ErrorMessagesConstants.*;

public class UserProfilePictureRequest {

    @Email(message = WRONG_EMAIL)
    @NotBlank(message = EMPTY_EMAIL)
    @Size(max = 60, message = TOO_LONG_EMAIL)
    private final String email;

    @NotBlank(message = "Profile picture must be selected.")
    private final String profilePicture;

    public UserProfilePictureRequest(
            final String email,
            final String profilePicture
    ) {
        this.email = email;
        this.profilePicture = profilePicture;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public String getEmail() {
        return email;
    }
}
