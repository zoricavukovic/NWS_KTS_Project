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
    private Location address;
    private String profilePicture;
    private Role role;

    public User(
        final String email,
        final String password,
        final String name,
        final String surname,
        final String phoneNumber,
        final Location address,
        final String profilePicture,
        final Role role
    ) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
        this.address = address;
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

    public Location getAddress() {
        return address;
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
