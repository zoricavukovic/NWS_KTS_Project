package com.example.serbUber.model;
import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name="routes")
public class Route {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne()
    @JoinColumn(name = "location_id", referencedColumnName = "id")
    private Location startPoint;

    @OneToMany()
    @JoinTable(name = "route_destinations",
            joinColumns = {@JoinColumn(name = "route_id")},
            inverseJoinColumns = {@JoinColumn(name = "location_id")}
    )
    private List<Location> destinations = new LinkedList<>();

    @Column(name="kilometers", nullable = false)
    private double kilometers;

    public Route(){
    }

    public Route(
        final Location startPoint,
        final List<Location> destinations,
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

    public List<Location> getDestinations() {
        return destinations;
    }

    public void setDestinations(List<Location> destinations) {
        this.destinations = destinations;
    }

    public double getKilometers() {
        return kilometers;
    }

    public void setKilometers(double kilometers) {
        this.kilometers = kilometers;
    }
}
