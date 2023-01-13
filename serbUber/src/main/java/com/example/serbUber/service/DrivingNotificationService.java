package com.example.serbUber.service;

import com.example.serbUber.dto.DrivingDTO;
import com.example.serbUber.dto.DrivingNotificationDTO;
import com.example.serbUber.dto.DrivingStatusNotificationDTO;
import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.exception.EntityType;
import com.example.serbUber.model.*;
import com.example.serbUber.model.user.Driver;
import com.example.serbUber.model.user.RegularUser;
import com.example.serbUber.repository.DrivingNotificationRepository;
import com.example.serbUber.request.RouteRequest;
import com.example.serbUber.service.interfaces.IDrivingNotificationService;
import com.example.serbUber.service.payment.TokenBankService;
import com.example.serbUber.service.user.DriverService;
import com.example.serbUber.service.user.RegularUserService;
import com.graphhopper.GHRequest;
import com.graphhopper.GHResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import static com.example.serbUber.SerbUberApplication.hopper;
import static com.example.serbUber.model.DrivingNotification.getListOfUsers;

@Component
@Qualifier("drivingNotificationServiceConfiguration")
public class DrivingNotificationService implements IDrivingNotificationService {
    private final DrivingNotificationRepository drivingNotificationRepository;
    private final RegularUserService regularUserService;
    private final WebSocketService webSocketService;
    private final VehicleService vehicleService;
    private final DriverService driverService;
    private final DrivingService drivingService;
    private final RouteService routeService;
    private final TokenBankService tokenBankService;


    public DrivingNotificationService(
        final DrivingNotificationRepository drivingNotificationRepository,
        final RegularUserService regularUserService,
        final WebSocketService webSocketService,
        final VehicleService vehicleService,
        final DriverService driverService,
        final DrivingService drivingService,
        final RouteService routeService,
        final TokenBankService tokenBankService
    ){
        this.drivingNotificationRepository = drivingNotificationRepository;
        this.regularUserService = regularUserService;
        this.webSocketService = webSocketService;
        this.vehicleService = vehicleService;
        this.driverService = driverService;
        this.drivingService = drivingService;
        this.routeService = routeService;
        this.tokenBankService = tokenBankService;
    }

    public DrivingNotificationDTO createDrivingNotificationDTO(
            final RouteRequest routeRequest,
            final String senderEmail,
            final double price,
            final List<String> passengers,
            final int duration,
            final boolean babySeat,
            final boolean petFriendly,
            final String vehicleType
    ) throws EntityNotFoundException {
        RegularUser sender = regularUserService.getRegularByEmail(senderEmail);

        Map<RegularUser, Integer> receiversReviewed = new HashMap<>();
        passengers.forEach(passengerEmail -> {
            try {
                receiversReviewed.put(regularUserService.getRegularByEmail(passengerEmail), NotificationReviewedType.NOT_REVIEWED.ordinal());
            } catch (EntityNotFoundException e) {
                throw new RuntimeException(e);
            }
        });

        //TODO:
        Vehicle selectedVehicle = vehicleService.getVehicleByType(vehicleType);
        Route route = routeService.createRoute(routeRequest.getLocations(), routeRequest.getTimeInMin(), routeRequest.getDistance(), routeRequest.getRoutePathIndex());
        DrivingNotification notification = createDrivingNotification(
                route, price, receiversReviewed, sender, LocalDateTime.now(), duration, babySeat, petFriendly, selectedVehicle
        );
        if(passengers.size() > 0){
            webSocketService.sendDrivingNotification(notification);
        }
        else{
            shouldFindDriver(notification);
        }

        return new DrivingNotificationDTO(notification);
    }

