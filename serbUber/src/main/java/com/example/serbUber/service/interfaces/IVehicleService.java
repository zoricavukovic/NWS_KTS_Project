package com.example.serbUber.service.interfaces;

import com.example.serbUber.dto.VehicleCurrentLocationDTO;
import com.example.serbUber.dto.VehicleDTO;
import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.model.Location;
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
    Vehicle getVehicleById(final Long id) throws EntityNotFoundException;
    double getRatingForVehicle(final Long id);
    Vehicle updateRate(final Long id, final double rate) throws EntityNotFoundException;
    void delete(final Long id);

    List<VehicleCurrentLocationDTO> getAllVehiclesForActiveDriver() throws EntityNotFoundException;

    List<VehicleCurrentLocationDTO> updateCurrentVehiclesLocation() throws EntityNotFoundException;

    VehicleDTO getVehicleDTOByVehicleType(String vehicleType);

    double getLatOfCurrentVehiclePosition(final Vehicle vehicle) throws EntityNotFoundException;

    double getLonOfCurrentVehiclePosition(final Vehicle vehicle) throws EntityNotFoundException;
}
