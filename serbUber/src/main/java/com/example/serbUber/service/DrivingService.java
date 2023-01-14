package com.example.serbUber.service;

import com.example.serbUber.dto.*;
import com.example.serbUber.exception.DriverAlreadyHasStartedDrivingException;
import com.example.serbUber.exception.DrivingShouldNotStartYetException;
import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.exception.EntityType;
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

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static com.example.serbUber.dto.DrivingDTO.fromDrivings;
import static com.example.serbUber.dto.DrivingPageDTO.fromDrivingsPage;
import static com.example.serbUber.util.Constants.MAX_MINUTES_BEFORE_DRIVING_CAN_START;

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

    public DrivingDTO create(
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
        LocalDateTime end = started.plusMinutes(duration);
        User driver = userService.getUserById(driverId);

        Driving driving = drivingRepository.save(new Driving(duration, started, end, payingLimit, route, drivingStatus, driverId, price));
        users.forEach(user -> {
            List<Driving> drivingsOfUser = user.getDrivings();
            drivingsOfUser.add(driving);
            user.setDrivings(drivingsOfUser);
            userService.saveUser(user);
        });


        return null;
    }

    public List<DrivingDTO> getAll() {
        List<Driving> drivings = drivingRepository.findAll();

        return fromDrivings(drivings);
    }

    public List<DrivingPageDTO> getDrivingsForUser(
            final Long id,
            final int pageNumber,
            final int pageSize,
            final String parameter,
            final String sortOrder
    ) throws EntityNotFoundException {
        Pageable page = PageRequest.of(pageNumber, pageSize, Sort.by(getSortOrder(sortOrder), getSortBy(parameter)));
        Page<Driving> results = getDrivingPage(id, page);
        //int numberOfPages = calculateTotalNumberOfPages(results.getTotalPages(), results.getSize());
        return fromDrivingsPage(results.getContent(), results.getSize(), results.getTotalPages());
    }

    private Page<Driving> getDrivingPage(final Long id, final Pageable page) throws EntityNotFoundException {
        User user = userService.getUserById(id);
        Page<Driving> drivings = drivingRepository.findByUserId(id, page);
        return user.getRole().isDriver() ?
                drivingRepository.findByDriverId(id, page) :
                drivingRepository.findByUserId(id, page);
    }


    private String getSortBy(final String sortBy) {
        Dictionary<String, String> sortByDict = new Hashtable<>();
        sortByDict.put("Date", "started");
        sortByDict.put("Departure", "route.startPoint");
        sortByDict.put("Destination", "route.locations"); //ne znam ovo kako da pristupi??
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
        driving.setDriverId(0L);

        drivingRepository.save(driving);

        return new DrivingDTO(driving);
    }

    public SimpleDrivingInfoDTO checkUserHasActiveDriving(final Long id) {
        Optional<Driving> optionalDriving = drivingRepository.getActiveDrivingForUser(id);

        return optionalDriving.map(SimpleDrivingInfoDTO::new).orElse(null);
    }

    public int getNumberOfAllDrivingsForUser(final Long id) throws EntityNotFoundException {
        User user = userService.getUserById(id);
        return user.getRole().isDriver() ?
                drivingRepository.getNumberOfAllDrivingsForDriver(id).size() :
                drivingRepository.getNumberOfAllDrivingsForRegularUser(id).size();
    }

    public DrivingDTO rejectDriving(final Long id, final String reason) throws EntityNotFoundException {
        Driving driving = getDriving(id);
        Driver driver = userService.getDriverById(driving.getDriverId());
        driving.setDrivingStatus(DrivingStatus.REJECTED);
        driver.getVehicle().setCurrentLocationIndex(-1);
        driver.getVehicle().setActiveRoute(null);
        drivingRepository.save(driving);

        DrivingStatusNotification drivingStatusNotification = drivingStatusNotificationService.create(
            reason, DrivingStatus.REJECTED, driving);

        DrivingStatusNotificationDTO drivingStatusNotificationDTO = new DrivingStatusNotificationDTO(
            drivingStatusNotification.getDriving().getId(),
            drivingStatusNotification.getDriving().getRoute().getTimeInMin(),
            DrivingStatus.REJECTED,
            reason,
            driving.getId()
        );

        webSocketService.sendRejectDriving(
            drivingStatusNotificationDTO,
            drivingStatusNotification.getDriving().getUsers()
        );
        this.vehicleService.updateCurrentVehiclesLocation();

        return new DrivingDTO(driving);
    }

    public VehicleCurrentLocationDTO getVehicleCurrentLocation(final Long id) throws EntityNotFoundException {
        Driving driving = getDriving(id);
        Driver driver = userService.getDriverById(driving.getDriverId());
        Vehicle vehicle = driver.getVehicle();
        List<double[]> listOfVehiclesRoutes;
        if (vehicle.hasRoute()){
            listOfVehiclesRoutes = routeService.getRoutePath(vehicle.getActiveRoute().getId());
        }
        else {
            listOfVehiclesRoutes = List.of(new double[]{vehicle.getCurrentStop().getLon(), vehicle.getCurrentStop().getLat()});
        }

        VehicleWithDriverId withDriverIds = new VehicleWithDriverId(vehicle, driving.getDriverId());

        VehicleCurrentLocationDTO vehicleCurrentLocationDTO =  new VehicleCurrentLocationDTO(withDriverIds, listOfVehiclesRoutes);
        webSocketService.sendVehicleCurrentLocation(vehicleCurrentLocationDTO, driver.getEmail(), driving.getUsers());

        return vehicleCurrentLocationDTO;
    }

    public Long getDrivingByFavouriteRoute(final Long routeId) throws EntityNotFoundException {

        return drivingRepository.findDrivingByFavouriteRoute(routeId)
                .orElseThrow(() -> new EntityNotFoundException(routeId, EntityType.DRIVING));
    }

    public DrivingDTO startDriving(final Long id) throws EntityNotFoundException, DriverAlreadyHasStartedDrivingException, DrivingShouldNotStartYetException {
        Driving driving = getDriving(id);
        if (driverHasActiveDriving(driving.getDriverId())){
            throw new DriverAlreadyHasStartedDrivingException();
        }

        if (drivingShouldNotStartYet(driving)){
            throw new DrivingShouldNotStartYetException();
        }
        Driver driver = userService.getDriverById(driving.getDriverId());
        driver.getVehicle().setActiveRoute(driving.getRoute());
        driver.getVehicle().setCurrentLocationIndex(0);
        driver.getVehicle().setCurrentStop(driving.getRoute().getLocations().first().getLocation());
        driving.setStarted(LocalDateTime.now());
        driving.setActive(true);
        driving.setDrivingStatus(DrivingStatus.ACCEPTED);
        drivingRepository.save(driving);

        webSocketService.startDrivingNotification(new SimpleDrivingInfoDTO(driving),driving.getUsers());
        this.vehicleService.updateCurrentVehiclesLocation();

        return new DrivingDTO(driving);
    }

    public DrivingDTO finishDriving(final Long id) throws EntityNotFoundException {
        Driving driving = getDriving(id);
        driving.setActive(false);
        driving.setDrivingStatus(DrivingStatus.FINISHED);
        driving.setEnd(LocalDateTime.now());
        Driver driver = userService.getDriverById(driving.getDriverId());
        driver.getVehicle().setCurrentLocationIndex(-1);
        driver.getVehicle().setActiveRoute(null);

        drivingRepository.save(driving);
        webSocketService.finishDrivingNotification(new SimpleDrivingInfoDTO(driving),driving.getUsers());
        this.vehicleService.updateCurrentVehiclesLocation();
        Driving nextDriving = driverHasFutureDriving(driving.getDriverId());
        if (nextDriving != null) {
            createDrivingToDeparture(driver, driving.getRoute().getLocations().last().getLocation(), nextDriving.getRoute());
        }
        return new DrivingDTO(driving);
    }

    private Driving driverHasFutureDriving(final Long id) {

        List<Driving> drivings = drivingRepository.driverHasFutureDriving(id);
        return drivings.size() > 0?
            drivings.get(0): null;
    }

    private DrivingDTO createDrivingToDeparture(Driver driver, Location currentStop, Route nextRoute) {

        List<DrivingLocationIndexRequest> drivingLocationIndexRequestList = new LinkedList<>();
        DrivingLocationIndexRequest firstLocation = new DrivingLocationIndexRequest(
            new LocationRequest(currentStop.getCity(), currentStop.getStreet(), currentStop.getNumber(), currentStop.getZipCode(), currentStop.getLon(), currentStop.getLat()), 1
        );

        Location nextLocation = nextRoute.getLocations().first().getLocation();
        DrivingLocationIndexRequest secondLocation = new DrivingLocationIndexRequest(
            new LocationRequest(nextLocation.getCity(), nextLocation.getStreet(), nextLocation.getNumber(), nextLocation.getZipCode(), nextLocation.getLon(), nextLocation.getLat()), 2
        );

        drivingLocationIndexRequestList.add(firstLocation);
        drivingLocationIndexRequestList.add(secondLocation);
        Route route = this.routeService.createRoute(drivingLocationIndexRequestList, 0, 0, List.of(0));
        Driving driving = new Driving(0, LocalDateTime.now(), null, null, route, DrivingStatus.ON_WAY_TO_DEPARTURE, driver.getId(), 0);
        driver.getVehicle().setActiveRoute(driving.getRoute());
        driver.getVehicle().setCurrentLocationIndex(0);
        driver.getVehicle().setCurrentStop(driving.getRoute().getLocations().first().getLocation());

        return new DrivingDTO(drivingRepository.save(driving));
    }

    private boolean drivingShouldNotStartYet(Driving driving) {

        return ChronoUnit.MINUTES.between(LocalDateTime.now(), driving.getStarted()) > MAX_MINUTES_BEFORE_DRIVING_CAN_START;
    }

    private boolean driverHasActiveDriving(Long driverId) {

        return drivingRepository.getActiveDrivingForDriver(driverId).isPresent();
    }
}
