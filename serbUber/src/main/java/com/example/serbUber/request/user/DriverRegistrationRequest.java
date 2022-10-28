package com.example.serbUber.request.user;

import com.example.serbUber.model.Vehicle;
import com.example.serbUber.request.VehicleRequest;
import com.example.serbUber.util.Constants;

import javax.validation.constraints.*;

public class DriverRegistrationRequest {

    @Email(message = "Email is not in the right format.")
    @NotBlank(message = "Email cannot be empty.")
    @Size(max = 1024, message = "Email length is too long.")
    private final String email;

    @Pattern(regexp = Constants.LEGIT_PASSWORD_REG,
            message = "Password must contain at least 5 characters. At least one number and one special character.")
    private final String password;

    @Pattern(regexp = Constants.LEGIT_PASSWORD_REG,
            message = "Password must contain at least 5 characters. At least one number and one special character.")
    private final String confirmPassword;

    @Pattern(regexp = Constants.LEGIT_NAME_REG, message = "Name must contain only letters and cannot be too long.")
    private final String name;

    @Pattern(regexp = Constants.LEGIT_NAME_REG, message = "Surname must contain betwen 2 and 30 letters.")
    private final String surname;

    @Pattern(regexp = Constants.LEGIT_PHONE_NUMBER_REG, message = "Phone number must contain 9 digits.")
    private final String phoneNumber;

    @Pattern(regexp = Constants.LEGIT_NAME_REG, message = "City must contain betwen 2 and 30 letters.")
    private String city;

    @NotNull(message = "Vehicle data must be added.")
    private VehicleRequest vehicleRequest;

    private final String profilePicture;

    public DriverRegistrationRequest(
            final String email,
            final String password,
            final String confirmPassword,
            final String name,
            final String surname,
            final String phoneNumber,
            final String city,
            VehicleRequest vehicleRequest,
            final String profilePicture
    ) {
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.name = name;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
        this.city = city;
        this.vehicleRequest = vehicleRequest;
        this.profilePicture = profilePicture;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getCity() {
        return city;
    }

    public VehicleRequest getVehicleRequest() {
        return vehicleRequest;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }
}
