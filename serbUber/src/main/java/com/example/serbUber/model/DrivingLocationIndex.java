package com.example.serbUber.model;

import org.hibernate.annotations.NaturalId;

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

    public DrivingLocationIndex(Location location, int index) {
        this.location = location;
        this.index = index;
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
        return index.compareTo( drivingLocationIndex.getIndex() );
    }
}
