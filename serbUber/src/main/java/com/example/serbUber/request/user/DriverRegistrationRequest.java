package com.example.serbUber.request.user;

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

    @NotNull(message = "Vehicle data must be added.")
    @Valid
    private VehicleRequest vehicleRequest;

    public DriverRegistrationRequest(
            final String email,
            final String password,
            final String name,
            final String surname,
            final String phoneNumber,
            final String city,
            final String profilePicture,
            final String confirmPassword,
            final VehicleRequest vehicleRequest
    ) {
        super(email, password, name, surname, phoneNumber, city, profilePicture);
        this.confirmPassword = confirmPassword;
        this.vehicleRequest = vehicleRequest;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public VehicleRequest getVehicleRequest() {
        return vehicleRequest;
    }
}
