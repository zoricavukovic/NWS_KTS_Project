package com.example.serbUber.service.user;

import com.example.serbUber.dto.DriverActivityResetNotificationDTO;
import com.example.serbUber.dto.DrivingNotificationDTO;
import com.example.serbUber.dto.DrivingStatusNotificationDTO;
import com.example.serbUber.dto.user.DriverDTO;
import com.example.serbUber.dto.user.DriverPageDTO;
import com.example.serbUber.dto.user.UserDTO;
import com.example.serbUber.exception.*;
import com.example.serbUber.model.*;
import com.example.serbUber.model.user.Driver;
import com.example.serbUber.repository.user.DriverRepository;
import com.example.serbUber.service.*;
import com.example.serbUber.service.interfaces.IDriverService;
import com.example.serbUber.util.Constants;
import com.graphhopper.GHRequest;
import com.graphhopper.GHResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static com.example.serbUber.SerbUberApplication.hopper;
import static com.example.serbUber.dto.user.DriverDTO.fromDrivers;
import static com.example.serbUber.dto.user.DriverPageDTO.fromDriversPage;
import static com.example.serbUber.exception.ErrorMessagesConstants.ACTIVE_DRIVING_IN_PROGRESS_MESSAGE;
import static com.example.serbUber.exception.ErrorMessagesConstants.UNBLOCK_UNBLOCKED_USER_MESSAGE;
import static com.example.serbUber.util.Constants.*;
import static com.example.serbUber.util.JwtProperties.getHashedNewUserPassword;
import static com.example.serbUber.util.PictureHandler.convertPictureToBase64ByName;

@Component
@Qualifier("driverServiceConfiguration")
public class DriverService implements IDriverService{

    private final DriverRepository driverRepository;
    private final VehicleService vehicleService;
    private final RoleService roleService;
    private final VerifyService verifyService;
    private final EmailService emailService;
    private final WebSocketService webSocketService;

    public DriverService(
            final DriverRepository driverRepository,
            final VehicleService vehicleService,
            final VerifyService verifyService,
            final RoleService roleService,
            final WebSocketService webSocketService,
            final EmailService emailService
            ) {
        this.driverRepository = driverRepository;
        this.vehicleService = vehicleService;
        this.verifyService = verifyService;
        this.roleService = roleService;
        this.webSocketService = webSocketService;
        this.emailService = emailService;
    }


    public List<DriverDTO> getAll() {
        List<Driver> drivers = driverRepository.findAllVerified();
        List<DriverDTO> driverDTOs = fromDrivers(drivers);
        driverDTOs.forEach(user ->  user.setProfilePicture(convertPictureToBase64ByName(user.getProfilePicture())));

        return driverDTOs;
    }

    public DriverDTO get(final Long id) throws EntityNotFoundException {
        Optional<Driver> optionalDriver = driverRepository.getDriverById(id);

        DriverDTO driverDTO = optionalDriver.map(DriverDTO::new)
            .orElseThrow(() ->  new EntityNotFoundException(id, EntityType.USER));
        driverDTO.setProfilePicture(convertPictureToBase64ByName(driverDTO.getProfilePicture()));

        return driverDTO;
    }

    public Driver getDriverById(final Long id) throws EntityNotFoundException {

        return driverRepository.getDriverById(id)
            .orElseThrow(() -> new EntityNotFoundException(id, EntityType.USER));
    }

