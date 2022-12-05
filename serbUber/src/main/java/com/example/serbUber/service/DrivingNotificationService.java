package com.example.serbUber.service;

import com.example.serbUber.dto.DrivingDTO;
import com.example.serbUber.dto.DrivingNotificationDTO;
import com.example.serbUber.dto.DrivingStatusNotificationDTO;
import com.example.serbUber.dto.user.DriverDTO;
import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.exception.EntityType;
import com.example.serbUber.model.*;
import com.example.serbUber.model.token.TokenBank;
import com.example.serbUber.model.user.Driver;
import com.example.serbUber.model.user.RegularUser;
import com.example.serbUber.model.user.User;
import com.example.serbUber.repository.DrivingNotificationRepository;
import com.example.serbUber.request.DrivingLocationIndexRequest;
import com.example.serbUber.request.RouteRequest;
import com.example.serbUber.service.interfaces.IDrivingNotificationService;
import com.example.serbUber.service.payment.TokenBankService;
import com.example.serbUber.service.user.DriverService;
import com.example.serbUber.service.user.RegularUserService;
import com.graphhopper.GHRequest;
import com.graphhopper.GHResponse;
import com.paypal.api.payments.BankToken;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;

import static com.example.serbUber.SerbUberApplication.hopper;

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
            final LocalDateTime started,
            final int duration,
            final boolean babySeat,
            final boolean petFriendly,
            final String vehicleType
    ) throws EntityNotFoundException {
        RegularUser sender = regularUserService.getRegularByEmail(senderEmail);
        Set<RegularUser> receivers = new HashSet<>();
        passengers.forEach(passengerEmail -> {
            try {
                receivers.add(regularUserService.getRegularByEmail(passengerEmail));
            } catch (EntityNotFoundException e) {
                throw new RuntimeException(e); ////???
            }
        });
        //TODO:
        Vehicle selectedVehicle = vehicleService.getVehicleByType(vehicleType);
        Route route = routeService.createRoute(routeRequest.getLocations(), routeRequest.getTimeInMin(), routeRequest.getDistance(), routeRequest.getRoutePathIndex());
        DrivingNotification notification = createDrivingNotification(
                route, price, receivers, sender, started, duration, babySeat, petFriendly, selectedVehicle
        );
        if(passengers.size() > 0){
            webSocketService.sendDrivingNotification(notification);
        }
        else{
            shouldFindDriver(notification);
        }

        return new DrivingNotificationDTO(notification);
    }

    public DrivingNotificationDTO acceptDriving(final Long id) throws EntityNotFoundException {
        DrivingNotification drivingNotification = drivingNotificationRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(id, EntityType.DRIVING_NOTIFICATION));
        drivingNotification.setAnsweredPassengers(drivingNotification.getAnsweredPassengers()+1);
        drivingNotificationRepository.save(drivingNotification);
        shouldFindDriver(drivingNotification);
        return new DrivingNotificationDTO(drivingNotification);
    }

    private boolean allPassengersAcceptDriving(DrivingNotification drivingNotification){

        return drivingNotification.getAnsweredPassengers() == drivingNotification.getReceivers().size();
    }

    public void shouldFindDriver(DrivingNotification drivingNotification) throws EntityNotFoundException {
        Set<RegularUser> passengers = drivingNotification.getReceivers();
        if (allPassengersAcceptDriving(drivingNotification)) {
            System.out.println("acccceptt");
            Driver driver = driverService.getDriverForDriving(drivingNotification);
            passengers.add(drivingNotification.getSender());
            if (driver == null) {
                DrivingStatusNotificationDTO drivingStatusNotificationDTO = new DrivingStatusNotificationDTO(0L, 0, DrivingStatus.PENDING, "Not found driver", drivingNotification.getId());
                webSocketService.sendDrivingStatus(drivingStatusNotificationDTO, passengers);
            } else {
                System.out.println("vozaaacccc" + driver.getEmail());
                DrivingDTO drivingDTO = drivingService.create(
                        drivingNotification.getDuration(),
                        drivingNotification.getStarted(),
                        drivingNotification.getStarted().plusMinutes(2),
                        drivingNotification.getRoute(),
                        DrivingStatus.PAYING,
                        driver.getId(),
                        drivingNotification.getReceivers(),
                        new HashMap<>(),
                        drivingNotification.getPrice());
                System.out.println("voznjaaa" + drivingDTO.getId());
                if (isPaidDriving(drivingNotification.getReceivers(), drivingNotification.getPrice())) {
                    drivingService.paidDriving(drivingDTO.getId());
                    DrivingStatusNotificationDTO drivingStatusNotificationDTO = new DrivingStatusNotificationDTO(driver.getId(), calculateMinutesForStartDriving(driver.getId(), drivingNotification.getRoute()), DrivingStatus.ACCEPTED, "", drivingDTO.getId());
                    webSocketService.sendDrivingStatus(drivingStatusNotificationDTO, passengers);
                    System.out.println("placenoooo" + drivingStatusNotificationDTO.getMinutes());
                } else {
                    drivingService.removeDriver(drivingDTO.getId());
                    DrivingStatusNotificationDTO drivingStatusNotificationDTO = new DrivingStatusNotificationDTO(driver.getId(), 0, DrivingStatus.PAYING, "Payment was not successful. Please, check your tokens!", drivingDTO.getId());
                    webSocketService.sendDrivingStatus(drivingStatusNotificationDTO, passengers);
                }

            }
        }
    }

    public double calculateMinutesForStartDriving(final Long driverId, final Route route) throws EntityNotFoundException {
        Driver driver = driverService.getDriverById(driverId);
        Location currentLocationForDriver = driver.getCurrentLocation();
        Location userLocation = route.getLocations().first().getLocation();
        GHRequest request = new GHRequest(currentLocationForDriver.getLat(), currentLocationForDriver.getLon(), userLocation.getLat(), userLocation.getLon());
        request.setProfile("car");
        GHResponse routeHopper = hopper.route(request);
        return routeHopper.getBest().getTime();
    }

    public boolean isPaidDriving(final Set<RegularUser> passengers, final double price){
        double priceForOnePassenger = Math.round(price/passengers.size()*100.0)/100.0;
        Map<Long, Boolean> usersEnoughTokens = haveUsersEnoughTokens(passengers, priceForOnePassenger);
        if(!usersEnoughTokens.containsValue(false)){
            passengers.forEach(passenger -> {
                try {
                    tokenBankService.updateNumOfTokens(passenger.getId(), priceForOnePassenger);
                } catch (EntityNotFoundException e) {
                    System.out.println("Token bank not found");
                }
            });
        }
        return true;
    }

    private Map<Long, Boolean> haveUsersEnoughTokens(Set<RegularUser> passengers, double priceForOnePassenger) {
        Map<Long, Boolean> usersEnoughTokens = new HashMap<>();
        passengers.forEach(passenger -> {
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
        final Set<RegularUser> receivers,
        final RegularUser sender,
        final LocalDateTime started,
        final int duration,
        final boolean babySeat,
        final boolean petFriendly,
        final Vehicle vehicle
    )
    {

        return drivingNotificationRepository.save(
            new DrivingNotification(route, price, sender, started, duration, babySeat, petFriendly, vehicle, receivers)
        );
    }
}
