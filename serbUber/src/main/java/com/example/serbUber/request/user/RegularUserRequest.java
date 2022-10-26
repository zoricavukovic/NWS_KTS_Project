package com.example.serbUber.request.user;

import com.example.serbUber.model.Location;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class RegularUserRequest {

    @Email
    @NotBlank
    @Size(max = 1024, message = "Email length is too long.")
    private final String email;
    @NotBlank
    private final String password;
    @NotBlank
    private final String confirmationPassword;
    @NotBlank
    @Size(max = 1024, message = "Name length is too long.")
    private final String name;
    @NotBlank
    @Size(max = 1024, message = "Surname length is too long.")
    private final String surname;
    @NotBlank
    @Size(max = 1024, message = "Phone number length is too long.")
    private final String phoneNumber;
    @NotNull(message = "Must add admin address.")
    private final Location address;
    private final String profilePicture;

    public RegularUserRequest(
        final String email,
        final String password,
        final String confirmationPassword,
        final String name,
        final String surname,
        final String phoneNumber,
        final Location address,
        final String profilePicture
    ) {
        this.email = email;
        this.password = password;
        this.confirmationPassword = confirmationPassword;
        this.name = name;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
        this.address = address;
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

    public Location getAddress() {
        return address;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

}
