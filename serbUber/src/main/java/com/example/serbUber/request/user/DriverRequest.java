package com.example.serbUber.request.user;

import com.example.serbUber.model.Location;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class DriverRequest extends UserRequest{

    public DriverRequest(
            final String email,
            final String password,
            final String name,
            final String surname,
            final String phoneNumber,
            final String city,
            final String profilePicture
    ) {
        super(email, password, name, surname, phoneNumber, city, profilePicture);
    }
}
