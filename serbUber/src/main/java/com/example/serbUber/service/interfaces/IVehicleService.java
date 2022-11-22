package com.example.serbUber.service.interfaces;

import com.example.serbUber.dto.VehicleDTO;
import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.model.Vehicle;
import com.example.serbUber.model.VehicleType;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IVehicleService {
    Vehicle create(
            final boolean petFriendly,
            final boolean babySeat,
            final VehicleType vehicleType
    ) throws EntityNotFoundException;
    List<VehicleDTO> getAll();
    Vehicle getVehicleById(Long id) throws EntityNotFoundException;
    double getRatingForVehicle(Long id);
    Vehicle updateRate(Long id, double rate) throws EntityNotFoundException;
    void delete(Long id);
}
