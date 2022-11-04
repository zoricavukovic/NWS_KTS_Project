package com.example.serbUber.service.user;

import com.example.serbUber.dto.user.DriverDTO;
import com.example.serbUber.exception.*;
import com.example.serbUber.model.Vehicle;
import com.example.serbUber.model.VehicleType;
import com.example.serbUber.model.Verify;
import com.example.serbUber.model.user.Driver;
import com.example.serbUber.repository.user.DriverRepository;
import com.example.serbUber.service.VehicleService;
import com.example.serbUber.service.VerifyService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.example.serbUber.dto.VehicleDTO.toVehicle;
import static com.example.serbUber.dto.user.DriverDTO.fromDrivers;
import static com.example.serbUber.model.user.User.passwordsMatch;
import static com.example.serbUber.util.Constants.getProfilePicture;
import static com.example.serbUber.util.JwtProperties.getHashedNewUserPassword;

@Service
public class DriverService {

    private final DriverRepository driverRepository;
    private final VehicleService vehicleService;
    private final VerifyService verifyService;

    public DriverService(
            final DriverRepository driverRepository,
            final VehicleService vehicleService,
            final VerifyService verifyService
    ) {
        this.driverRepository = driverRepository;
        this.vehicleService = vehicleService;
        this.verifyService = verifyService;
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

    public Driver getById(Long id) throws EntityNotFoundException {
        Optional<Driver> optionalDriver = driverRepository.getDriverById(id);

        if (optionalDriver.isPresent()){
            return optionalDriver.get();
        } else {
            throw new EntityNotFoundException(id, EntityType.USER);
        }
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
    ) throws EntityNotFoundException,
            PasswordsDoNotMatchException,
            EntityAlreadyExistsException,
            MailCannotBeSentException
    {
        if (!passwordsMatch(password, confirmPassword)) {
            throw new PasswordsDoNotMatchException();
        }
        Vehicle vehicle = toVehicle(vehicleService.create(petFriendly, babySeat, vehicleType));
        Driver driver = saveDriver(email, password, name, surname, phoneNumber, city, profilePicture, vehicle);

        return new DriverDTO(driver);
    }

    private Driver saveDriver(
            final String email,
            final String password,
            final String name,
            final String surname,
            final String phoneNumber,
            final String city,
            final String profilePicture,
            final Vehicle vehicle
    ) throws MailCannotBeSentException, EntityAlreadyExistsException {
        try {
            String hashedPassword = getHashedNewUserPassword(password);
            Driver driver = driverRepository.save(new Driver(
                    email,
                    hashedPassword,
                    name,
                    surname,
                    phoneNumber,
                    city,
                    getProfilePicture(profilePicture),
                    vehicle
            ));
            verifyService.sendEmail(driver.getId(), driver.getEmail());

            return driver;
        }catch (MailCannotBeSentException e) {
            throw new MailCannotBeSentException(e.getMessage());
        } catch (Exception e) {
            throw new EntityAlreadyExistsException(String.format("User with %s already exists.", email));
        }
    }


    public void activate(final Long verifyId, final int securityCode)
            throws EntityNotFoundException, WrongVerifyTryException
    {
        try {
            Verify verify = verifyService.update(verifyId, securityCode);
            Driver driver = getById(verify.getUserId());
            driver.setVerified(true);
            driverRepository.save(driver);
        } catch (WrongVerifyTryException e) {
            throw new WrongVerifyTryException(e.getMessage());
        }

    }
}
