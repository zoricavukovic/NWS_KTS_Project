package com.example.serbUber.service.interfaces;

import com.example.serbUber.dto.VehicleTypeInfoDTO;
import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.model.VehicleType;
import com.example.serbUber.model.VehicleTypeInfo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IVehicleTypeInfoService {
    List<VehicleTypeInfoDTO> getAll();

    VehicleTypeInfoDTO create(
            final VehicleType vehicleType,
            final double startPrice,
            final int numOfSeats
    );

    VehicleTypeInfoDTO findBy(VehicleType vehicleType) throws EntityNotFoundException;

    VehicleTypeInfo get(VehicleType vehicleType) throws EntityNotFoundException;
}
