package com.example.serbUber.model.user;

import com.example.serbUber.model.Location;
import org.springframework.data.annotation.Id;

public abstract class User {

    @Id
    private String id;
    private String email;
    private String password;
    private String name;
    private String surname;
    private String phoneNumber;
    private String city;
    private String profilePicture;
    private Role role;

    public User(
        final String email,
        final String password,
        final String name,
        final String surname,
        final String phoneNumber,
        final String city,
        final String profilePicture,
        final Role role
    ) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
        this.city = city;
        this.profilePicture = profilePicture;
        this.role = role;
    }

    public User() {

    }

    public String getId() {
        return id;
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

    public static boolean passwordsMatch(String password, String confirmationPassword){

        return password.equals(confirmationPassword);
    }

    public Role getRole() {
        return role;
    }
}
