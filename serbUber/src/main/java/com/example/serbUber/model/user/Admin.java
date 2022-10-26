package com.example.serbUber.model.user;

import com.example.serbUber.model.Location;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "admins")
public class Admin extends User{

    public Admin(
        final String email,
        final String password,
        final String name,
        final String surname,
        final String phoneNumber,
        final Location address,
        final String profilePicture
    ) {
        super(email, password, name, surname, phoneNumber, address, profilePicture, new Role("ROLE_ADMIN"));
    }

}
