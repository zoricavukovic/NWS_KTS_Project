package com.example.serbUber.dto.user;

import com.example.serbUber.model.Route;
import com.example.serbUber.model.user.RegularUser;

import java.util.LinkedList;
import java.util.List;

public class RegularUserDTO extends UserDTO{
    private final boolean blocked;
    private final boolean verified;
    private List<Route> favourites = new LinkedList<>();

    public RegularUserDTO(final RegularUser regularUser) {
        super(
            regularUser.getId(),
            regularUser.getEmail(),
            regularUser.getName(),
            regularUser.getSurname(),
            regularUser.getPhoneNumber(),
            regularUser.getCity(),
            regularUser.getProfilePicture(),
            regularUser.getRole(),
            regularUser.getPassword()
        );
        this.blocked = regularUser.isBlocked();
        this.verified = regularUser.isVerified();
        this.favourites = regularUser.getFavouriteRoutes();
    }

    public static List<RegularUserDTO> fromRegularUsers(final List<RegularUser> regularUsers){
        List<RegularUserDTO> regularUserDTOs = new LinkedList<>();
        regularUsers.forEach(regularUser ->
            regularUserDTOs.add(new RegularUserDTO(regularUser))
        );

        return regularUserDTOs;
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
