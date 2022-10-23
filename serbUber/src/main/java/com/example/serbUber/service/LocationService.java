package com.example.serbUber.service;

import com.example.serbUber.model.Location;
import com.example.serbUber.repository.LocationRepository;
import org.springframework.stereotype.Service;

@Service
public class LocationService {

    private final LocationRepository locationRepository;

    public LocationService(final LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public void create(
        final String city,
        final String street,
        final String number,
        final String zipCode,
        final double lon,
        final double lat
    ) {

        locationRepository.save(new Location(
            city,
            street,
            number,
            zipCode,
            lon,
            lat
        ));
    }
}
