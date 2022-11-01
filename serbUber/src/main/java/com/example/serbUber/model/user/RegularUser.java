package com.example.serbUber.model.user;

import com.example.serbUber.model.Driving;
import com.example.serbUber.model.Route;

import java.util.LinkedList;
import java.util.List;

public class RegularUser extends User{

    private boolean blocked = false;
    private boolean verified = false;
    private List<Driving> drivings = new LinkedList<>();
    private List<Route> favourites = new LinkedList<>();

    public RegularUser() {
        super();
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

    public RegularUser(RegularUser regularUser) {
    }

    public boolean isBlocked() {
        return blocked;
    }

    public boolean isVerified() {
        return verified;
    }

    public List<Driving> getDrivings() {
        return drivings;
    }

    public List<Route> getFavourites() {
        return favourites;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }
}
