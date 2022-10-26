package com.example.serbUber.request;

import javax.validation.constraints.Size;

public class LocationRequest {
    @Size(max = 1024, message = "{validation.name.size.too_long}")
    private String city;

    @Size(max = 1024, message = "{validation.name.size.too_long}")
    private String street;

    @Size(max = 1024, message = "{validation.name.size.too_long}")
    private String number;

    @Size(max = 1024, message = "{validation.name.size.too_long}")
    private String zipCode;

    private double lon;
    private double lat;

    public LocationRequest(
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }
}
