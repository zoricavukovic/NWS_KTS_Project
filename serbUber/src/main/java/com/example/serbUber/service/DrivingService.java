package com.example.serbUber.service;

import com.example.serbUber.dto.*;
import com.example.serbUber.dto.chart.ChartDataDTO;
import com.example.serbUber.dto.chart.ChartItemDTO;
import com.example.serbUber.dto.chart.ChartSumDataDTO;
import com.example.serbUber.exception.*;
import com.example.serbUber.model.*;
import com.example.serbUber.model.user.Driver;
import com.example.serbUber.model.user.RegularUser;
import com.example.serbUber.model.user.User;
import com.example.serbUber.repository.DrivingRepository;
import com.example.serbUber.request.DrivingLocationIndexRequest;
import com.example.serbUber.request.LocationRequest;
import com.example.serbUber.service.interfaces.IDrivingService;
import com.example.serbUber.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static com.example.serbUber.dto.DrivingDTO.fromDrivings;
import static com.example.serbUber.dto.DrivingPageDTO.fromDrivingsPage;
import static com.example.serbUber.model.DrivingStatus.FINISHED;
import static com.example.serbUber.util.Constants.*;

@Component
@Qualifier("drivingServiceConfiguration")
public class DrivingService implements IDrivingService {

    private DrivingRepository drivingRepository;
    private UserService userService;
    private WebSocketService webSocketService;
    private DrivingStatusNotificationService drivingStatusNotificationService;
    private RouteService routeService;

    @Autowired
    public DrivingService(
        final DrivingRepository drivingRepository,
        final UserService userService,
        final WebSocketService webSocketService,
        final DrivingStatusNotificationService drivingStatusNotificationService,
        final RouteService routeService
    ) {
        this.drivingRepository = drivingRepository;
        this.userService = userService;
        this.webSocketService = webSocketService;
        this.drivingStatusNotificationService = drivingStatusNotificationService;
        this.routeService = routeService;
    }

    public Driving create(
        final double duration,
        final LocalDateTime started,
        final Route route,
        final DrivingStatus drivingStatus,
        final Long driverId,
        final Set<RegularUser> users,
        final double price
    ) throws EntityNotFoundException {
        Driver driver = userService.getDriverById(driverId);

        Driving driving = drivingRepository.save(new Driving(duration, started, null, route, drivingStatus, driver, price));
        users.forEach(user -> {
            List<Driving> drivings = getAllDrivingsForUserEmail(user.getEmail());
            drivings.add(driving);
            user.setDrivings(drivings);
            userService.saveUser(user);
        });

        return driving;
    }

    public DrivingDTO createDTO(
        final double duration,
        final LocalDateTime started,
        final Route route,
        final DrivingStatus drivingStatus,
        final Long driverId,
        final Set<RegularUser> users,
        final double price
    ) throws EntityNotFoundException {

        return new DrivingDTO(create(duration, started, route, drivingStatus, driverId, users, price));
    }

    public Driving save(Driving driving){

        return drivingRepository.save(driving);
    }

    public List<DrivingDTO> getAll() {
        List<Driving> drivings = drivingRepository.findAll();

        return fromDrivings(drivings);
    }

    @Transactional
    public List<Driving> getAllDrivingsForUserEmail(final String email) {

        return drivingRepository.getAllDrivingsForUserEmail(email);
    }

    public List<DrivingPageDTO> getDrivingsForUser(
            final Long id,
            int pageNumber,
            int pageSize,
            final String parameter,
            final String sortOrder
    ) throws EntityNotFoundException {

        return (parameter.equals("Price") || parameter.equals("Date")) ?
            sortDrivingsByPriceOrDate(id, pageNumber, pageSize, parameter, sortOrder):
            sortDrivingsByDepartureOrDestination(id, pageNumber, pageSize, parameter, sortOrder);
    }


    @Transactional
    public List<Driving> getAllReservations(){

        return drivingRepository.getAllReservations();
    }

    public List<Driving> getAcceptedNotActiveDrivings(){
        return drivingRepository.getAcceptedNotActiveDrivings();
    }


    public DrivingDTO getDrivingDto(final Long id) throws EntityNotFoundException {

        return new DrivingDTO(getDriving(id));
    }