    public DrivingNotificationDTO updateStatus(final Long id, final String email, final boolean accepted) throws EntityNotFoundException {
        DrivingNotification drivingNotification = drivingNotificationRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(id, EntityType.DRIVING_NOTIFICATION));
        updateReceiversReviewedByUserEmail(drivingNotification.getReceiversReviewed(), email, accepted);
        drivingNotificationRepository.save(drivingNotification);
        shouldFindDriver(drivingNotification);
        return new DrivingNotificationDTO(drivingNotification);
    }

    private void updateReceiversReviewedByUserEmail(
        final Map<RegularUser, Integer> receiversReviewed,
        final String email,
        final boolean accept
    ) {
        receiversReviewed.forEach((key, value) -> {
            if (key.getEmail().equals(email)){
                receiversReviewed.put(key, accept ? 0 : 1); //true -> vrednost nula -> accepted
            }
        });
    }


    private boolean allPassengersAcceptDriving(DrivingNotification drivingNotification){
        Collection<Integer> values = drivingNotification.getReceiversReviewed().values().stream().filter(
            value -> value == 2
        ).toList();

        return values.size() == 0;
    }

    public void shouldFindDriver(DrivingNotification drivingNotification) throws EntityNotFoundException {
        Map<RegularUser, Integer> receiversReviewed = drivingNotification.getReceiversReviewed();
        if (allPassengersAcceptDriving(drivingNotification)) {
            System.out.println("acccceptt");
            Driver driver = driverService.getDriverForDriving(drivingNotification);
            receiversReviewed.put(drivingNotification.getSender(), 0);
            if (driver == null) {
                System.out.println("nema vozacaaa");
                DrivingStatusNotificationDTO drivingStatusNotificationDTO = new DrivingStatusNotificationDTO(0L, 0, DrivingStatus.PENDING, "Not found driver", null, drivingNotification.getId());
                webSocketService.sendDrivingStatus(drivingStatusNotificationDTO, receiversReviewed);
            } else {
                System.out.println("vozaaacccc" + driver.getEmail());
                Driving driving = drivingService.create(
                        drivingNotification.getDuration(),
                        drivingNotification.getStarted(),
                        drivingNotification.getStarted().plusMinutes(2),
                        drivingNotification.getRoute(),
                        DrivingStatus.PAYING,
                        driver.getId(),
                        getListOfUsers(drivingNotification.getReceiversReviewed()),
                        new HashMap<>(),
                        drivingNotification.getPrice());
                if (isPaidDriving(drivingNotification.getReceiversReviewed(), drivingNotification.getPrice())) {
                    drivingService.paidDriving(driving.getId());
                    int minutesToStartDrive = calculateMinutesForStartDriving(driver.getId(), drivingNotification.getRoute());
                    driving.setStarted(LocalDateTime.now().plusMinutes(minutesToStartDrive));
                    DrivingDTO drivingDTO = drivingService.save(driving);
                    DrivingStatusNotificationDTO drivingStatusNotificationDTO = new DrivingStatusNotificationDTO(driver.getId(), minutesToStartDrive, DrivingStatus.ACCEPTED, "", drivingDTO.getId(), drivingNotification.getId());
                    webSocketService.sendDrivingStatus(drivingStatusNotificationDTO, receiversReviewed);
                    System.out.println("placenoooo" + drivingStatusNotificationDTO.getMinutes());

                } else {
                    drivingService.removeDriver(driving.getId());
                    DrivingStatusNotificationDTO drivingStatusNotificationDTO = new DrivingStatusNotificationDTO(driver.getId(), 0, DrivingStatus.REJECTED, "Payment was not successful. Please, check your tokens!", driving.getId(), drivingNotification.getId());
                    webSocketService.sendDrivingStatus(drivingStatusNotificationDTO, receiversReviewed);
                }

            }
        }
    }

    public int calculateMinutesForStartDriving(final Long driverId, final Route route) throws EntityNotFoundException {
        Driver driver = driverService.getDriverById(driverId);
        Location userLocation = route.getLocations().first().getLocation();
        GHRequest request = new GHRequest(
            vehicleService.getLatOfCurrentVehiclePosition(driver.getVehicle()),
            vehicleService.getLonOfCurrentVehiclePosition(driver.getVehicle()),
            userLocation.getLat(),
            userLocation.getLon()
        );
        request.setProfile("car");
//        GHResponse routeHopper = hopper.route(request);
//        System.out.println("vreemeee" + TimeUnit.MILLISECONDS.toMinutes(routeHopper.getBest().getTime()));
//        System.out.println((routeHopper.getBest().getTime()/1000)/60);
//        return TimeUnit.MILLISECONDS.toMinutes(routeHopper.getBest().getTime()) + 1;
        return 5;
    }

    public boolean isPaidDriving(final  Map<RegularUser, Integer> receiversReviewed, final double price){
        double priceForOnePassenger = Math.round(price/receiversReviewed.size()*100.0)/100.0;
        Map<Long, Boolean> usersEnoughTokens = haveUsersEnoughTokens(receiversReviewed, priceForOnePassenger);
        if(!usersEnoughTokens.containsValue(false)){
            receiversReviewed.forEach((passenger, value) -> {
                try {
                    tokenBankService.updateNumOfTokens(passenger.getId(), priceForOnePassenger);
                } catch (EntityNotFoundException e) {
                    System.out.println("Token bank not found");
                }
            });
        }
        return true;
    }

    private Map<Long, Boolean> haveUsersEnoughTokens(
        final  Map<RegularUser, Integer> receiversReviewed,
        final double priceForOnePassenger
    ) {
        Map<Long, Boolean> usersEnoughTokens = new HashMap<>();
        receiversReviewed.forEach((passenger, value) -> {
            double passengerTokens = 0;
            try {
                passengerTokens = tokenBankService.getTokensForUser(passenger.getId());
            } catch (EntityNotFoundException e) {
                //BLALLLA
            }
            usersEnoughTokens.put(passenger.getId(), passengerTokens > priceForOnePassenger);
        });

        return usersEnoughTokens;
    }


    public DrivingNotificationDTO getDrivingNotification(Long id) throws EntityNotFoundException {
        DrivingNotification drivingNotification =  drivingNotificationRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(id, EntityType.DRIVING_NOTIFICATION));

        return new DrivingNotificationDTO(drivingNotification);
    }

    private DrivingNotification createDrivingNotification(
        final Route route,
        final double price,
        final Map<RegularUser, Integer> receiversReviewed,
        final RegularUser sender,
        final LocalDateTime started,
        final int duration,
        final boolean babySeat,
        final boolean petFriendly,
        final Vehicle vehicle
    )
    {

        return drivingNotificationRepository.save(
            new DrivingNotification(
                route,
                price,
                sender,
                started,
                duration,
                babySeat,
                petFriendly,
                vehicle,
                receiversReviewed
            )
        );
    }
}
