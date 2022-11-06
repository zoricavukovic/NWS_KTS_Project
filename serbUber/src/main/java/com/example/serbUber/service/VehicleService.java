package com.example.serbUber.service;

import com.example.serbUber.dto.VehicleDTO;
import com.example.serbUber.dto.user.DriverDTO;
import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.model.user.Driver;
import com.example.serbUber.service.user.DriverService;
import com.example.serbUber.util.Constants;
import com.example.serbUber.model.Vehicle;
import com.example.serbUber.model.VehicleType;
import com.example.serbUber.repository.VehicleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.serbUber.dto.VehicleDTO.fromVehicles;
import static com.example.serbUber.dto.VehicleTypeInfoDTO.toVehicleTypeInfo;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final VehicleTypeInfoService vehicleTypeInfoService;

    public VehicleService(
            final VehicleRepository vehicleRepository,
            final VehicleTypeInfoService vehicleTypeInfoService
    ) {
        this.vehicleRepository = vehicleRepository;
        this.vehicleTypeInfoService = vehicleTypeInfoService;
    }

    public VehicleDTO create(
            final boolean petFriendly,
            final boolean babySeat,
            final VehicleType vehicleType
    ) throws EntityNotFoundException {
        Vehicle vehicle = vehicleRepository.save(new Vehicle(
                petFriendly,
                babySeat,
                toVehicleTypeInfo(vehicleTypeInfoService.findBy(vehicleType)),
                Constants.STARTING_RATE
        ));

        return new VehicleDTO(vehicle);
    }

    public List<VehicleDTO> getAll() {
        List<Vehicle> vehicles = vehicleRepository.findAll();

        return fromVehicles(vehicles);
    }

    public double getRatingForVehicle(Long id) {;
        return vehicleRepository.getVehicleRatingById(id);
    }

    public void delete(Long id) {

        vehicleRepository.deleteById(id);
    }
}