    public Driving getDriving(final Long id) throws EntityNotFoundException {

        return drivingRepository.getDrivingById(id)
                .orElseThrow(() -> new EntityNotFoundException(id, EntityType.DRIVING));
    }

    public List<DrivingDTO> getAllNowAndFutureDrivings(final Long driverId) {

        return fromDrivings(drivingRepository.getAllNowAndFutureDrivings(driverId));
    }

    public DrivingDTO paidDriving(final Long id) throws EntityNotFoundException {
        Driving driving = getDriving(id);
        driving.setDrivingStatus(DrivingStatus.ACCEPTED);

        drivingRepository.save(driving);

        return new DrivingDTO(driving);
    }

    public DrivingDTO removeDriver(Long id) throws EntityNotFoundException {
        Driving driving = getDriving(id);
        driving.setDriver(null);
        drivingRepository.save(driving);

        return new DrivingDTO(driving);
    }

    public SimpleDrivingInfoDTO checkUserHasActiveDriving(final Long id) {
        LocalDateTime limitDateTime = LocalDateTime.now().plusMinutes(HALF_AN_HOUR);
        List<Driving> optionalDriving = drivingRepository.getActiveDrivingForUser(id, limitDateTime);

        return optionalDriving.size() > 0 ? new SimpleDrivingInfoDTO(optionalDriving.get(0)): null;
    }

    public DrivingDTO rejectDriving(final Long id, final String reason) throws EntityNotFoundException {
        Driving driving = getDriving(id);
        driving.setDrivingStatus(DrivingStatus.REJECTED);
        driving.getDriver().getVehicle().setCurrentLocationIndex(-1);
        driving.getDriver().getVehicle().setActiveRoute(null);
        drivingRepository.save(driving);

        DrivingStatusNotification drivingStatusNotification = drivingStatusNotificationService.create(
                reason, DrivingStatus.REJECTED, driving);

        webSocketService.sendRejectDriving(
                driving.getDriver().getEmail(), reason,
                drivingStatusNotification.getDriving().getUsers()
        );

        return new DrivingDTO(driving);
    }

    public void rejectOutdatedDrivings(){
        List<Driving> drivings = getAcceptedNotActiveDrivings();
        for(Driving driving : drivings){
            if(driving.getStarted().plusMinutes(FIVE_MINUTES).isBefore(LocalDateTime.now())){
                driving.setDrivingStatus(DrivingStatus.REJECTED);
                drivingRepository.save(driving);
                webSocketService.sendRejectedOutdatedDriving(driving.getUsers(), driving.getDriver().getEmail(), driving.getId());
            }
        }
    }

    public VehicleCurrentLocationDTO getVehicleCurrentLocation(final Long id) throws EntityNotFoundException {
        Driving driving = getDriving(id);
        Driver driver = driving.getDriver();
        Vehicle vehicle = driver.getVehicle();

        VehicleWithDriverId withDriverIds = new VehicleWithDriverId(vehicle, driving.getDriver().getId(), driving.getDriver().isActive());

        VehicleCurrentLocationDTO vehicleCurrentLocationDTO = new VehicleCurrentLocationDTO(withDriverIds);
        webSocketService.sendVehicleCurrentLocation(vehicleCurrentLocationDTO, driver.getEmail(), driving.getUsers());

        return vehicleCurrentLocationDTO;
    }

    public Long getDrivingByFavouriteRoute(final Long routeId) throws EntityNotFoundException {

        return drivingRepository.findDrivingByFavouriteRoute(routeId)
                .orElseThrow(() -> new EntityNotFoundException(routeId, EntityType.DRIVING));
    }

    public boolean checkIfPassengersAreBusy(final List<String> passengersEmail, final LocalDateTime started) throws EntityNotFoundException {
        for (String passengerEmail : passengersEmail) {
            if (isUserBusy(passengerEmail, started)) {
                return true;
            }
        }

        return false;
    }

    public DrivingDTO startDriving(final Long id) throws EntityNotFoundException, DriverAlreadyHasStartedDrivingException, DrivingShouldNotStartYetException {
        Driving driving = getDriving(id);
        if (driverHasActiveDriving(driving.getDriver().getId())) {
            throw new DriverAlreadyHasStartedDrivingException();
        }

        if (drivingShouldNotStartYet(driving)) {
            throw new DrivingShouldNotStartYetException();
        }

        driving = updateFieldsOfDrivingWhenStarting(driving);
        sendNotificationsAboutStartDriving(driving);

        return new DrivingDTO(driving);
    }

