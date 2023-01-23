package com.example.serbUber.model;

import org.hibernate.annotations.SortNatural;

import javax.persistence.*;
import java.util.SortedSet;

@Entity
@Table(name="routes")
public class Route {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany()
    @JoinColumn(name="route_id", referencedColumnName = "id")
    @SortNatural
    private SortedSet<DrivingLocationIndex> locations;

    @Column(name="distance", nullable = false)
    private double distance;

    @Column(name="time", nullable = false)
    private double timeInMin;

    public Route(){
    }

    public Route(
        final SortedSet<DrivingLocationIndex> locations,
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

    public SortedSet<DrivingLocationIndex> getLocations() {
        return locations;
    }

    public void setLocations(SortedSet<DrivingLocationIndex> locations) {
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

    public void setId(Long id) {
        this.id = id;
    }
}
