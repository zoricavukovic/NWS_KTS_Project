package com.example.serbUber.service.interfaces;

import com.example.serbUber.dto.LocationDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ILocationService {
    List<LocationDTO> getAll();
    LocationDTO create(
            final String city,
            final String street,
            final String number,
            final String zipCode,
            final double lon,
            final double lat
    );

}
