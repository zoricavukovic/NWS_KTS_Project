package com.example.serbUber.model;

public enum VehicleType {
    VAN,
    SUV,
    CAR;

    public static VehicleType getVehicleType(String vehicleType){
        return switch (vehicleType.toLowerCase()) {
            case "van" -> VehicleType.VAN;
            case "suv" -> VehicleType.SUV;
            default -> VehicleType.CAR;
        };
    }
}
