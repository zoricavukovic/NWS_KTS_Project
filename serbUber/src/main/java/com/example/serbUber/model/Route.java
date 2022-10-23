package com.example.serbUber.model;

import java.util.LinkedList;
import java.util.List;

public class Route {
    private Location startPoint;
    private List<Location> destinations = new LinkedList<>();
    private double kilometers;

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
