package com.example.serbUber.service.user;

import com.example.serbUber.dto.DriverActivityResetNotificationDTO;
import com.example.serbUber.dto.user.DriverDTO;
import com.example.serbUber.dto.user.DriverPageDTO;
import com.example.serbUber.dto.user.UserDTO;
import com.example.serbUber.exception.*;
import com.example.serbUber.model.*;
import com.example.serbUber.model.user.Driver;
import com.example.serbUber.model.user.DriverUpdateApproval;
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

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static com.example.serbUber.SerbUberApplication.hopper;
import static com.example.serbUber.dto.user.DriverDTO.fromDrivers;
import static com.example.serbUber.dto.user.DriverPageDTO.fromDriversPage;
import static com.example.serbUber.exception.ErrorMessagesConstants.ACTIVE_DRIVING_IN_PROGRESS_MESSAGE;
import static com.example.serbUber.exception.ErrorMessagesConstants.UNBLOCK_UNBLOCKED_USER_MESSAGE;
import static com.example.serbUber.model.DrivingStatus.ACCEPTED;
import static com.example.serbUber.util.Constants.*;
import static com.example.serbUber.util.JwtProperties.getHashedNewUserPassword;
import static com.example.serbUber.util.PictureHandler.convertPictureToBase64ByName;

@Component
@Qualifier("driverServiceConfiguration")
public class DriverService implements IDriverService{

    private DriverRepository driverRepository;
    private final VehicleService vehicleService;
    private final RoleService roleService;
    private final VerifyService verifyService;
    private final EmailService emailService;
    private final WebSocketService webSocketService;
    private RouteService routeService;

    public DriverService(
            final DriverRepository driverRepository,
            final VehicleService vehicleService,
            final VerifyService verifyService,
            final RoleService roleService,
            final WebSocketService webSocketService,
            final EmailService emailService,
            final RouteService routeService
    ) {
        this.driverRepository = driverRepository;
        this.vehicleService = vehicleService;
        this.verifyService = verifyService;
        this.roleService = roleService;
        this.webSocketService = webSocketService;
        this.emailService = emailService;
        this.routeService = routeService;
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

    public Driver getDriverByEmail(final String email) throws EntityNotFoundException {

        return driverRepository.getDriverByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException(email, EntityType.USER));
    }

