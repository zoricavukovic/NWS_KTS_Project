package com.example.serbUber.service.user;

import com.example.serbUber.dto.user.DriverDTO;
import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.exception.EntityType;
import com.example.serbUber.model.Location;
import com.example.serbUber.model.Vehicle;
import com.example.serbUber.model.user.Driver;
import com.example.serbUber.repository.user.DriverRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.example.serbUber.dto.user.DriverDTO.fromDrivers;

@Service
public class DriverService {

    private final DriverRepository driverRepository;

    public DriverService(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
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
        final String name,
        final String surname,
        final String phoneNumber,
        final String city,
        final String profilePicture
    ) {
        Vehicle driverVehicle = null; //TODO: findVehicle
        Driver driver = driverRepository.save(new Driver(
            email,
            password,
            name,
            surname,
            phoneNumber,
            city,
            profilePicture,
            driverVehicle
        ));

        return new DriverDTO(driver);
    }
}
