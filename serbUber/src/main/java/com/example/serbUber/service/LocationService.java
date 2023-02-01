package com.example.serbUber.service;

import com.example.serbUber.dto.LocationDTO;
import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.exception.EntityType;
import com.example.serbUber.model.Location;
import com.example.serbUber.repository.LocationRepository;
import com.example.serbUber.service.interfaces.ILocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.example.serbUber.dto.LocationDTO.fromLocations;

@Component
@Qualifier("locationServiceConfiguration")
public class LocationService implements ILocationService {

    private LocationRepository locationRepository;

    @Autowired
    public LocationService(final LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public List<LocationDTO> getAll() {
        List<Location> locations = locationRepository.findAll();

        return fromLocations(locations);
    }

    public Location get(Long id) throws EntityNotFoundException {

        return locationRepository.findById(id)
            .orElseThrow(() ->  new EntityNotFoundException(id, EntityType.LOCATION));
    }

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

    public Location tryToFindLocation(final double lon, final double lat) {
        List<Location> locations = locationRepository.findByLonAndLat(lon, lat);

        return locations.size() > 0 ? locations.get(0) : null;
    }
}
