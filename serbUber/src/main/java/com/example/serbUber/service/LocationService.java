package com.example.serbUber.service;

import com.example.serbUber.dto.LocationDTO;
import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.exception.EntityType;
import com.example.serbUber.model.Location;
import com.example.serbUber.repository.LocationRepository;
import com.example.serbUber.service.interfaces.ILocationService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.example.serbUber.dto.LocationDTO.fromLocations;

@Component
@Qualifier("locationServiceConfiguration")
public class LocationService implements ILocationService {

    private final LocationRepository locationRepository;

    public LocationService(final LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public List<LocationDTO> getAll() {
        List<Location> locations = locationRepository.findAll();

        return fromLocations(locations);
    }

//    public LocationDTO getByCity(String city) throws EntityNotFoundException {
//        Optional<Location> optionalLocation = locationRepository.findByCity(city);
//
//        return optionalLocation.map(LocationDTO::new)
//            .orElseThrow(() ->  new EntityNotFoundException(city, EntityType.LOCATION));
//    }

    public LocationDTO createDTO(
        final String city,
        final String street,
        final String number,
        final String zipCode,
        final double lon,
        final double lat
    ) {

        return new LocationDTO(locationRepository.save(new Location(city, street, number, zipCode, lon, lat)));
    }

    public Location create(
            final String city,
            final String street,
            final String number,
            final String zipCode,
            final double lon,
            final double lat
    ) {

        return locationRepository.save(new Location(city, street, number, zipCode, lon, lat));
    }
}
