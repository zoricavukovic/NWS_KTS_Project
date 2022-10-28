package com.example.serbUber.service.user;

import com.example.serbUber.dto.user.DriverDTO;
import com.example.serbUber.exception.EntityAlreadyExistsException;
import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.exception.EntityType;
import com.example.serbUber.exception.PasswordsDoNotMatchException;
import com.example.serbUber.model.Vehicle;
import com.example.serbUber.model.VehicleType;
import com.example.serbUber.model.user.Driver;
import com.example.serbUber.repository.user.DriverRepository;
import com.example.serbUber.service.VehicleService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.example.serbUber.dto.VehicleDTO.toVehicle;
import static com.example.serbUber.dto.user.DriverDTO.fromDrivers;
import static com.example.serbUber.model.user.User.passwordsMatch;
import static com.example.serbUber.util.JwtProperties.getHashedNewUserPassword;

@Service
public class DriverService {

    private final DriverRepository driverRepository;

    private final VehicleService vehicleService;

    public DriverService(
            final DriverRepository driverRepository,
            final VehicleService vehicleService) {
        this.driverRepository = driverRepository;
        this.vehicleService = vehicleService;
    }

    public List<DriverDTO> getAll() {
        List<Driver> drivers = driverRepository.findAll();

        return fromDrivers(drivers);
    }

    public DriverDTO get(String email) throws EntityNotFoundException {
        Optional<Driver> optionalDriver = driverRepository.getDriverByEmail(email);

        return optionalDriver.map(DriverDTO::new)
            .orElseThrow(() ->  new EntityNotFoundException(email, EntityType.USER));
    }

    public DriverDTO create(
            final String email,
            final String password,
            final String confirmPassword,
            final String name,
            final String surname,
            final String phoneNumber,
            final String city,
            final String profilePicture,
            final boolean petFriendly,
            final boolean babySeat,
            final VehicleType vehicleType
            ) throws EntityNotFoundException, PasswordsDoNotMatchException, EntityAlreadyExistsException {

        if (!passwordsMatch(password, confirmPassword)) {
            throw new PasswordsDoNotMatchException();
        }
        Vehicle vehicle = toVehicle(vehicleService.create(petFriendly, babySeat, vehicleType));

        try {
            Driver driver = driverRepository.save(new Driver(
                    email,
                    getHashedNewUserPassword(password),
                    name,
                    surname,
                    phoneNumber,
                    city,
                    (profilePicture == null) ? "default.jpg" : profilePicture,
                    vehicle
            ));

            return new DriverDTO(driver);

        } catch (Exception e) {
            throw new EntityAlreadyExistsException("User with " + email + " already exists.");
        }
    }
}
