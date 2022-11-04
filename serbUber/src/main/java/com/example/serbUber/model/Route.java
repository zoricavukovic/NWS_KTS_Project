package com.example.serbUber.model;
import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="routes")
public class Route {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne()
    @JoinColumn(name = "location_id", referencedColumnName = "id")
    private Location startPoint;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "route_destinations",
            joinColumns = {@JoinColumn(name = "route_id")},
            inverseJoinColumns = {@JoinColumn(name = "location_id")}
    )
    private Set<Location> destinations;

    @Column(name="kilometers", nullable = false)
    private double kilometers;

    public Route(){
    }

    public Route(
        final Location startPoint,
        final Set<Location> destinations,
        final double kilometers
    ) {
        this.startPoint = startPoint;
        this.destinations = destinations;
        this.kilometers = kilometers;
    }

    public Long getId() {
        return id;
    }

    public Location getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(Location startPoint) {
        this.startPoint = startPoint;
    }

    public Set<Location> getDestinations() {
        return destinations;
    }

    public void setDestinations(Set<Location> destinations) {
        this.destinations = destinations;
    }

    public double getKilometers() {
        return kilometers;
    }

    public void setKilometers(double kilometers) {
        this.kilometers = kilometers;
    }
}
