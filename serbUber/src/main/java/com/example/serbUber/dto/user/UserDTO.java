package com.example.serbUber.dto.user;

import com.example.serbUber.model.user.Role;
import com.example.serbUber.model.user.User;

import java.util.LinkedList;
import java.util.List;

import static com.example.serbUber.util.PictureHandler.convertPictureToBase64ByName;

public class UserDTO{

    private final String email;
    private final String name;
    private final String surname;
    private final String phoneNumber;
    private final String city;
    private final String profilePicture;
    private Role role;
    private String password;

    public UserDTO(final User user) {
        this.email = user.getEmail();
        this.name = user.getName();
        this.surname = user.getSurname();
        this.phoneNumber = user.getPhoneNumber();
        this.city = user.getCity();
        this.profilePicture = convertPictureToBase64ByName(user.getProfilePicture());
        this.role = user.getRole();
        this.password = user.getPassword();
    }

    public UserDTO(
        final String email,
        final String name,
        final String surname,
        final String phoneNumber,
        final String city,
        final String profilePicture,
        final Role role,
        final String password
    ) {
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
        this.city = city;
        this.profilePicture = profilePicture;
        this.role = role;
        this.password = password;
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
}
