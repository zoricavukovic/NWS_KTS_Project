package com.example.serbUber.model;
import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="routes")
public class Route {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "route_locations",
            joinColumns = @JoinColumn(name = "route_id"),
            inverseJoinColumns = @JoinColumn(name = "location_id")
    )
    private Set<Location> locations;

    @Column(name="distance", nullable = false)
    private double distance;

    @Column(name="time", nullable = false)
    private double timeInMin;

    public Route(){
    }

    public Route(
        final Set<Location> locations,
        final double distance,
        final double timeInMin
    ) {
        this.locations = locations;
        this.distance = distance;
        this.timeInMin = timeInMin;
    }

    public Long getId() {
        return id;
    }

    public Set<Location> getLocations() {
        return locations;
    }

    public void setLocations(Set<Location> locations) {
        this.locations = locations;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getTimeInMin() {
        return timeInMin;
    }

    public void setTimeInMin(double timeInMin) {
        this.timeInMin = timeInMin;
    }
}
