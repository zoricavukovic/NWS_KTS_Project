package com.example.serbUber.dto;

import com.example.serbUber.model.DrivingLocationIndex;
import com.example.serbUber.model.Location;
import com.example.serbUber.model.Route;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

public class RouteDTO {

    private Long id;
    private SortedSet<DrivingLocationIndex> locations;
    private double distance; //in metres
    private double timeInMin;

    public RouteDTO(final Route route) {
        this.id = route.getId();
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

    public RouteDTO(SortedSet<DrivingLocationIndex> locations, double distance, double timeInMs) {
        this.locations = locations;
        this.distance = distance;
        this.timeInMin = getTimeInMinFromMs(timeInMs);
    }

    public SortedSet<DrivingLocationIndex> getLocations() {
        return locations;
    }

    public double getDistance() {
        return distance;
    }

    public double getTimeInMin() {
        return timeInMin;
    }

    private double getTimeInMinFromMs(final double timeInMs) {
        return timeInMs == 0 ? 0.0 :  Math.ceil((timeInMs/1000)/60L + 0.5);
    }

    public Long getId() {
        return id;
    }
}
