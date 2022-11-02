package com.example.serbUber.dto;

import com.example.serbUber.model.VehicleType;
import com.example.serbUber.model.VehicleTypeInfo;

import java.util.LinkedList;
import java.util.List;

public class VehicleTypeInfoDTO {

    private Long id;
    private VehicleType vehicleType;
    private double startPrice;
    private int numOfSeats;

    public VehicleTypeInfoDTO(final VehicleTypeInfo vti) {
        this.id = vti.getId();
        this.vehicleType = vti.getVehicleType();
        this.startPrice = vti.getStartPrice();
        this.numOfSeats = vti.getNumOfSeats();
    }

    public static VehicleTypeInfo toVehicleTypeInfo(VehicleTypeInfoDTO vehicleTypeInfoDTO) {

        return new VehicleTypeInfo(
            vehicleTypeInfoDTO.getVehicleType(),
            vehicleTypeInfoDTO.getStartPrice(),
            vehicleTypeInfoDTO.getNumOfSeats()
        );
    }

    public static List<VehicleTypeInfoDTO> fromVehicleTypeInfos(List<VehicleTypeInfo> vtis) {
        List<VehicleTypeInfoDTO> vtiDTOs = new LinkedList<>();
        vtis.forEach(vti ->
            vtiDTOs.add(new VehicleTypeInfoDTO(vti))
        );

        return vtiDTOs;
    }

    public Long getId() {
        return id;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public double getStartPrice() {
        return startPrice;
    }

    public int getNumOfSeats() {
        return numOfSeats;
    }

}
