package com.example.serbUber.service;

import com.example.serbUber.dto.LocationDTO;
import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.exception.EntityType;
import com.example.serbUber.model.Location;
import com.example.serbUber.repository.LocationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.example.serbUber.dto.LocationDTO.fromLocations;

@Service
public class LocationService {

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

    public void create(
        final String city,
        final String street,
        final String number,
        final String zipCode,
        final double lon,
        final double lat
    ) {

        locationRepository.save(new Location(city, street, number, zipCode, lon, lat));
    }
}
