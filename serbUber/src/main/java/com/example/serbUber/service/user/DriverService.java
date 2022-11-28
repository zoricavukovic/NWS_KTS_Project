package com.example.serbUber.service.user;

import com.example.serbUber.dto.DrivingNotificationDTO;
import com.example.serbUber.dto.user.DriverDTO;
import com.example.serbUber.dto.user.UserDTO;
import com.example.serbUber.exception.*;
import com.example.serbUber.model.Vehicle;
import com.example.serbUber.model.VehicleType;
import com.example.serbUber.model.Verify;
import com.example.serbUber.model.user.Driver;
import com.example.serbUber.repository.user.DriverRepository;
import com.example.serbUber.service.DrivingNotificationService;
import com.example.serbUber.service.VehicleService;
import com.example.serbUber.service.VerifyService;
import com.example.serbUber.service.interfaces.IDriverService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;


import java.util.List;
import java.util.Optional;

import static com.example.serbUber.dto.user.DriverDTO.fromDrivers;
import static com.example.serbUber.model.user.User.passwordsDontMatch;
import static com.example.serbUber.util.Constants.ROLE_DRIVER;
import static com.example.serbUber.util.Constants.getProfilePicture;
import static com.example.serbUber.util.JwtProperties.getHashedNewUserPassword;

@Component
@Qualifier("driverServiceConfiguration")
public class DriverService implements IDriverService{

    private final DriverRepository driverRepository;
    private final VehicleService vehicleService;
    private final RoleService roleService;
    private final VerifyService verifyService;
    private final UserService userService;

    private final DrivingNotificationService drivingNotificationService;

    public DriverService(
            final DriverRepository driverRepository,
            final VehicleService vehicleService,
            final VerifyService verifyService,
            final RoleService roleService,
            final UserService userService,
            final DrivingNotificationService drivingNotificationService
            ) {
        this.driverRepository = driverRepository;
        this.vehicleService = vehicleService;
        this.verifyService = verifyService;
        this.roleService = roleService;
        this.userService = userService;
        this.drivingNotificationService = drivingNotificationService;
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
        } else if (userService.checkIfUserAlreadyExists(email)) {
            throw new EntityAlreadyExistsException(String.format("User with %s already exists.", email));
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
            verifyService.create(driver.getId(), driver.getEmail());

            return driver;
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(ROLE_DRIVER, EntityType.ROLE);
        } catch (Exception e) {
            throw new EntityAlreadyExistsException(String.format("User with %s already exists.", email));
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

    public DriverDTO getDriverForDriving(Long id) throws EntityNotFoundException {
        DrivingNotificationDTO drivingNotificationDTO = drivingNotificationService.getDrivingNotification(id);

        return null;
    }

    private List<DriverDTO> getActiveAndFreeDrivers(){
        driverRepository.getActiveAndFreeDrivers();

        return null;
    }

}
