package com.example.serbUber.request.user;

public class AdminRequest extends UserRequest {

    public AdminRequest(
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