    public DrivingDTO finishDriving(final Long id) throws EntityNotFoundException {
        Driving driving = getDriving(id);
        driving.setActive(false);
        driving.setDrivingStatus(FINISHED);
        driving.setEnd(LocalDateTime.now());
        driving.getDriver().getVehicle().setCurrentLocationIndex(-1);
        driving.getDriver().getVehicle().setActiveRoute(null);
        driving.getDriver().getVehicle().setCrossedWaypoints(0);
        driving.getDriver().setDrive(false);
        drivingRepository.save(driving);

        webSocketService.finishDrivingNotification(new SimpleDrivingInfoDTO(driving), driving.getUsers());

        return new DrivingDTO(driving);
    }

    public ChartDataDTO getChartData(
            final Long id,
            final ChartType chartType,
            final LocalDate startDate,
            final LocalDate endDate
    ) throws EntityNotFoundException {
        User user = this.userService.getUserById(id);

        return user.getRole().isDriver()
                ? this.calculateChartData(this.drivingRepository.getFinishedDrivingsForDriver(id), chartType, startDate, endDate)
                : this.calculateChartData(this.drivingRepository.getFinishedDrivingsForRegular(id), chartType, startDate, endDate);
    }

    public ChartDataDTO getAdminChartData(
            final Long id,
            final ChartType chartType,
            LocalDate startDate,
            LocalDate endDate
    ) throws EntityNotFoundException {

        return id != NOT_BY_SPECIFIC_USER ?
            calculateChartData(getListOfDrivingForSpecificUser(id), chartType, startDate, endDate):
            calculateForAllUsers(chartType, startDate, endDate);
    }

    public LocalDateTime getTimeForDriving(final Long drivingId) throws EntityNotFoundException {
        Driving driving = getDriving(drivingId);

        return driving.getStarted();
    }

    public Driving driverHasFutureDriving(final Long id) {

        List<Driving> drivings = drivingRepository.driverHasFutureDriving(id);
        return drivings.size() > 0 ?
            drivings.get(0) : null;
    }

    public Driving getTimeToDepartureDriving(final Long driverId) {

        return drivingRepository.getOnWayToDepartureDriving(driverId);
    }

    public DrivingDTO createDrivingToDeparture(Driver driver, Location currentStop, Route nextRoute, Set<RegularUser> users) {
        DrivingLocationIndexRequest firstLocation = new DrivingLocationIndexRequest(
                new LocationRequest(currentStop.getLon(), currentStop.getLat()), 1);
        Location nextLocation = nextRoute.getLocations().first().getLocation();
        DrivingLocationIndexRequest secondLocation = new DrivingLocationIndexRequest(
            new LocationRequest(nextLocation.getCity(), nextLocation.getStreet(), nextLocation.getNumber(), nextLocation.getZipCode(), nextLocation.getLon(), nextLocation.getLat()), 2
        );

        Driving driving = saveDrivingOnWayToDeparture(driver, firstLocation, secondLocation);
        addDrivingToUserDrivings(users, driving);
        updateDriverInfo(driving, driver);

        return new DrivingDTO(driving);
    }

    private Driving saveDrivingOnWayToDeparture(Driver driver, DrivingLocationIndexRequest firstLocation, DrivingLocationIndexRequest secondLocation) {
        double minutes = this.routeService.calculateMinutesForDistance(
            firstLocation.getLocation().getLat(),
            firstLocation.getLocation().getLon(),
            secondLocation.getLocation().getLat(),
            secondLocation.getLocation().getLon()
        );
        List<DrivingLocationIndexRequest> drivingLocationIndexRequestList = new LinkedList<>();
        drivingLocationIndexRequestList.add(firstLocation);
        drivingLocationIndexRequestList.add(secondLocation);
        Route route = this.routeService.createRoute(drivingLocationIndexRequestList, minutes, routeService.getDistanceInKmFromTime(minutes), List.of(0));
        Driving driving = new Driving((int) minutes, LocalDateTime.now(), LocalDateTime.now().plusMinutes((long) minutes), route, DrivingStatus.ON_WAY_TO_DEPARTURE, driver, 0);
        driving.setActive(true); //ovo treba prilikom kreiranja, on ce tada krenuti kao

        return drivingRepository.save(driving);
    }

