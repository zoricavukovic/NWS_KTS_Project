package com.example.serbUber.model.user;

import com.example.serbUber.model.Location;
import org.checkerframework.common.aliasing.qual.Unique;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

public abstract class User {

    @Id
    private String id;
    @Indexed(unique = true)
    private String email;
    private String password;
    private String name;
    private String surname;
    private String phoneNumber;
    private String city;
    private String profilePicture;
    private Role role;

    public User() {}

    public User(
            final String id,
            final String email,
            final String password,
            final String name,
            final String surname,
            final String phoneNumber,
            final String city,
            final String profilePicture,
            final Role role
    ) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
        this.city = city;
        this.profilePicture = profilePicture;
        this.role = role;
    }

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public static boolean passwordsMatch(String password, String confirmationPassword){

        return password.equals(confirmationPassword);
    }
}
