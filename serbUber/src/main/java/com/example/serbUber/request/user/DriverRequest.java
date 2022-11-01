package com.example.serbUber.request.user;

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
