package com.example.serbUber.dto;

import com.example.serbUber.model.Location;

import java.util.LinkedList;
import java.util.List;

public class LocationDTO {

    private final String city;
    private final String street;
    private final String number;
    private final String zipCode;
    private final double lon;
    private final double lat;

    public LocationDTO(final Location location) {
        this.city = location.getCity();
        this.street = location.getStreet();
        this.number = location.getNumber();
        this.zipCode = location.getZipCode();
        this.lon = location.getLon();
        this.lat = location.getLat();
    }

    public static List<LocationDTO> fromLocations(final List<Location> locations){
        List<LocationDTO> locationDTOs = new LinkedList<>();
        locations.forEach(location ->
            locationDTOs.add(new LocationDTO(location))
        );

        return locationDTOs;
    }

    public String getCity() {
        return city;
    }

    public String getStreet() {
        return street;
    }

    public String getNumber() {
        return number;
    }

    public String getZipCode() {
        return zipCode;
    }

    public double getLon() {
        return lon;
    }

    public double getLat() {
        return lat;
    }
}