    public List<Driver> getActiveDrivers() {

        return driverRepository.getActiveDrivers();
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

    public Driver onDriverLogin(final Long id)
            throws EntityNotFoundException
    {
        Driver driver = getDriverById(id);
        setDriverLoginData(driver);
        driver.setOnline(true);

        return driverRepository.save(driver);
    }

    public boolean blockDriver(final Long id, final String reason)
            throws IOException, EntityNotFoundException, EntityUpdateException, MailCannotBeSentException {
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
        webSocketService.sendBlockedNotification(driver.getEmail());

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

    public List<DriverPageDTO> getDriversWithPagination(int pageNumber, int pageSize) {
        Pageable page = PageRequest.of(pageNumber, pageSize);
        Page<Driver> results = getDriverPage(page);
        return fromDriversPage(results.getContent(), results.getSize(), results.getTotalPages());
    }

    public DriverDTO approveDriverChanges(final DriverUpdateApproval driverUpdateApproval)
            throws EntityNotFoundException
    {
        Driver driver = getDriverByEmail(driverUpdateApproval.getUserEmail());

        driver.setName(driverUpdateApproval.getName());
        driver.setSurname(driverUpdateApproval.getSurname());
        driver.setPhoneNumber(driverUpdateApproval.getPhoneNumber());
        driver.setCity(driverUpdateApproval.getCity());
        driver.getVehicle().setPetFriendly(driverUpdateApproval.isPetFriendly());
        driver.getVehicle().setBabySeat(driverUpdateApproval.isBabySeat());
        driver.getVehicle().setVehicleTypeInfo(this.vehicleService.driverUpdateApprovalVehicle(driverUpdateApproval.getVehicleType()));
        this.driverRepository.save(driver);

        return new DriverDTO(driver);
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

    @Transactional
    public Driver getDriverForDriving(final DrivingNotification drivingNotification){
        LocalDateTime startDate = drivingNotification.getStarted();
        LocalDateTime endDate = drivingNotification.getStarted().plusMinutes((int) drivingNotification.getDuration());
        Location startLocation = drivingNotification.getRoute().getLocations().first().getLocation();
        List<Driver> drivers = driverRepository.getActiveDriversWhichVehicleMatchParams(drivingNotification.getVehicleTypeInfo().getVehicleType());

        Driver driver = drivers.size() > 0 ?
                findMatchesDriver(
                        drivers,
                        startLocation.getLon(),
                        startLocation.getLat(),
                        drivingNotification.isBabySeat(),
                        drivingNotification.isPetFriendly(),
                        startDate,
                        endDate,
                        drivingNotification.getDuration()
                )
                : null;

        if (driver != null) {
            driver.setLocked(!driver.isLocked());
            driver = driverRepository.save(driver);
        }

        return driver;
    }

    @Transactional
    public boolean isTimeToGoToDeparture(Driver driver, Driving nextDriving){
        int minutesToStartDriving = (int) ChronoUnit.MINUTES.between(LocalDateTime.now(), nextDriving.getStarted());
        int minutesFromCurrentLocationToStartDriving = (int) calculateMinutesToStartDriving(driver, nextDriving);

        return minutesToStartDriving - minutesFromCurrentLocationToStartDriving <= 2;
    }

    private Driver findMatchesDriver(
            final List<Driver> drivers,
            final double lonStart,
            final double latStart,
            final boolean babySeat,
            final boolean petFriendly,
            final LocalDateTime start,
            final LocalDateTime end,
            final double duration
    ){
        List<Driver> matchedDrivers = new LinkedList<>();
        drivers.stream()
                .filter(driver ->
                        isEligibleDriver(driver, babySeat, petFriendly)
                                && !driverHasActiveAndFutureRide(driver)
                                && checkIsFreeDuringRequestedTime(driver, start, end, latStart, lonStart, duration)
                )
                .forEach(matchedDrivers::add);

        //u matchesDrivers se nalaze vozaci koji su trenutno slobodni i koji ili nemaju buducu voznju ili imaju buducu voznju ali se one ne preklapaju sa nasom trenutnom voznjom
        return matchedDrivers.size() == 0 ?
                findBusyDriverWithoutFutureDrivings(drivers, start, latStart, lonStart, duration):
                findNearestDriver(matchedDrivers, lonStart, latStart);
    }

    public Driving getActiveDriving(List<Driving> drivings){
        for (Driving driving: drivings){
            if (driving.getDrivingStatus().equals(DrivingStatus.ACCEPTED) && driving.isActive()){
                return driving;
            }
        }

        return null;
    }

    public void save(Driver driver) {
        driverRepository.save(driver);
    }

    public double calculateMinutesToStartDriving(Driver driver, Driving driving){
        Location drivingLocation = driving.getRoute().getLocations().first().getLocation();
        if(!driver.isDrive() && !hasFutureDrivings(driver)){
            return calculateMinutesToArrivalForFreeDriversWithoutFutureDrivings(driver, drivingLocation.getLat(), drivingLocation.getLon());
        }
        else if(!driver.isDrive() && hasFutureDrivings(driver)){
            return calculateMinutesToArrivalForFreeDriversWithFutureDrivings(driver, drivingLocation.getLat(), drivingLocation.getLon(), getFutureDrivings(driver), driving.getStarted());
        }
        else{
            return calculateMinutesToArrivalForBusyDriversWithoutFutureDrivings(driver, drivingLocation.getLat(), drivingLocation.getLon());
        }
    }

    private Driver getSoonFreeDriver(List<Driver> busyDriversWithoutFutureDrivings) {

        return busyDriversWithoutFutureDrivings.stream()
                .filter(driver -> getActiveDriving(driver.getDrivings()) != null)
                .min(Comparator.comparingDouble(driver -> getMinToEndOfRide(getActiveDriving(driver.getDrivings()))))
                .orElse(busyDriversWithoutFutureDrivings.get(0));
    }

    private Driver findBusyDriverWithoutFutureDrivings(
            final List<Driver> drivers,
            final LocalDateTime start,
            final double startLat,
            final double startLng,
            final double duration
    ) {
        List<Driver> busyDriversWithoutFutureDrivings = new LinkedList<>();
        drivers.stream()
                .filter(driver -> !hasFutureDriving(driver))
                .forEach(driver -> putDriverInBusyDriverIfNotFinishShift(start, startLat, startLng, duration, busyDriversWithoutFutureDrivings, driver));

        return getSoonestFreeDriver(busyDriversWithoutFutureDrivings);
    }

    private boolean hasFutureDriving(Driver driver) {
        if (driver.isDrive()) {
            return driver.getDrivings().stream()
                    .anyMatch(driving -> driving.getDrivingStatus().equals(DrivingStatus.ACCEPTED) && !driving.isActive());
        }

        return false;
    }


    private Driver getSoonestFreeDriver(final List<Driver> busyDriversWithoutFutureDrivings) {

        return busyDriversWithoutFutureDrivings.size()!=0 ?
                getSoonFreeDriver(busyDriversWithoutFutureDrivings):
                null;
    }

    private void putDriverInBusyDriverIfNotFinishShift(
            final LocalDateTime start,
            final double startLat,
            final double startLng,
            final double duration,
            final List<Driver> busyDriversWithoutFutureDrivings,
            final Driver driver
    ) {
        double minutesToArrival = calculateMinutesToArrivalForBusyDriversWithoutFutureDrivings(driver, startLat, startLng);
        if(minutesToArrival >= 0){
            LocalDateTime endOfDriving = start.plusMinutes((long) (duration + minutesToArrival));
            if (isNotSoonEndShiftForDriver(endOfDriving, driver.getWorkingMinutes())) {
                busyDriversWithoutFutureDrivings.add(driver);
            }
        }
    }

    private double getMinToEndOfRide(Driving activeDriving) {

        return ChronoUnit.MINUTES.between(LocalDateTime.now(), activeDriving.getStarted().plusMinutes((int) activeDriving.getDuration()));
    }

    private boolean driverHasActiveAndFutureRide(Driver driver) {
        boolean drive = driver.isDrive();

        return drive && hasFutureDrivings(driver);
    }

    private boolean hasFutureDrivings(Driver driver){
        boolean driverHasFutureDrivings = false;
        for (Driving driving: driver.getDrivings()){
            if (driving.getDrivingStatus().equals(DrivingStatus.ACCEPTED) && !driving.isActive() && driving.getStarted().isAfter(LocalDateTime.now())){
                driverHasFutureDrivings = true;
                break;
            }
        }
        return driverHasFutureDrivings;
    }

    private List<Driving> getFutureDrivings(final Driver driver){
        List<Driving> futureDrivings = new LinkedList<>();
        for (Driving driving: driver.getDrivings()) {
            if (driving.getDrivingStatus().equals(DrivingStatus.ACCEPTED) && !driving.isActive() && driving.getStarted().isAfter(LocalDateTime.now())) {
                futureDrivings.add(driving);
            }
        }

        return futureDrivings;
    }

    private boolean isEligibleDriver(Driver driver, boolean babySeat, boolean petFriendly) {
        if (babySeat && petFriendly && driver.getVehicle().isBabySeat() && driver.getVehicle().isPetFriendly()){

            return true;
        }
        else if (babySeat && !petFriendly && driver.getVehicle().isBabySeat()){
            return true;
        }
        else if (!babySeat && petFriendly && driver.getVehicle().isPetFriendly()){

            return true;
        }
        else return !babySeat && !petFriendly;
    }

    //ako se preklapaju datumi, onda se taj vozac ne moze izabrati
    private boolean checkIsFreeDuringRequestedTime(
            final Driver driver,
            final LocalDateTime start,
            final LocalDateTime end,
            final double startLat,
            final double startLng,
            final double duration
    ) {

        return !driver.isDrive() && isFreeDuringRequestedTime(driver, start, end, startLat, startLng, duration);
    }

    private boolean isFreeDuringRequestedTime(
            final Driver driver,
            final LocalDateTime start,
            final LocalDateTime end,
            final double startLat,
            final double startLng,
            final double duration
    ) {
        List<Driving> futureDrivings = new LinkedList<>();
        for (Driving driving : driver.getDrivings()) {
            if (DrivingStatus.ACCEPTED.equals(driving.getDrivingStatus()) && !driving.isActive() && overlap(start, end, driving)) {

                return false;
            }
            futureDrivings.add(driving);
        }

        return checkIfDriverWithFutureDrivingNotSoonEndShift(futureDrivings, startLat, startLng, duration, start, driver);

    }

    private boolean checkIfDriverWithFutureDrivingNotSoonEndShift(
            final List<Driving> futureDrivings,
            final double startLat,
            final double startLng,
            final double duration,
            final LocalDateTime start,
            final Driver driver
    ){
        if(futureDrivings.size() > 0){
            double minutesToArrival = calculateMinutesToArrivalForFreeDriversWithFutureDrivings(driver, startLat, startLng, futureDrivings, start);
            LocalDateTime endOfDriving = start.plusMinutes((long) (duration + minutesToArrival));

            return isNotSoonEndShiftForDriver(endOfDriving, driver.getWorkingMinutes());
        }

        double minutesToArrival = calculateMinutesToArrivalForFreeDriversWithoutFutureDrivings(driver, startLat, startLng);
        LocalDateTime endOfDriving = start.plusMinutes((long) (duration + minutesToArrival));

        return isNotSoonEndShiftForDriver(endOfDriving, driver.getWorkingMinutes());
    }

    private boolean overlap(LocalDateTime start, LocalDateTime end, Driving driving) {
        LocalDateTime drivingStart = driving.getStarted();
        LocalDateTime drivingEnd = drivingStart.plusMinutes((int) driving.getDuration());

        return !(start.isAfter(drivingEnd) || end.isBefore(drivingStart));
    }

    private double calculateMinutesToArrivalForFreeDriversWithoutFutureDrivings(
            final Driver driver,
            final double startLat,
            final double startLng
    ){
        Location driverLocation = driver.getVehicle().getCurrentStop();

        return routeService.calculateMinutesForDistance(driverLocation.getLat(), driverLocation.getLon(), startLat, startLng);
    }

    private double calculateMinutesToArrivalForFreeDriversWithFutureDrivings(
            final Driver driver,
            final double startLat,
            final double startLng,
            final List<Driving> futureDrivings,
            final LocalDateTime start
    ){
        Driving lastDrivingBeforeOurDriving = getLastDrivingBeforeOurDriving(futureDrivings, start);
        if (lastDrivingBeforeOurDriving != null){
            Location drivingLocation = lastDrivingBeforeOurDriving.getRoute().getLocations().last().getLocation();

            return routeService.calculateMinutesForDistance(
                    drivingLocation.getLat(), drivingLocation.getLon(), startLat, startLng
            );
        }

        return addMinutesIfDriverHasNotDrivingBeforeOurDriving(driver, startLat, startLng);
    }

    private double calculateMinutesToArrivalForBusyDriversWithoutFutureDrivings(
            final Driver driver,
            final double startLat,
            final double startLng
    ){
        Location driverLocation = driver.getVehicle().getCurrentStop();
        Driving activeDriving = getActiveDriving(driver.getDrivings());
        double minutes = -1;
        if(activeDriving != null) {
            Location lastLocationOfActiveDriving = activeDriving.getRoute().getLocations().last().getLocation();
            double minutesToFinishDriving = routeService.calculateMinutesForDistance(
                    driverLocation.getLat(), driverLocation.getLon(), lastLocationOfActiveDriving.getLat(), lastLocationOfActiveDriving.getLon());
            double minutesToArrival = routeService.calculateMinutesForDistance(
                    lastLocationOfActiveDriving.getLat(), lastLocationOfActiveDriving.getLon(), startLat, startLng);
            minutes = minutesToArrival + minutesToFinishDriving;
        }

        return minutes;
    }

    private double addMinutesIfDriverHasNotDrivingBeforeOurDriving(
            final Driver driver,
            final double startLat,
            final double startLng
    ) {

        return routeService.calculateMinutesForDistance(driver.getVehicle().getCurrentStop().getLat(), driver.getVehicle().getCurrentStop().getLon(), startLat, startLng);
    }

    private Driving getLastDrivingBeforeOurDriving(List<Driving> futureDrivings, LocalDateTime start) {

        Optional<Driving> lastDrivingBeforeOurDriving = futureDrivings.stream()
                .filter(driving -> driving.getStarted().plusMinutes((int) driving.getDuration()).isBefore(start))
                .max(Comparator.comparing(driving -> driving.getStarted().plusMinutes((int) driving.getDuration())));

        return lastDrivingBeforeOurDriving.orElse(null);
    }

    private Driver findNearestDriver(List<Driver> activeAndFreeDrivers, double lonStart, double latStart) {

        return activeAndFreeDrivers.stream()
                .min(Comparator.comparingDouble(d -> getDistance(
                        lonStart, latStart, d.getVehicle().getCurrentStop().getLon(), d.getVehicle().getCurrentStop().getLat())))
                .orElseThrow(NoSuchElementException::new);
    }

    private double getDistance(double lonStart, double latStart, double lonEnd, double latEnd){
        GHRequest request = new GHRequest(latStart, lonStart, latEnd, lonEnd);
        request.setProfile("car");
        GHResponse route = hopper.route(request);

        return route.getBest().getDistance();
    }

    ///////////
    //1. slobodnog driver-a bez buducih voznji - started+duration+minDolaska
    //2. slobodan driver sa buducim voznjama - started+duration+minDolaskaOdPoslednjeVoznjeKojaJePreNase
    //3. driver trenutno zauzet - started+duration+vremeDoKrajaVoznje+minDolaska
    private boolean isNotSoonEndShiftForDriver(LocalDateTime endOfDriving, int currentWorkingMinutes){
        long minutes = ChronoUnit.MINUTES.between(LocalDateTime.now(), endOfDriving);

        return currentWorkingMinutes + minutes < MAX_WORKING_MINUTES;
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
            if (driving.isActive() || (driving.getDrivingStatus() == ACCEPTED && driving.getStarted().isAfter(LocalDateTime.now()))) {

                return true;
            }
        }

        return false;
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
    ) throws EntityAlreadyExistsException, EntityNotFoundException {
        try {
            String hashedPassword = getHashedNewUserPassword(password);
            Driver driver = driverRepository.save(
                    new Driver(email, hashedPassword, name, surname, phoneNumber, city, getProfilePicture(profilePicture), vehicle, roleService.get(ROLE_DRIVER))
            );
            verifyService.create(driver.getId(), driver.getEmail());

            return driver;
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(ROLE_DRIVER, EntityType.ROLE);
        } catch (Exception e) {
            throw new EntityAlreadyExistsException(String.format("User with %s already exists.", email));
        }
    }
}
