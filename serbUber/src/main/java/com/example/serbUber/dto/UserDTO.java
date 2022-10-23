package com.example.serbUber.dto;

import com.example.serbUber.model.Location;
import com.example.serbUber.model.User;

import java.util.LinkedList;
import java.util.List;

public class UserDTO {

    private final String email;
    private final String name;
    private final String surname;
    private final String phoneNumber;
    private final Location address;
    private final String profilePicture;

    public UserDTO(final User user) {
        this.email = user.getEmail();
        this.name = user.getName();
        this.surname = user.getSurname();
        this.phoneNumber = user.getPhoneNumber();
        this.address = user.getAddress();
        this.profilePicture = user.getProfilePicture();
    }

    public static List<UserDTO> fromUsers(List<User> users){
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

    public Location getAddress() {
        return address;
    }

    public String getProfilePicture() {
        return profilePicture;
    }
}
