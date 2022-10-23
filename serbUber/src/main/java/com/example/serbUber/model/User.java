package com.example.serbUber.model;

public abstract class User {
    private String id;
    private String email;
    private String password;
    private String name;
    private String surname;
    private String phoneNumber;
    private Location address;
    private String profilePicture;

    public User(
        final String email,
        final String password,
        final String name,
        final String surname,
        final String phoneNumber,
        final Location address,
        final String profilePicture
    ) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.profilePicture = profilePicture;
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
}
