package com.example.serbUber.service;

import com.example.serbUber.dto.VehicleTypeInfoDTO;
import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.exception.EntityType;
import com.example.serbUber.model.VehicleType;
import com.example.serbUber.model.VehicleTypeInfo;
import com.example.serbUber.repository.VehicleTypeInfoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.example.serbUber.dto.VehicleTypeInfoDTO.fromVehicleTypeInfos;

@Service
public class VehicleTypeInfoService {

    private final VehicleTypeInfoRepository vehicleTypeInfoRepository;

    public VehicleTypeInfoService(final VehicleTypeInfoRepository vehicleTypeInfoRepository) {
        this.vehicleTypeInfoRepository = vehicleTypeInfoRepository;
    }

    public List<VehicleTypeInfoDTO> getAll() {
        List<VehicleTypeInfo> vehicleTypeInfos = vehicleTypeInfoRepository.findAll();

        return fromVehicleTypeInfos(vehicleTypeInfos);
    }

    public void create(
            final VehicleType vehicleType,
            final double startPrice,
            final int numOfSeats
    ) {

        vehicleTypeInfoRepository.save(new VehicleTypeInfo(
           vehicleType,
           startPrice,
           numOfSeats
        ));
    }

    public VehicleTypeInfoDTO findBy(VehicleType vehicleType) throws EntityNotFoundException {
        Optional<VehicleTypeInfo> vehicleTypeInfoOpt = vehicleTypeInfoRepository.findByVehicleType(vehicleType);

        return vehicleTypeInfoOpt.map(VehicleTypeInfoDTO::new).orElseThrow(() ->
                new EntityNotFoundException(vehicleType.toString(), EntityType.VEHICLE_TYPE_INFO));
    }
}
