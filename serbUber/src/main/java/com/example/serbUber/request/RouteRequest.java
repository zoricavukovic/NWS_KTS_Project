package com.example.serbUber.request;

import com.example.serbUber.model.Location;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.LinkedList;
import java.util.List;

public class RouteRequest {
    @NotNull
    private Location startPoint;
    @NotEmpty
    private List<Location> destinations = new LinkedList<>();
    @Positive
    private double kilometers;

    public RouteRequest(
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
