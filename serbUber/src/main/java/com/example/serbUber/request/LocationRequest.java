package com.example.serbUber.request;

import com.example.serbUber.util.Constants;

import javax.validation.constraints.*;

import static com.example.serbUber.exception.ErrorMessagesConstants.WRONG_CITY;
import static com.example.serbUber.util.Constants.POSITIVE_INT_NUM_REG;

public class LocationRequest {

    @NotBlank(message = WRONG_CITY)
    @Pattern(regexp = Constants.LEGIT_NAME_REG, message = WRONG_CITY)
    private String city;

    @NotBlank(message = "Street cannot be empty.")
    @Size(min = 2, max = 50, message = "Street cannot be longer than 50 characters nor shorter tha 2")
    private String street;

    @NotBlank(message = "Street number must be selected")
    @Pattern(regexp = POSITIVE_INT_NUM_REG, message = "Street number must be whole number.")
    private String number;

    @NotBlank(message = "Zip code must be selected.")
    @Pattern(regexp = POSITIVE_INT_NUM_REG, message = "Zip code must be whole number.")
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

    public LocationRequest(double lon, double lat) {
        this.lon = lon;
        this.lat = lat;
    }

    public LocationRequest(){}

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
