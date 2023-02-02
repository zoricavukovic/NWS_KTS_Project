package com.example.serbUber.request.user;

import com.example.serbUber.model.VehicleType;
import com.example.serbUber.request.VehicleRequest;
import com.example.serbUber.util.Constants;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import static com.example.serbUber.exception.ErrorMessagesConstants.WRONG_PASSWORD;

public class DriverRegistrationRequest extends UserRequest {

    @NotBlank(message = WRONG_PASSWORD)
    @Pattern(regexp = Constants.LEGIT_PASSWORD_REG,
            message = WRONG_PASSWORD)
    private final String confirmPassword;

    @NotNull(message = "Pet friendly option must be selected!")
    private boolean petFriendly;

    @NotNull(message = "Baby seat option must be selected!")
    private boolean babySeat;

    @NotNull(message = "Vehicle type must be selected!")
    private VehicleType vehicleType;

    public DriverRegistrationRequest(
            final String email,
            final String password,
            final String name,
            final String surname,
            final String phoneNumber,
            final String city,
            final String profilePicture,
            final String confirmPassword,
            final boolean petFriendly,
            final boolean babySeat,
            final VehicleType vehicleType
    ) {
        super(email, password, name, surname, phoneNumber, city, profilePicture);
        this.confirmPassword = confirmPassword;
        this.petFriendly = petFriendly;
        this.babySeat = babySeat;
        this.vehicleType = vehicleType;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public boolean isPetFriendly() {
        return petFriendly;
    }

    public void setPetFriendly(boolean petFriendly) {
        this.petFriendly = petFriendly;
    }

    public boolean isBabySeat() {
        return babySeat;
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