    private void addDrivingToUserDrivings(final Set<RegularUser> users, Driving createdDriving){
        users.forEach(user -> {
            List<Driving> drivings = getAllDrivingsForUserEmail(user.getEmail());
            drivings.add(createdDriving);
            user.setDrivings(drivings);
            userService.saveUser(user);
        });
    }
    private void updateDriverInfo(final Driving driving, final Driver driver){
        driver.getVehicle().setActiveRoute(driving.getRoute());
        driver.getVehicle().setCurrentLocationIndex(0);
        driver.getVehicle().setCrossedWaypoints(0);
        driver.getVehicle().setCurrentStop(driving.getRoute().getLocations().first().getLocation());
        driver.setDrive(true);
        userService.saveDriver(driver);
    }

    public boolean checkSenderAndReceiverInActiveDriving(Long driverId, Long userId) {

        return driverHasActiveDriving(driverId) && this.checkUserHasActiveDriving(userId) != null;
    }

    private Driving updateFieldsOfDrivingWhenStarting(final Driving driving) {
        driving.setStarted(LocalDateTime.now());
        driving.setActive(true);
        driving.getDriver().getVehicle().setActiveRoute(driving.getRoute());
        driving.getDriver().getVehicle().setCurrentLocationIndex(0);
        driving.getDriver().getVehicle().setCrossedWaypoints(0);
        driving.getDriver().getVehicle().setCurrentStop(driving.getRoute().getLocations().first().getLocation());
        driving.getDriver().setDrive(true);
        driving.setDrivingStatus(DrivingStatus.ACCEPTED);
        return drivingRepository.save(driving);
    }

    private void sendNotificationsAboutStartDriving(final Driving driving) {
        webSocketService.startDrivingNotification(new SimpleDrivingInfoDTO(driving), driving.getUsers());
        webSocketService.sendVehicleCurrentLocation(new VehicleCurrentLocationDTO(
            new VehicleWithDriverId(
                driving.getDriver().getVehicle(),
                driving.getDriver().getId(),
                driving.getDriver().isActive()),
            driving.getRoute().getLocations().first().getRouteIndex())
        );
    }

    private Page<Driving> getDrivingPage(final Long id, final Pageable page) throws EntityNotFoundException {
        User user = userService.getUserById(id);

        return user.getRole().isDriver() ?
            drivingRepository.findByDriverId(id, page) :
            drivingRepository.findByUserId(id, page);
    }

    private ChartDataDTO calculateChartData(
        final List<Driving> drivings,
        final ChartType chartType,
        final LocalDate startDate,
        final LocalDate endDate
    ) {
        List<LocalDate> datesBetween = getDatesBetween(startDate, endDate);
        List<ChartItemDTO> chartItemDTOS = new ArrayList<>();
        double totalSum = 0;
        int numOfAcceptedDrivings = 0;

        for (LocalDate date : datesBetween) {
            double totalPerDay = 0;
            for (Driving driving : drivings) {
                if (checkIfDateInRange(date, driving.getStarted())) {
                    totalPerDay += getTotalPerDay(driving, chartType);
                    totalSum += getTotalPerDay(driving, chartType);
                    numOfAcceptedDrivings++;
                }
            }

            chartItemDTOS.add(new ChartItemDTO(date.toString(), totalPerDay));
        }

        return new ChartDataDTO(chartItemDTOS, new ChartSumDataDTO(totalSum, getChartAverage(numOfAcceptedDrivings, totalSum)));
    }

    private String getSortBy(final String sortBy) {
        Dictionary<String, String> sortByDict = new Hashtable<>();
        sortByDict.put("Date", "started");
        sortByDict.put("Price", "price");

        return sortByDict.get(sortBy);
    }

    private Sort.Direction getSortOrder(final String sortOrder) {
        Dictionary<String, Sort.Direction> sortOrderDict = new Hashtable<>();
        sortOrderDict.put("Descending", Sort.Direction.DESC);
        sortOrderDict.put("Ascending", Sort.Direction.ASC);

        return sortOrderDict.get(sortOrder);
    }

