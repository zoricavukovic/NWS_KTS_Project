package com.example.serbUber.model.user;

import javax.persistence.*;

@Entity
@Table(name="admins")
public class Admin extends User{

    public Admin() {
        super();
    }

    public Admin(
        final String email,
        final String password,
        final String name,
        final String surname,
        final String phoneNumber,
        final String city,
        final String profilePicture,
        final Role role
    ) {
        super(email, password, name, surname, phoneNumber, city, profilePicture, role);
    }

}
