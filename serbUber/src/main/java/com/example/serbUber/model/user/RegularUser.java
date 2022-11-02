package com.example.serbUber.model.user;

import com.example.serbUber.model.Driving;
import com.example.serbUber.model.Reservation;
import com.example.serbUber.model.Route;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name="regular_users")
public class RegularUser extends User {

    @JoinColumn(name="blocked", nullable = false)
    private boolean blocked = false;

    @JoinColumn(name="verified", nullable = false)
    private boolean verified = false;

    @ManyToMany(mappedBy = "users", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Driving> drivings = new LinkedList<>();

    @OneToMany()
    @JoinColumn(name = "route_id", referencedColumnName = "id")
    private List<Route> favouriteRoutes = new LinkedList<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "reservations_users", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "reservation_id", referencedColumnName = "id"))
    private List<Reservation> reservations = new LinkedList<>();

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

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public List<Driving> getDrivings() {
        return drivings;
    }

    public void setDrivings(List<Driving> drivings) {
        this.drivings = drivings;
    }

    public List<Route> getFavouriteRoutes() {
        return favouriteRoutes;
    }

    public void setFavouriteRoutes(List<Route> favouriteRoutes) {
        this.favouriteRoutes = favouriteRoutes;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }
}
