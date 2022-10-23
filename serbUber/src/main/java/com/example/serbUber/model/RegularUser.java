package com.example.serbUber.model;

import java.util.LinkedList;
import java.util.List;

public class RegularUser extends User{

    private boolean blocked;
    private boolean verified;
    private List<Driving> drives = new LinkedList<>();
    private List<Route> favourites = new LinkedList<>();

    public RegularUser(
        final String email,
        final String password,
        final String name,
        final String surname,
        final String phoneNumber,
        final Location address,
        final String profilePicture,
        final boolean blocked,
        final boolean verified,
        final List<Driving> drives,
        final List<Route> favourites
    ) {
        super(email, password, name, surname, phoneNumber, address, profilePicture);
        this.blocked = blocked;
        this.verified = verified;
        this.drives = drives;
        this.favourites = favourites;
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
