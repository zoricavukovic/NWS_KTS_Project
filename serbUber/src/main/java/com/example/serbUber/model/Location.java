package com.example.serbUber.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "locations")
public class Location {
    @Id
    private String id;

    private String city;
    private String street;
    private String number;
    private String zipCode;
    private double lon;
    private double lat;

    public Location(
        final String city,
        final String street,
        final String number,
        final String zipCode,
        final double lon,
        final double lat
    ) {
        this.city = city;
        this.street = street;
        this.number = number;
        this.zipCode = zipCode;
        this.lon = lon;
        this.lat = lat;
    }

    public String getId() {
        return id;
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
