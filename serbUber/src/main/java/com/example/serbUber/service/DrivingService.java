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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static com.example.serbUber.dto.DrivingDTO.fromDrivings;
import static com.example.serbUber.dto.DrivingPageDTO.fromDrivingsPage;
import static com.example.serbUber.model.ChartType.DISTANCE;
import static com.example.serbUber.model.ChartType.SPENDING;
import static com.example.serbUber.model.DrivingStatus.FINISHED;
import static com.example.serbUber.util.Constants.*;

@Component
@Qualifier("drivingServiceConfiguration")
public class DrivingService implements IDrivingService {

    private final DrivingRepository drivingRepository;
    private final UserService userService;
    private final WebSocketService webSocketService;
    private final DrivingStatusNotificationService drivingStatusNotificationService;
    private final VehicleService vehicleService;
    private final RouteService routeService;
    private final DrivingWithVehicleService drivingWithVehicleService;

    public DrivingService(
            final DrivingRepository drivingRepository,
            final UserService userService,
            final WebSocketService webSocketService,
            final DrivingStatusNotificationService drivingStatusNotificationService,
            final VehicleService vehicleService,
            final RouteService routeService,
            final DrivingWithVehicleService drivingWithVehicleService
    ) {
        this.drivingRepository = drivingRepository;
        this.userService = userService;
        this.webSocketService = webSocketService;
        this.drivingStatusNotificationService = drivingStatusNotificationService;
        this.vehicleService = vehicleService;
        this.routeService = routeService;
        this.drivingWithVehicleService = drivingWithVehicleService;
    }

    public Driving create(
            final int duration,
            final LocalDateTime started,
            final LocalDateTime payingLimit,
            final Route route,
            final DrivingStatus drivingStatus,
            final Long driverId,
            final Set<RegularUser> users,
            final HashMap<Long, Boolean> usersPaid,
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

    public DrivingDTO createDTO(final int duration,
                                final LocalDateTime started,
                                final LocalDateTime payingLimit,
                                final Route route,
                                final DrivingStatus drivingStatus,
                                final Long driverId,
                                final Set<RegularUser> users,
                                final HashMap<Long, Boolean> usersPaid,
                                final double price) throws EntityNotFoundException {
        return new DrivingDTO(create(duration, started, payingLimit, route, drivingStatus, driverId, users, usersPaid, price));
    }

    public DrivingDTO save(Driving driving){
        return new DrivingDTO(drivingRepository.save(driving));
    }

    public List<DrivingDTO> getAll() {
        List<Driving> drivings = drivingRepository.findAll();

        return fromDrivings(drivings);
    }

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

        if(parameter.equals("Price") || parameter.equals("Date")) {
            Pageable page = PageRequest.of(pageNumber, pageSize, Sort.by(getSortOrder(sortOrder), getSortBy(parameter)));
            Page<Driving> results = getDrivingPage(id, page);
            return fromDrivingsPage(results.getContent(), results.getSize(), results.getTotalPages());
        }
        else{
            List<Driving> drivings = drivingRepository.getDrivingsForUserId(id);
            if (parameter.equals("Departure")) {
                if(sortOrder.equals("Ascending")){
                    Collections.sort(drivings, compareByDeparture);
                }
                else{
                    Collections.sort(drivings, compareByDeparture.reversed());
                }
            } else {
                if(sortOrder.equals("Ascending")) {
                    Collections.sort(drivings, compareByDestination);
                }
                else{
                    Collections.sort(drivings, compareByDestination.reversed());
                }
            }
            if(pageNumber > 0){
                pageNumber = pageSize * pageNumber;
                pageSize = pageNumber + pageNumber;
                if(pageSize > drivings.size()){
                    pageSize = drivings.size();
                }
            }
            //0,1,2
            //3
            //6,7,8 -> 6-9
            return fromDrivingsPage(drivings.subList(pageNumber, pageSize), pageSize, drivings.size());
        }
    }

    public List<Driving> getAllReservations(){

        return drivingRepository.getAllReservations();
    }

