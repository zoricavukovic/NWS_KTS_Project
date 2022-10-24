package com.example.serbUber.service;

import com.example.serbUber.dto.VehicleDTO;
import com.example.serbUber.helper.Constants;
import com.example.serbUber.model.Vehicle;
import com.example.serbUber.model.VehicleType;
import com.example.serbUber.repository.VehicleRepository;
import com.example.serbUber.repository.VehicleTypeInfoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.serbUber.dto.VehicleDTO.fromVehicles;

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

    public void create(
            final boolean petFriendly,
            final boolean babySeat,
            final VehicleType vehicleType) {

        vehicleRepository.save(new Vehicle(
                petFriendly,
                babySeat,
                vehicleTypeInfoService.findBy(vehicleType),
                Constants.startingRate
        ));
    }

    public List<VehicleDTO> getAll() {
        List<Vehicle> vehicles = vehicleRepository.findAll();

        return fromVehicles(vehicles);
    }

    public void delete(String id) {

        vehicleRepository.deleteById(id);
    }
}
