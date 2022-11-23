package com.example.serbUber.service;

import com.example.serbUber.dto.VehicleDTO;
import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.exception.EntityType;
import com.example.serbUber.model.user.Driver;
import com.example.serbUber.service.interfaces.IVehicleService;
import com.example.serbUber.util.Constants;
import com.example.serbUber.model.Vehicle;
import com.example.serbUber.model.VehicleType;
import com.example.serbUber.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.example.serbUber.dto.VehicleDTO.fromVehicles;
import static com.example.serbUber.dto.VehicleTypeInfoDTO.toVehicleTypeInfo;

@Component
@Qualifier("vehicleServiceConfiguration")
public class VehicleService implements IVehicleService {

    private final VehicleRepository vehicleRepository;
    private final VehicleTypeInfoService vehicleTypeInfoService;

    public VehicleService(
            final VehicleRepository vehicleRepository,
            final VehicleTypeInfoService vehicleTypeInfoService
    ) {
        this.vehicleRepository = vehicleRepository;
        this.vehicleTypeInfoService = vehicleTypeInfoService;
    }

    public Vehicle create(
            final boolean petFriendly,
            final boolean babySeat,
            final VehicleType vehicleType
    ) throws EntityNotFoundException {
        Vehicle vehicle = vehicleRepository.save(new Vehicle(
                petFriendly,
                babySeat,
                vehicleTypeInfoService.get(vehicleType),
                Constants.STARTING_RATE
        ));

        return vehicle;
    }

    public List<VehicleDTO> getAll() {
        List<Vehicle> vehicles = vehicleRepository.findAll();

        return fromVehicles(vehicles);
    }

    public Vehicle getVehicleById(Long id) throws EntityNotFoundException {

        return vehicleRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(id, EntityType.VEHICLE));
    }

    public double getRatingForVehicle(Long id) {

        return vehicleRepository.getVehicleRatingById(id);
    }

    public Vehicle updateRate(Long id, double rate) throws EntityNotFoundException {
        Vehicle vehicle = getVehicleById(id);
        vehicle.setRate(rate);
        return vehicleRepository.save(vehicle);
    }

    public void delete(Long id) {

        vehicleRepository.deleteById(id);
    }
}
