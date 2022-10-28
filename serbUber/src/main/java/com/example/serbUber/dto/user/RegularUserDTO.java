package com.example.serbUber.dto.user;

import com.example.serbUber.model.Location;
import com.example.serbUber.model.Route;
import com.example.serbUber.model.user.RegularUser;

import java.util.LinkedList;
import java.util.List;

public class RegularUserDTO {
    private final String email;
    private final String name;
    private final String surname;
    private final String phoneNumber;
    private final String city;
    private final String profilePicture;
    private final boolean blocked;
    private final boolean verified;
    private List<Route> favourites = new LinkedList<>();

    public RegularUserDTO(final RegularUser regularUser) {
        this.email = regularUser.getEmail();
        this.name = regularUser.getName();
        this.surname = regularUser.getSurname();
        this.phoneNumber = regularUser.getPhoneNumber();
        this.city = regularUser.getCity();
        this.profilePicture = regularUser.getProfilePicture();
        this.blocked = regularUser.isBlocked();
        this.verified = regularUser.isVerified();
        this.favourites = regularUser.getFavourites();
    }

    public static List<RegularUserDTO> fromRegularUsers(final List<RegularUser> regularUsers){
        List<RegularUserDTO> regularUserDTOs = new LinkedList<>();
        regularUsers.forEach(regularUser ->
            regularUserDTOs.add(new RegularUserDTO(regularUser))
        );

        return regularUserDTOs;
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

    public boolean isBlocked() {
        return blocked;
    }

    public boolean isVerified() {
        return verified;
    }

    public List<Route> getFavourites() {
        return favourites;
    }
}
