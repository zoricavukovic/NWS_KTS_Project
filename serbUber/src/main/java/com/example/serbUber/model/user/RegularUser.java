package com.example.serbUber.model.user;

import com.example.serbUber.dto.user.UserDTO;
import com.example.serbUber.model.Driving;
import com.example.serbUber.model.Location;
import com.example.serbUber.model.Route;

import java.util.LinkedList;
import java.util.List;

public class RegularUser extends User{

    private boolean blocked = false;
    private boolean verified = false;
    private List<Driving> drives = new LinkedList<>();
    private List<Route> favourites = new LinkedList<>();

    public RegularUser(
        final String email,
        final String password,
        final String name,
        final String surname,
        final String phoneNumber,
        final String city,
        final String profilePicture,
        final List<Driving> drives,
        final List<Route> favourites
    ) {
        super(email, password, name, surname, phoneNumber, city, profilePicture, new Role("ROLE_REGULAR_USER"));
        this.drives = drives;
        this.favourites = favourites;
    }

    public RegularUser(
        final String email,
        final String password,
        final String name,
        final String surname,
        final String phoneNumber,
        final String city,
        final String profilePicture
    ) {
        super(email, password, name, surname, phoneNumber, city, profilePicture, new Role("ROLE_REGULAR_USER"));
    }

    public boolean isBlocked() {
        return blocked;
    }

    public boolean isVerified() {
        return verified;
    }

    public List<Driving> getDrives() {
        return drives;
    }

    public List<Route> getFavourites() {
        return favourites;
    }
}