    private ChartDataDTO calculateForAllUsers(
            final ChartType chartType,
            final LocalDate startDate,
            final LocalDate endDate
    ) {

        return calculateChartData(this.drivingRepository.getAllFinishedDrivings(), chartType, startDate, endDate);
    }

    private List<Driving> getListOfDrivingForSpecificUser(Long id) throws EntityNotFoundException {

        return this.userService.getUserById(id).getRole().isRegularUser() ?
            this.drivingRepository.getFinishedDrivingsForRegular(id):
            this.drivingRepository.getFinishedDrivingsForDriver(id);
    }

    private double getTotalPerDay(final Driving driving, final ChartType chartType) {

        switch (chartType) {
            case SPENDING -> {
                return driving.getPrice();
            }
            case DISTANCE -> {
                return driving.getRoute().getDistance();
            }
            default -> {
                return ONE_DRIVING;
            }
        }
    }

    private double getChartAverage(final int numOfDrivings, final double totalSum) {
        DecimalFormat df = new DecimalFormat("#.##");

        return  Double.parseDouble(df.format(numOfDrivings > 0 ? totalSum / numOfDrivings : 0));
    }

    private boolean checkIfDateInRange(final LocalDate date, final LocalDateTime started) {

        return date.equals(started.toLocalDate());
    }

    private boolean drivingShouldNotStartYet(Driving driving) {

        return ChronoUnit.MINUTES.between(LocalDateTime.now(), driving.getStarted()) > FIVE_MINUTES;
    }

    private boolean driverHasActiveDriving(Long driverId) {

        return drivingRepository.getActiveDrivingForDriver(driverId).isPresent();
    }

    private List<DrivingPageDTO> sortDrivingsByDepartureOrDestination(Long id, int pageNumber, int pageSize, String parameter, String sortOrder) {
        List<Driving> drivings = drivingRepository.getDrivingsForUserId(id);
        if (parameter.equals("Departure")) {
            sortDrivings(sortOrder, drivings, compareByDeparture);
        } else {
            sortDrivings(sortOrder, drivings, compareByDestination);
        }
        if(pageNumber > 0){
            pageNumber = pageSize * pageNumber;
            pageSize = pageNumber + pageNumber;
            if(pageSize > drivings.size()){
                pageSize = drivings.size();
            }
        }
        return fromDrivingsPage(drivings.subList(pageNumber, pageSize), pageSize, drivings.size());
    }

    private List<DrivingPageDTO> sortDrivingsByPriceOrDate(Long id, int pageNumber, int pageSize, String parameter, String sortOrder) throws EntityNotFoundException {
        Pageable page = PageRequest.of(pageNumber, pageSize, Sort.by(getSortOrder(sortOrder), getSortBy(parameter)));
        Page<Driving> results = getDrivingPage(id, page);

        return fromDrivingsPage(results.getContent(), results.getSize(), results.getTotalPages());
    }

    private void sortDrivings(String sortOrder, List<Driving> drivings, Comparator<Driving> compareByDeparture) {
        if(sortOrder.equals("Ascending")){
            drivings.sort(compareByDeparture);
        }
        else{
            drivings.sort(compareByDeparture.reversed());
        }
    }

    private boolean isUserBusy(String passengerEmail, LocalDateTime started) throws EntityNotFoundException {
        User user = userService.getUserByEmail(passengerEmail);
        SimpleDrivingInfoDTO activeDriving = checkUserHasActiveDriving(user.getId());

        return activeDriving != null && ChronoUnit.MINUTES.between(activeDriving.getStarted(), started) <= 30;
    }

    Comparator<Driving> compareByDeparture = new Comparator<Driving>() {
        @Override
        public int compare(Driving d1, Driving d2) {
            return d1.getRoute().getLocations().first().getLocation().getStreet().compareTo(d2.getRoute().getLocations().first().getLocation().getStreet());
        }
    };

    Comparator<Driving> compareByDestination = new Comparator<Driving>() {
        @Override
        public int compare(Driving d1, Driving d2) {
            return d1.getRoute().getLocations().last().getLocation().getStreet().compareTo(d2.getRoute().getLocations().last().getLocation().getStreet());
        }
    };

}
