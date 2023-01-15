package com.example.serbUber.service;

import com.example.serbUber.dto.VehicleTypeInfoDTO;
import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.exception.EntityType;
import com.example.serbUber.model.VehicleType;
import com.example.serbUber.model.VehicleTypeInfo;
import com.example.serbUber.repository.VehicleTypeInfoRepository;
import com.example.serbUber.service.interfaces.IVehicleTypeInfoService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.example.serbUber.dto.VehicleTypeInfoDTO.fromVehicleTypeInfos;

@Component
@Qualifier("vehicleTypeInfoServiceConfiguration")
public class VehicleTypeInfoService implements IVehicleTypeInfoService {

    private final VehicleTypeInfoRepository vehicleTypeInfoRepository;

    public VehicleTypeInfoService(final VehicleTypeInfoRepository vehicleTypeInfoRepository) {
        this.vehicleTypeInfoRepository = vehicleTypeInfoRepository;
    }

    public List<VehicleTypeInfoDTO> getAll() {
        List<VehicleTypeInfo> vehicleTypeInfos = vehicleTypeInfoRepository.findAll();

        return fromVehicleTypeInfos(vehicleTypeInfos);
    }

    public VehicleTypeInfoDTO create(
            final VehicleType vehicleType,
            final double startPrice,
            final int numOfSeats
    ) {

        return new VehicleTypeInfoDTO(vehicleTypeInfoRepository.save(new VehicleTypeInfo(
                vehicleType,
                startPrice,
                numOfSeats
        )));
    }

    public VehicleTypeInfoDTO findBy(VehicleType vehicleType) throws EntityNotFoundException {
        Optional<VehicleTypeInfo> vehicleTypeInfoOpt = vehicleTypeInfoRepository.findByVehicleType(vehicleType);

        return vehicleTypeInfoOpt.map(VehicleTypeInfoDTO::new).orElseThrow(() ->
                new EntityNotFoundException(vehicleType.toString(), EntityType.VEHICLE_TYPE_INFO));
    }

    public double getPriceForVehicle(VehicleType vehicleType) throws EntityNotFoundException {
        return findBy(vehicleType).getStartPrice();
    }

    public double getPriceForVehicleAndChosenRoute(double kilometers, VehicleType vehicleType) throws EntityNotFoundException {
        double priceForType = getPriceForVehicle(vehicleType);
        return priceForType + (kilometers/1000)*1; // *1 token -> 1token=1e
    }

    public VehicleTypeInfo get(VehicleType vehicleType) throws EntityNotFoundException {

        return vehicleTypeInfoRepository.getVehicleTypeInfoByName(vehicleType)
                .orElseThrow(() -> new EntityNotFoundException(vehicleType.toString(), EntityType.VEHICLE_TYPE_INFO));

    }

    public VehicleTypeInfoDTO getDTO(VehicleType vehicleType) throws EntityNotFoundException {

        return new VehicleTypeInfoDTO(get(vehicleType));
    }

    public boolean isCorrectNumberOfSeats(String vehicleType, int numberOfPassengers) throws EntityNotFoundException {
        VehicleTypeInfo vehicleTypeInfo = get(VehicleType.getVehicleType(vehicleType));
        return vehicleTypeInfo.getNumOfSeats() >= numberOfPassengers;
    }

}
