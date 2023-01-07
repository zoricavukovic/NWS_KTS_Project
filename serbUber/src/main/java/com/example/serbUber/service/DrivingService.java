package com.example.serbUber.service;

import com.example.serbUber.dto.DrivingDTO;
import com.example.serbUber.dto.DrivingPageDTO;
import com.example.serbUber.dto.DrivingStatusNotificationDTO;
import com.example.serbUber.dto.SimpleDrivingInfoDTO;
import com.example.serbUber.exception.DriverAlreadyHasStartedDrivingException;
import com.example.serbUber.exception.DrivingShouldNotStartYetException;
import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.exception.EntityType;
import com.example.serbUber.model.*;
import com.example.serbUber.model.user.RegularUser;
import com.example.serbUber.model.user.User;
import com.example.serbUber.repository.DrivingRepository;
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

    public DrivingService(
        final DrivingRepository drivingRepository,
        final UserService userService,
        final WebSocketService webSocketService,
        final DrivingStatusNotificationService drivingStatusNotificationService
    ) {
        this.drivingRepository = drivingRepository;
        this.userService = userService;
        this.webSocketService = webSocketService;
        this.drivingStatusNotificationService = drivingStatusNotificationService;
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
    ) {
        LocalDateTime end = started.plusMinutes(duration);

        Driving driving = drivingRepository.save(new Driving(duration, started, end, payingLimit, route, drivingStatus, driverId, price));
        users.forEach(user -> {
            List<Driving> drivingsOfUser = user.getDrivings();
            drivingsOfUser.add(driving);
            user.setDrivings(drivingsOfUser);
            userService.saveUser(user);
        });


        return new DrivingDTO(driving);
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
//        User driver = userService.getUserById(driving.getDriverId());
        driving.setDrivingStatus(DrivingStatus.REJECTED);
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
                drivingStatusNotification.getDriving().getUsers());

        return new DrivingDTO(driving);
    }

    public DrivingDTO startDriving(final Long id) throws EntityNotFoundException, DriverAlreadyHasStartedDrivingException, DrivingShouldNotStartYetException {
        Driving driving = getDriving(id);
        if (driverHasActiveDriving(driving.getDriverId())){
            throw new DriverAlreadyHasStartedDrivingException();
        }

        if (drivingShouldNotStartYet(driving)){
            throw new DrivingShouldNotStartYetException();
        }

        driving.setStarted(LocalDateTime.now());
        driving.setActive(true);
        driving.setDrivingStatus(DrivingStatus.ACCEPTED);
        drivingRepository.save(driving);

        webSocketService.startDrivingNotification(new SimpleDrivingInfoDTO(driving),driving.getUsers());

        return new DrivingDTO(driving);
    }

    public DrivingDTO finishDriving(final Long id) throws EntityNotFoundException {
        Driving driving = getDriving(id);
        driving.setActive(false);
        driving.setDrivingStatus(DrivingStatus.FINISHED);
        driving.setEnd(LocalDateTime.now());

        drivingRepository.save(driving);

        webSocketService.finishDrivingNotification(new SimpleDrivingInfoDTO(driving),driving.getUsers());

        return new DrivingDTO(driving);
    }

    private boolean drivingShouldNotStartYet(Driving driving) {

        return ChronoUnit.MINUTES.between(LocalDateTime.now(), driving.getStarted()) > MAX_MINUTES_BEFORE_DRIVING_CAN_START;
    }

    private boolean driverHasActiveDriving(Long driverId) throws DriverAlreadyHasStartedDrivingException {

        return drivingRepository.getActiveDrivingForDriver(driverId).isPresent();
    }
}
