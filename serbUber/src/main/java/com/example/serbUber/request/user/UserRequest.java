package com.example.serbUber.request.user;

import com.example.serbUber.util.Constants;

import javax.validation.constraints.*;

import static com.example.serbUber.exception.ErrorMessagesConstants.*;

public abstract class UserRequest {

    @Email(message = WRONG_EMAIL)
    @NotBlank(message = EMPTY_EMAIL)
    @Size(max = 60, message = TOO_LONG_EMAIL)
    private final String email;

    @NotBlank(message = WRONG_PASSWORD)
    @Pattern(regexp = Constants.LEGIT_PASSWORD_REG,
            message = WRONG_PASSWORD)
    private final String password;

    @NotBlank(message = WRONG_NAME)
    @Pattern(regexp = Constants.LEGIT_NAME_REG, message = WRONG_NAME)
    private final String name;

    @NotBlank(message = WRONG_SURNAME)
    @Pattern(regexp = Constants.LEGIT_NAME_REG, message = WRONG_SURNAME)
    private final String surname;

    @NotBlank(message = WRONG_PHONE_NUM)
    @Pattern(regexp = Constants.LEGIT_PHONE_NUMBER_REG, message = WRONG_PHONE_NUM)
    private final String phoneNumber;

    @NotBlank(message = WRONG_CITY)
    @Pattern(regexp = Constants.LEGIT_NAME_REG, message = WRONG_CITY)
    private final String city;

    private final String profilePicture;

    public UserRequest(
            final String email,
            final String password,
            final String name,
            final String surname,
            final String phoneNumber,
            final String city,
            final String profilePicture
    ) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
        this.city = city;
        this.profilePicture = profilePicture;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getCity() {
        return city;
    }

    public String getProfilePicture() {
        return profilePicture;
    }
}
