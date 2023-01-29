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

import java.util.List;
import java.util.Optional;

import static com.example.serbUber.dto.VehicleTypeInfoDTO.fromVehicleTypeInfos;
import static com.example.serbUber.util.Constants.ONE_KILOMETER_TO_METER;
import static com.example.serbUber.util.Constants.TOKEN_VALUE;

@Component
@Qualifier("vehicleTypeInfoServiceConfiguration")
public class VehicleTypeInfoService implements IVehicleTypeInfoService {

    private VehicleTypeInfoRepository vehicleTypeInfoRepository;

    public VehicleTypeInfoService(VehicleTypeInfoRepository vehicleTypeInfoRepository) {
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



    public double getPriceForVehicle(VehicleType vehicleType) throws EntityNotFoundException {
        return get(vehicleType).getStartPrice();
    }

    public double getPriceForVehicleAndChosenRoute(double kilometers, VehicleType vehicleType) throws EntityNotFoundException {
        double priceForType = getPriceForVehicle(vehicleType);

        return Math.ceil(priceForType + (kilometers/ONE_KILOMETER_TO_METER)*TOKEN_VALUE);
    }

    public double getAveragePriceForChosenRoute(double kilometers) throws EntityNotFoundException {
        double totalPrice = 0;
        for(VehicleType vehicleType : VehicleType.values()){
            totalPrice += getPriceForVehicleAndChosenRoute(kilometers, vehicleType);
        }

        return Math.ceil(totalPrice/VehicleType.values().length);
    }

    public VehicleTypeInfo get(VehicleType vehicleType) throws EntityNotFoundException {

        return vehicleTypeInfoRepository.getVehicleTypeInfoByName(vehicleType)
                .orElseThrow(() -> new EntityNotFoundException(vehicleType.toString(), EntityType.VEHICLE_TYPE_INFO));
    }

    public VehicleTypeInfoDTO getDTO(VehicleType vehicleType) throws EntityNotFoundException {

        return new VehicleTypeInfoDTO(get(vehicleType));
    }

    public boolean isCorrectNumberOfSeats(VehicleTypeInfo vehicleTypeInfo, int numberOfPassengers) {

        return vehicleTypeInfo.getNumOfSeats() >= numberOfPassengers;
    }

}
