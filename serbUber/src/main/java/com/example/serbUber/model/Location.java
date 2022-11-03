package com.example.serbUber.model;

import javax.persistence.*;

@Entity
@Table(name="locations")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="city", nullable = false)
    private String city;

    @Column(name="street", nullable = false)
    private String street;

    @Column(name="number", nullable = false)
    private String number;

    @Column(name="zip_code", nullable = false)
    private String zipCode;

    @Column(name="lon")
    private double lon;

    @Column(name="lat")
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

    public Location() {
    }

    public Long getId() {
        return id;
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
