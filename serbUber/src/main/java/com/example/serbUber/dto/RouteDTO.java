package com.example.serbUber.dto;

import com.example.serbUber.model.Location;
import com.example.serbUber.model.Route;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class RouteDTO {

    private Location startPoint;
    private Set<Location> destinations;
    private double kilometers;

    public RouteDTO(final Route route) {
        this.startPoint = route.getStartPoint();
        this.destinations = route.getDestinations();
        this.kilometers = route.getKilometers();
    }

    public static List<RouteDTO> fromRoutes(final List<Route> routes){
        List<RouteDTO> routeDTOs = new LinkedList<>();
        routes.forEach(route ->
                routeDTOs.add(new RouteDTO(route))
        );

        return routeDTOs;
    }

    public Location getStartPoint() {
        return startPoint;
    }

    public Set<Location> getDestinations() {
        return destinations;
    }

    public double getKilometers() {
        return kilometers;
    }
}
