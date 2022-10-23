package com.example.serbUber.controller;


import com.example.serbUber.request.LocationRequest;
import com.example.serbUber.service.LocationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/locations")
public class LocationController {
    private LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }



    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@Valid @RequestBody LocationRequest locationRequest) {

        this.locationService.create(
            locationRequest.getCity(),
            locationRequest.getStreet(),
            locationRequest.getNumber(),
            locationRequest.getZipCode(),
            locationRequest.getLon(),
            locationRequest.getLat()
        );
    }
}
