package com.example.serbUber.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.LinkedList;
import java.util.List;

@Document(collection = "routes")
public class Route {
    @Id
    private String id;
    private Location startPoint;
    private List<Location> destinations = new LinkedList<>();
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

    public Location getStartPoint() {
        return startPoint;
    }

    public List<Location> getDestinations() {
        return destinations;
    }

    public double getKilometers() {
        return kilometers;
    }
}