    public Driver getDriverByIdWithoutDrivings(final Long id) throws EntityNotFoundException {

        return driverRepository.getDriverByIdWithoutDrivings(id)
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

    @Transactional
    public Driver updateRate(final Long id, final double rate) throws EntityNotFoundException {
        Driver driver = getDriverByIdWithoutDrivings(id);
        driver.setRate(rate);
        driverRepository.updateDrivingRate(id, rate);
        return driver;
    }


    public Double getDriverRating(final Long id){
        return driverRepository.getRatingForDriver(id);
    }


    public Driver getDriverForDriving( DrivingNotification drivingNotification) throws EntityNotFoundException {
        LocalDateTime startDate = drivingNotification.getStarted();
        LocalDateTime endDate = drivingNotification.getStarted().plusMinutes(drivingNotification.getDuration());
        Vehicle vehicle = drivingNotification.getVehicle();
        Location startLocation = drivingNotification.getRoute().getLocations().first().getLocation();
        List<Driver> activeAndFreeDrivers = getActiveAndFreeDrivers(startDate, endDate, vehicle.getVehicleTypeInfo().getVehicleType());
        if(activeAndFreeDrivers.size() == 0) { // nema trenutno slobodnih aktivnih vozaca
            activeAndFreeDrivers = getFutureFreeDrivers(startDate, endDate, vehicle.getVehicleTypeInfo().getVehicleType());
        }

       return activeAndFreeDrivers.size() > 0 ?
               findNearestDriver(activeAndFreeDrivers, startLocation.getLon(), startLocation.getLat())
               : null;
    }

    private Driver findNearestDriver(List<Driver> activeAndFreeDrivers, double lonStart, double latStart) throws EntityNotFoundException {
        double latEnd = vehicleService.getLatOfCurrentVehiclePosition(activeAndFreeDrivers.get(0).getVehicle());
        double lonEnd = vehicleService.getLonOfCurrentVehiclePosition(activeAndFreeDrivers.get(0).getVehicle());;
        double minDistance = getDistance(lonStart, latStart, lonEnd, latEnd);
        Driver nearestDriver = activeAndFreeDrivers.get(0);
        for(Driver driver : activeAndFreeDrivers){
            double newMinDistance = getDistance(
                lonStart,
                latStart,
                vehicleService.getLonOfCurrentVehiclePosition(driver.getVehicle()),
                vehicleService.getLatOfCurrentVehiclePosition(driver.getVehicle())
            );
            if(newMinDistance < minDistance){
                minDistance = newMinDistance;
                nearestDriver = driver;
            }
        }
        return nearestDriver;
    }

    private double getDistance(double lonStart, double latStart, double lonEnd, double latEnd){
        GHRequest request = new GHRequest(latStart, lonStart, latEnd, lonEnd);
        request.setProfile("car");
        GHResponse route = hopper.route(request);
        return route.getBest().getDistance();
    }


    private List<Driver> getFutureFreeDrivers(LocalDateTime startDate, LocalDateTime endDate, VehicleType vehicleType){
        List<Driver> busyDriversNow = driverRepository.getBusyDriversNow(startDate, vehicleType);
        List<Driver> futureFreeDrivers = new LinkedList<>();
        busyDriversNow.forEach(driver -> {
            driver.getDrivings().forEach(driving -> {
                if(driving.isActive() && driving.getEnd().isBefore(startDate.plusMinutes(3))){
                    futureFreeDrivers.add(driver);
                }
            });
        });
       return futureFreeDrivers;
    }

    private List<Driver> getActiveAndFreeDrivers(LocalDateTime startDate, LocalDateTime endDate, VehicleType vehicleType) {
        return driverRepository.getActiveAndFreeDrivers(startDate, endDate, vehicleType);
    }

    public List<DriverPageDTO> getDriversWithPagination(int pageNumber, int pageSize) {
        Pageable page = PageRequest.of(pageNumber, pageSize);
        Page<Driver> results = getDriverPage(page);
        return fromDriversPage(results.getContent(), results.getSize(), results.getTotalPages());
    }

    public Page<Driver> getDriverPage(Pageable page){
        return driverRepository.findAll(page);
    }

    public DriverDTO updateActivityStatus(final Long id, boolean active)
            throws EntityNotFoundException, ActivityStatusCannotBeChangedException
    {
        Driver driver = getDriverById(id);

        if (canChangeStatus(active, driver.getWorkingMinutes(), driver.getEndShift(), driver.getDrivings())) {
            driver.setActive(active);
        } else if (checkIfStartingNewShift(driver.getEndShift())) {
            driver.setActive(true);
            driver.setStartShift(LocalDateTime.now());
            driver.setWorkingMinutes(START_WORKING_MINUTES);
            driver.setEndShift(LocalDateTime.now().plusHours(HOURS_IN_A_DAY));
        }

        return new DriverDTO(driverRepository.save(driver));
    }

    public Driver onDriverLogout(final Long id)
            throws EntityNotFoundException, ActivityStatusCannotBeChangedException
    {
        Driver driver = getDriverById(id);
        this.checkIfCurrentDrivingNotInProgress(driver.getDrivings());
        driver.setActive(false);
        driver.setOnline(false);

        return driverRepository.save(driver);
    }

    public void resetActivityForDrivers() {
        List<Driver> drivers = driverRepository.getAllWithDrivings();
        drivers.forEach(driver -> {
            if (driver.isActive() && !driver.isBlocked()) {
                driver.incementWorkingMinutes();
                changeStatusIfNeeded(driver);
            }
        });
    }

    private Driver changeStatusIfNeeded(final Driver driver) {
        if (this.canResetDriverStatus(driver)) {
            driver.setActive(false);
            this.webSocketService.sendActivityResetNotification(new DriverActivityResetNotificationDTO(driver));
        }

        return driverRepository.save(driver);
    }

    private boolean canResetDriverStatus(final Driver driver) {

        return this.checkWorkingHoursOvertime(driver.getWorkingMinutes(), driver.getEndShift())
                && !this.checkIfDrivingInProgress(driver.getDrivings());
    }

    public Driver onDriverLogin(final Long id)
            throws EntityNotFoundException
    {
        Driver driver = getDriverById(id);
        driver = this.setDriverLoginData(driver);
        driver.setOnline(true);

        return driverRepository.save(driver);
    }

    public boolean blockDriver(final Long id, final String reason)
            throws EntityNotFoundException, EntityUpdateException
    {
        Driver driver = getDriverById(id);
        if (checkIfDrivingInProgress(driver.getDrivings())) {
            throw new EntityUpdateException("Driver cannot be blocked until he finishes his driving.");
        }

        driver.setOnline(false);
        driver.setActive(false);
        driver.setBlocked(true);
        driver.setVerified(false);
        driverRepository.save(driver);
        emailService.sendBlockDriverMail(driver.getEmail(), reason);
        webSocketService.sendBlockedNotification(driver.getEmail(), reason);

        return true;
    }

    public boolean getIsBlocked(Long id) {

        return driverRepository.getIsBlocked(id);
    }

    public boolean unblock(Long id)
            throws EntityNotFoundException, EntityUpdateException {
        Driver driver = getDriverById(id);
        if (!driver.isBlocked()) {
            throw new EntityUpdateException(UNBLOCK_UNBLOCKED_USER_MESSAGE);
        }
        driver.setBlocked(false);
        driver.setVerified(true);
        driverRepository.save(driver);

        return true;
    }

    private Driver setDriverLoginData(final Driver driver) {
        if (checkIfStartShiftEmptyOrPassed(driver.getStartShift(), driver.getEndShift())) {
            driver.setActive(true);
            driver.setStartShift(LocalDateTime.now());
            driver.setWorkingMinutes(START_WORKING_MINUTES);
            driver.setEndShift(LocalDateTime.now().plusHours(HOURS_IN_A_DAY));
        } else {
            driver.setActive(!checkWorkingHoursOvertime(driver.getWorkingMinutes(), driver.getEndShift()));
        }

        return driver;
    }

    private boolean checkIfStartShiftEmptyOrPassed(
            final LocalDateTime startShift,
            final LocalDateTime endShift
    ) {

        return (startShift == null || endShift.isBefore(LocalDateTime.now()));
    }

    private boolean checkIfStartingNewShift(final LocalDateTime endShift) {

        return LocalDateTime.now().isAfter(endShift);
    }

    private boolean canChangeStatus(
            final boolean active,
            final int workingMinutes,
            final LocalDateTime endShift,
            final List<Driving> drivings
    ) throws ActivityStatusCannotBeChangedException {

        return active ? checkIfActiveStatusAllowed(workingMinutes, endShift)
                : checkIfCurrentDrivingNotInProgress(drivings);
    }

    private boolean checkIfCurrentDrivingNotInProgress(final List<Driving> drivings)
            throws ActivityStatusCannotBeChangedException
    {
        for (Driving driving : drivings){
            if (driving.isActive()) {
                throw new ActivityStatusCannotBeChangedException(ACTIVE_DRIVING_IN_PROGRESS_MESSAGE);
            }
        }

        return true;
    }

    private boolean checkIfActiveStatusAllowed(
            final int workingMinutes,
            final LocalDateTime endShift
    ) throws ActivityStatusCannotBeChangedException
    {
        if (checkWorkingHoursOvertime(workingMinutes, endShift)) {
            throw new ActivityStatusCannotBeChangedException();
        }

        return true;
    }

    private boolean checkWorkingHoursOvertime(
            final int workingMinutes,
            final LocalDateTime endShift
    ) {
        LocalDateTime currentTime = LocalDateTime.now();

        return workingMinutes >= Constants.MAX_WORKING_MINUTES && currentTime.isBefore(endShift);
    }

    private boolean checkIfDrivingInProgress(final List<Driving> drivings) {
        for (Driving driving : drivings){
            if (driving.isActive()) {
                return true;
            }
        }

        return false;
    }

}
