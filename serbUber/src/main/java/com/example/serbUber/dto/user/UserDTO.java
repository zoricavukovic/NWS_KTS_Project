package com.example.serbUber.dto.user;

import com.example.serbUber.model.user.Driver;
import com.example.serbUber.model.user.RegularUser;
import com.example.serbUber.model.user.Role;
import com.example.serbUber.model.user.User;

import java.util.LinkedList;
import java.util.List;

import static com.example.serbUber.util.PictureHandler.convertPictureToBase64ByName;

public class UserDTO{

    private final Long id;
    private final String email;
    private final String name;
    private final String surname;
    private final String phoneNumber;
    private final String city;
    private final String profilePicture;
    private Role role;
    private final String password;
    private boolean online = false;

    public UserDTO(final User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.name = user.getName();
        this.surname = user.getSurname();
        this.phoneNumber = user.getPhoneNumber();
        this.city = user.getCity();
        this.profilePicture = convertPictureToBase64ByName(user.getProfilePicture());
        this.role = user.getRole();
        this.password = user.getPassword();
        this.online = user.isOnline();
    }

    public UserDTO(
        final Long id,
        final String email,
        final String name,
        final String surname,
        final String phoneNumber,
        final String city,
        final String profilePicture,
        final Role role,
        final String password
    ) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
        this.city = city;
        this.profilePicture = convertPictureToBase64ByName(profilePicture);
        this.role = role;
        this.password = password;
    }

    public UserDTO(final Driver driver) {
        this.id = driver.getId();
        this.email = driver.getEmail();
        this.name = driver.getName();
        this.surname = driver.getSurname();
        this.phoneNumber = driver.getPhoneNumber();
        this.city = driver.getCity();
        this.profilePicture = convertPictureToBase64ByName(driver.getProfilePicture());
        this.role = driver.getRole();
        this.password = driver.getPassword();
        this.online = driver.isOnline();
    }

    public UserDTO(final RegularUser regularUser) {
        this.id = regularUser.getId();
        this.email = regularUser.getEmail();
        this.name = regularUser.getName();
        this.surname = regularUser.getSurname();
        this.phoneNumber = regularUser.getPhoneNumber();
        this.city = regularUser.getCity();
        this.profilePicture = convertPictureToBase64ByName(regularUser.getProfilePicture());
        this.role = regularUser.getRole();
        this.password = regularUser.getPassword();
        this.online = regularUser.isOnline();
    }

    public static List<UserDTO> fromUsers(final List<User> users){
        List<UserDTO> userDTOs = new LinkedList<>();
        users.forEach(user ->
            userDTOs.add(new UserDTO(user))
        );

        return userDTOs;
    }

    public String getEmail() {
        return email;
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

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public boolean isOnline() {
        return online;
    }
}
