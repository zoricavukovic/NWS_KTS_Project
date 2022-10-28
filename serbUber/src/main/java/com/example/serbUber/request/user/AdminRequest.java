package com.example.serbUber.request.user;

import com.example.serbUber.model.Location;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class AdminRequest {

    @Email
    @NotBlank
    @Size(max = 1024, message = "Email length is too long.")
    private final String email;
    @NotBlank
    private final String password;
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
    private String city;
    private final String profilePicture;

    public AdminRequest(
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
