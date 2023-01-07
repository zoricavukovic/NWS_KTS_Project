package com.example.serbUber.model;

import javax.persistence.*;

@Entity
@Table(name="driving_locations")
public class DrivingLocationIndex implements Comparable<DrivingLocationIndex>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "location_id", referencedColumnName = "id")
    private Location location;

    @Column(name="index")
    private Integer index;

    @Column(name="route_index")
    private int routeIndex;

    public DrivingLocationIndex(Location location, int index, int routeIndex) {
        this.location = location;
        this.index = index;
        this.routeIndex = routeIndex;
    }

    public DrivingLocationIndex() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        id = id;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public int compareTo(DrivingLocationIndex drivingLocationIndex) {
        return index.compareTo(drivingLocationIndex.getIndex());
    }

    public int getRouteIndex() {
        return routeIndex;
    }

    public void setRouteIndex(int routeIndex) {
        this.routeIndex = routeIndex;
    }
}
