package com.example.serbUber.dto;

import com.example.serbUber.model.Location;
import com.example.serbUber.model.Route;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class RouteDTO {

    private Set<Location> locations;
    private double distance; //in metres
    private double timeInMin;

    public RouteDTO(final Route route) {
        this.locations = route.getLocations();
        this.distance = route.getDistance();
        this.timeInMin = route.getTimeInMin();
    }

    public static List<RouteDTO> fromRoutes(final List<Route> routes){
        List<RouteDTO> routeDTOs = new LinkedList<>();
        routes.forEach(route ->
                routeDTOs.add(new RouteDTO(route))
        );

        return routeDTOs;
    }

    public Set<Location> getLocations() {
        return locations;
    }

    public double getDistance() {
        return distance;
    }

    public double getTimeInMin() {
        return timeInMin;
    }
}
