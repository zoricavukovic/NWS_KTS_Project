package com.example.serbUber.service.user;

import com.example.serbUber.dto.user.DriverDTO;
import com.example.serbUber.dto.user.UserDTO;
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

import static com.example.serbUber.dto.user.DriverDTO.fromDrivers;
import static com.example.serbUber.model.user.User.passwordsDontMatch;
import static com.example.serbUber.util.Constants.ROLE_DRIVER;
import static com.example.serbUber.util.Constants.getProfilePicture;
import static com.example.serbUber.util.JwtProperties.getHashedNewUserPassword;

@Service
public class DriverService {

    private final DriverRepository driverRepository;
    private final VehicleService vehicleService;
    private final RoleService roleService;
    private final VerifyService verifyService;

    public DriverService(
            final DriverRepository driverRepository,
            final VehicleService vehicleService,
            final VerifyService verifyService,
            final RoleService roleService
    ) {
        this.driverRepository = driverRepository;
        this.vehicleService = vehicleService;
        this.verifyService = verifyService;
        this.roleService = roleService;
    }

    public List<DriverDTO> getAll() {
        List<Driver> drivers = driverRepository.findAll();

        return fromDrivers(drivers);
    }

    public DriverDTO get(Long id) throws EntityNotFoundException {
        Optional<Driver> optionalDriver = driverRepository.findById(id);

        return optionalDriver.map(DriverDTO::new)
            .orElseThrow(() ->  new EntityNotFoundException(id, EntityType.USER));
    }

    public Driver getDriverById(Long id) throws EntityNotFoundException {

        return driverRepository.getDriverById(id)
            .orElseThrow(() -> new EntityNotFoundException(id, EntityType.USER));
    }

    public UserDTO create(
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
    ) throws PasswordsDoNotMatchException, EntityNotFoundException, EntityAlreadyExistsException, MailCannotBeSentException {
        if (passwordsDontMatch(password, confirmPassword)) {
            throw new PasswordsDoNotMatchException();
        }
        Vehicle vehicle = vehicleService.create(petFriendly, babySeat, vehicleType);
        Driver driver = saveDriver(email, password, name, surname, phoneNumber, city, profilePicture, vehicle);

        return new UserDTO(driver);
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
    ) throws MailCannotBeSentException, EntityAlreadyExistsException, EntityNotFoundException {
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
                    vehicle,
                    roleService.get(ROLE_DRIVER)
            ));
            verifyService.sendEmail(driver.getId(), driver.getEmail());

            return driver;
        } catch (IllegalArgumentException e) {
            throw new EntityAlreadyExistsException(String.format("User with %s already exists.", email));
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(ROLE_DRIVER, EntityType.ROLE);
        }
    }

    public Driver updateRate(Long id, double rate) throws EntityNotFoundException {
        Driver driver = getDriverById(id);
        driver.setRate(rate);
        return driverRepository.save(driver);
    }


    public Double getDriverRating(Long id){
        return driverRepository.getRatingForDriver(id);
    }

    public UserDTO activate(final Long verifyId, final int securityCode)
            throws EntityNotFoundException, WrongVerifyTryException {
        Verify verify = verifyService.update(verifyId, securityCode);
        Driver driver = getDriverById(verify.getUserId());
        driver.setVerified(true);

        return new UserDTO(driverRepository.save(driver));
    }
}
