package com.example.serbUber.request.user;

import com.example.serbUber.model.Location;
import com.example.serbUber.request.LocationRequest;
import com.example.serbUber.util.Constants;

import javax.validation.constraints.*;

public class RegularUserRequest {

    @Email(message = "Email is not in the right format.")
    @NotBlank(message = "Email cannot be empty.")
    @Size(max = 1024, message = "Email length is too long.")
    private final String email;

    @Pattern(regexp = Constants.LEGIT_PASSWORD_REG,
            message = "Password must contain at least 5 characters. At least one number and one special character.")
    private final String password;

    @Pattern(regexp = Constants.LEGIT_PASSWORD_REG,
            message = "Password must contain at least 5 characters. At least one number and one special character.")
    private final String confirmationPassword;

    @Pattern(regexp = Constants.LEGIT_NAME_REG, message = "Name must contain only letters and cannot be too long.")
    private final String name;

    @Pattern(regexp = Constants.LEGIT_NAME_REG, message = "Surname must contain between 2 and 30 letters.")
    private final String surname;

    @Pattern(regexp = Constants.LEGIT_PHONE_NUMBER_REG, message = "Phone number must contain 9 digits.")
    private final String phoneNumber;

    @Pattern(regexp = Constants.LEGIT_NAME_REG, message = "City must contain between 2 and 30 letters.")
    private String city;

    private final String profilePicture;

    public RegularUserRequest(
        final String email,
        final String password,
        final String confirmationPassword,
        final String name,
        final String surname,
        final String phoneNumber,
        final String city,
        final String profilePicture
    ) {
        this.email = email;
        this.password = password;
        this.confirmationPassword = confirmationPassword;
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

    public String getConfirmationPassword() {
        return confirmationPassword;
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
