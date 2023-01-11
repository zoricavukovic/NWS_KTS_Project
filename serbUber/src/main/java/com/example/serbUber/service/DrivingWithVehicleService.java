package com.example.serbUber.service;

import com.example.serbUber.dto.LocationDTO;
import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.exception.EntityType;
import com.example.serbUber.model.Location;
import com.example.serbUber.repository.DrivingWithVehicleRepository;
import com.example.serbUber.repository.LocationRepository;
import com.example.serbUber.service.interfaces.ILocationService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.serbUber.dto.LocationDTO.fromLocations;

@Service
public class DrivingWithVehicleService {

    private final DrivingWithVehicleRepository drivingWithVehicleRepository;

    public DrivingWithVehicleService(final DrivingWithVehicleRepository drivingWithVehicleRepository) {
        this.drivingWithVehicleRepository = drivingWithVehicleRepository;
    }

}
