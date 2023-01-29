package com.example.serbUber.model.user;

import com.example.serbUber.model.Driving;
import com.example.serbUber.model.Reservation;
import com.example.serbUber.model.Route;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Fetch;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

import static com.example.serbUber.util.Constants.ROLE_REGULAR_USER;
import static org.hibernate.annotations.FetchMode.SELECT;

@Entity
@Table(name="regular_users")
public class RegularUser extends User {

    @JoinColumn(name="blocked", nullable = false)
    private boolean blocked = false;

    @ManyToMany
    @JoinTable(name = "drivings_users", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "driving_id", referencedColumnName = "id"))
    private List<Driving> drivings = new LinkedList<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "favourite_routes", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "route_id", referencedColumnName = "id"))
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
        final String profilePicture,
        final Role role
    ) {
        super(email, password, name, surname, phoneNumber, city, profilePicture, role);
    }

    public RegularUser(
        final Long id,
        final String email,
        final String password,
        final String name,
        final String surname,
        final String phoneNumber,
        final String city,
        final String profilePicture,
        final Role role
    ) {
        super(id, email, password, name, surname, phoneNumber, city, profilePicture, role);
    }


    public RegularUser(RegularUser regularUser) {
        super(
                regularUser.getId(),
                regularUser.getEmail(),
                regularUser.getPassword(),
                regularUser.getName(),
                regularUser.getSurname(),
                regularUser.getPhoneNumber(),
                regularUser.getCity(),
                regularUser.getProfilePicture(),
                regularUser.getRole());
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
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
