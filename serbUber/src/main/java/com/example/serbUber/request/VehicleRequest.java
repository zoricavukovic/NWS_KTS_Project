package com.example.serbUber.request;

import com.example.serbUber.model.VehicleType;
import com.example.serbUber.model.VehicleTypeInfo;

import javax.validation.constraints.NotNull;

public class VehicleRequest {

    @NotNull(message = "Pet friendly option must be selected!")
    private boolean petFriendly;

    @NotNull(message = "Baby seat option must be selected!")
    private boolean babySeat;

    @NotNull(message = "Vehicle type must be selected!")
    private VehicleType vehicleType;

    public VehicleRequest(
            final boolean petFriendly,
            final boolean babySeat,
            final VehicleType vehicleType
    ) {
        this.petFriendly = petFriendly;
        this.babySeat = babySeat;
        this.vehicleType = vehicleType;
    }

    public boolean isPetFriendly() {
        return petFriendly;
    }

    public boolean isBabySeat() {
        return babySeat;
    }

    public void setPetFriendly(boolean petFriendly) {
        this.petFriendly = petFriendly;
    }

    public void setBabySeat(boolean babySeat) {
        this.babySeat = babySeat;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }
}