    private Page<Driving> getDrivingPage(final Long id, final Pageable page) throws EntityNotFoundException {
        User user = userService.getUserById(id);

        return user.getRole().isDriver() ?
                drivingRepository.findByDriverId(id, page) :
                drivingRepository.findByUserId(id, page);
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

    public DrivingDTO getDrivingDto(final Long id) throws EntityNotFoundException {

        return new DrivingDTO(getDriving(id));
    }

    public Driving getDriving(final Long id) throws EntityNotFoundException {

        return drivingRepository.getDrivingById(id)
                .orElseThrow(() -> new EntityNotFoundException(id, EntityType.DRIVING));
    }

    public List<DrivingDTO> getAllNowAndFutureDrivings(final Long id) {

        return fromDrivings(drivingRepository.getAllNowAndFutureDrivings(id));
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
       List<Driving> optionalDriving = drivingRepository.getActiveDrivingForUser(id, LocalDateTime.now().plusMinutes(30));

        return optionalDriving.size() > 0 ? new SimpleDrivingInfoDTO(optionalDriving.get(0)): null;
    }

    public int getNumberOfAllDrivingsForUser(final Long id) throws EntityNotFoundException {
        User user = userService.getUserById(id);
        return user.getRole().isDriver() ?
                drivingRepository.getNumberOfAllDrivingsForDriver(id).size() :
                drivingRepository.getNumberOfAllDrivingsForRegularUser(id).size();
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
//        this.vehicleService.updateCurrentVehiclesLocation();

        return new DrivingDTO(driving);
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

    public boolean isPassengersAlreadyHaveRide(final List<String> passengersEmail, final LocalDateTime started) throws EntityNotFoundException {

        boolean busyPassengers = false;
        for(String passengerEmail : passengersEmail){
            User user = userService.getUserByEmail(passengerEmail);
            SimpleDrivingInfoDTO activeDriving = checkUserHasActiveDriving(user.getId());
            if(activeDriving == null){
                busyPassengers = false;
                break;
            }
            if(ChronoUnit.MINUTES.between(activeDriving.getStarted(), started) > 30){
                busyPassengers = false;
                break;
            }
            else{
                busyPassengers = true;
                break;
            }
        }
        return busyPassengers;
    }

    public DrivingDTO startDriving(final Long id) throws EntityNotFoundException, DriverAlreadyHasStartedDrivingException, DrivingShouldNotStartYetException {
        Driving driving = getDriving(id);
        if (driverHasActiveDriving(driving.getDriver().getId())) {
            throw new DriverAlreadyHasStartedDrivingException();
        }

        if (drivingShouldNotStartYet(driving)) {
            throw new DrivingShouldNotStartYetException();
        }

        driving.setStarted(LocalDateTime.now());
        driving.setActive(true);
        driving.getDriver().getVehicle().setActiveRoute(driving.getRoute());
        driving.getDriver().getVehicle().setCurrentLocationIndex(0);
        driving.getDriver().getVehicle().setCrossedWaypoints(0);
        driving.getDriver().getVehicle().setCurrentStop(driving.getRoute().getLocations().first().getLocation());
        driving.getDriver().setDrive(true);
        driving.setDrivingStatus(DrivingStatus.ACCEPTED);
        drivingRepository.save(driving);

        webSocketService.startDrivingNotification(new SimpleDrivingInfoDTO(driving), driving.getUsers());
        webSocketService.sendVehicleCurrentLocation(new VehicleCurrentLocationDTO(
            new VehicleWithDriverId(
                driving.getDriver().getVehicle(),
                driving.getDriver().getId(),
                driving.getDriver().isActive())
            )
        );

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

        Driving nextDriving = driverHasFutureDriving(driving.getDriver().getId());
        if (nextDriving != null) {
            createDrivingToDeparture(driving.getDriver(), driving.getDriver().getVehicle().getCurrentStop(), nextDriving.getRoute());
        } else {
            webSocketService.sendVehicleCurrentLocation(
                new VehicleCurrentLocationDTO(new VehicleWithDriverId(driving.getDriver().getVehicle(), driving.getDriver().getVehicle().getId(), driving.getDriver().isActive()))
            );
        }

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
                ? this.calculateChartData(this.drivingRepository.getDrivingsForDriver(id), chartType, startDate, endDate)
                : this.calculateChartData(this.drivingRepository.getDrivingsForRegular(id), chartType, startDate, endDate);
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

    public ChartDataDTO getAdminChartData(
            final Long id,
            final ChartType chartType,
            LocalDate startDate,
            LocalDate endDate
    ) throws EntityNotFoundException {
        User user = null;
        if (id != NOT_BY_SPECIFIC_USER) {
            user = this.userService.getUserById(id);
        }

        return (id != NOT_BY_SPECIFIC_USER) ? calculateChartData(user.getRole().isRegularUser() ? this.drivingRepository.getDrivingsForRegular(id)
                : this.drivingRepository.getDrivingsForDriver(id), chartType, startDate, endDate)
                : calculateForAllUsers(chartType, startDate, endDate);
    }

    boolean checkSenderAndReceiverInActiveDriving(Long driverId, Long userId) {

        return driverHasActiveDriving(driverId) && this.checkUserHasActiveDriving(userId) != null;
    }

    private ChartDataDTO calculateForAllUsers(
            final ChartType chartType,
            final LocalDate startDate,
            final LocalDate endDate
    ) {

        return calculateChartData(this.drivingRepository.getAllDrivings(), chartType, startDate, endDate);
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
                return ONE_DRIVING; //kao jedan driving za broj drivinga
            }
        }
    }

    private double getChartAverage(final int numOfDrivigns, final double totalSum) {
        DecimalFormat df = new DecimalFormat("#.##");

        return  Double.parseDouble(df.format(numOfDrivigns > 0 ? totalSum / numOfDrivigns : 0));
    }

    private boolean checkIfDateInRange(
            final LocalDate date,
            final LocalDateTime started
    ) {

        return date.equals(started.toLocalDate());
    }

    public Driving driverHasFutureDriving(final Long id) {

        List<Driving> drivings = drivingRepository.driverHasFutureDriving(id);
        return drivings.size() > 0 ?
                drivings.get(0) : null;
    }

    public DrivingDTO createDrivingToDeparture(Driver driver, Location currentStop, Route nextRoute) {

        List<DrivingLocationIndexRequest> drivingLocationIndexRequestList = new LinkedList<>();
        DrivingLocationIndexRequest firstLocation = new DrivingLocationIndexRequest(
                new LocationRequest(currentStop.getLon(), currentStop.getLat()), 1);

        Location nextLocation = nextRoute.getLocations().first().getLocation();
        DrivingLocationIndexRequest secondLocation = new DrivingLocationIndexRequest(
                new LocationRequest(nextLocation.getCity(), nextLocation.getStreet(), nextLocation.getNumber(), nextLocation.getZipCode(), nextLocation.getLon(), nextLocation.getLat()), 2
        );

        drivingLocationIndexRequestList.add(firstLocation);
        drivingLocationIndexRequestList.add(secondLocation);

        double minutes = this.routeService.calculateMinutesForDistance(
            firstLocation.getLocation().getLat(),
            firstLocation.getLocation().getLon(),
            secondLocation.getLocation().getLat(),
            secondLocation.getLocation().getLon()
        );
        Route route = this.routeService.createRoute(drivingLocationIndexRequestList, minutes, routeService.getDistanceInKmFromTime(minutes), List.of(0));
        Driving driving = new Driving((int) minutes, LocalDateTime.now(), LocalDateTime.now().plusMinutes((long) minutes), route, DrivingStatus.ON_WAY_TO_DEPARTURE, driver, 0);

        driver.getVehicle().setActiveRoute(driving.getRoute());
        driver.getVehicle().setCurrentLocationIndex(0);
        driver.getVehicle().setCrossedWaypoints(0);
        driver.getVehicle().setCurrentStop(driving.getRoute().getLocations().first().getLocation());
        driver.setDrive(true);

        return new DrivingDTO(drivingRepository.save(driving));
    }

    private boolean drivingShouldNotStartYet(Driving driving) {

        return ChronoUnit.MINUTES.between(LocalDateTime.now(), driving.getStarted()) > MAX_MINUTES_BEFORE_DRIVING_CAN_START;
    }

    private boolean driverHasActiveDriving(Long driverId) {

        return drivingRepository.getActiveDrivingForDriver(driverId).isPresent();
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
