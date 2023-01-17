package com.example.serbUber.service;

import com.example.serbUber.dto.DrivingDTO;
import com.example.serbUber.dto.DrivingNotificationDTO;
import com.example.serbUber.dto.DrivingNotificationWebSocketDTO;
import com.example.serbUber.dto.DrivingStatusNotificationDTO;
import com.example.serbUber.exception.*;
import com.example.serbUber.model.*;
import com.example.serbUber.model.user.Driver;
import com.example.serbUber.model.user.RegularUser;
import com.example.serbUber.repository.DrivingNotificationRepository;
import com.example.serbUber.request.RouteRequest;
import com.example.serbUber.service.interfaces.IDrivingNotificationService;
import com.example.serbUber.service.payment.TokenBankService;
import com.example.serbUber.service.user.DriverService;
import com.example.serbUber.service.user.RegularUserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;

import static com.example.serbUber.model.DrivingNotification.getListOfUsers;
import static com.example.serbUber.util.Constants.*;

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
    private final VehicleTypeInfoService vehicleTypeInfoService;


    public DrivingNotificationService(
        final DrivingNotificationRepository drivingNotificationRepository,
        final RegularUserService regularUserService,
        final WebSocketService webSocketService,
        final VehicleService vehicleService,
        final DriverService driverService,
        final DrivingService drivingService,
        final RouteService routeService,
        final TokenBankService tokenBankService,
        final VehicleTypeInfoService vehicleTypeInfoService
    ){
        this.drivingNotificationRepository = drivingNotificationRepository;
        this.regularUserService = regularUserService;
        this.webSocketService = webSocketService;
        this.vehicleService = vehicleService;
        this.driverService = driverService;
        this.drivingService = drivingService;
        this.routeService = routeService;
        this.tokenBankService = tokenBankService;
        this.vehicleTypeInfoService = vehicleTypeInfoService;
    }

    public List<DrivingNotification> getAll(){
        return drivingNotificationRepository.findAll();
    }

    public void delete(DrivingNotification drivingNotification){
        drivingNotificationRepository.deleteById(drivingNotification.getId());
    }

    public void deleteOutdatedNotification(final DrivingNotification notification){
        drivingNotificationRepository.deleteById(notification.getId());
        sendWebSocketForDrivingNotification(
            notification.getId(),
            notification.getSender().getEmail(),
            DrivingNotificationType.DELETED,
            notification.getReceiversReviewed()
        );
    }

    public DrivingNotificationDTO get(Long id) throws EntityNotFoundException {

        return drivingNotificationRepository.findById(id).map(DrivingNotificationDTO::new)
                .orElseThrow(() -> new EntityNotFoundException(id, EntityType.DRIVING_NOTIFICATION));
    }

    public void sendPassengersNotAcceptDrivingNotification(Set<RegularUser> regularUsers, String userEmail, String senderEmail){
        webSocketService.passengerNotAcceptDrivingNotification(regularUsers, userEmail, senderEmail);
    }

    public DrivingNotificationDTO createDrivingNotificationDTO(
            final RouteRequest routeRequest,
            final String senderEmail,
            final double price,
            final List<String> passengers,
            final int duration,
            final boolean babySeat,
            final boolean petFriendly,
            final String vehicleType,
            final LocalDateTime chosenDateTime
            ) throws EntityNotFoundException, ExcessiveNumOfPassengersException, InvalidStartedDateTimeException, PassengerNotHaveTokensException {
        RegularUser sender = regularUserService.getRegularByEmail(senderEmail);

        Map<RegularUser, Integer> receiversReviewed = new HashMap<>();
        passengers.forEach(passengerEmail -> {
            try {
                receiversReviewed.put(regularUserService.getRegularByEmail(passengerEmail), NotificationReviewedType.NOT_REVIEWED.ordinal());
            } catch (EntityNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        if(!vehicleTypeInfoService.isCorrectNumberOfSeats(vehicleType, passengers.size()+1)) {
            throw new ExcessiveNumOfPassengersException(vehicleType);
        }

        VehicleTypeInfo vehicleTypeInfo = vehicleTypeInfoService.get(VehicleType.getVehicleType(vehicleType));
        Route route = routeService.createRoute(routeRequest.getLocations(), routeRequest.getTimeInMin(), routeRequest.getDistance(), routeRequest.getRoutePathIndex());
        LocalDateTime startedDateTime = getStartedDate(chosenDateTime);
        DrivingNotification notification = createDrivingNotification(
                route, price, receiversReviewed, sender, startedDateTime, duration, babySeat, petFriendly, vehicleTypeInfo
        );

        if(passengers.size() > 0){
            sendWebSocketForDrivingNotification(
                notification.getId(),
                notification.getSender().getEmail(),
                DrivingNotificationType.LINKED_USER,
                notification.getReceiversReviewed()
            );
        }
        else{
            shouldFindDriver(notification);
            delete(notification);
        }

        return new DrivingNotificationDTO(notification);
    }

    private void sendWebSocketForDrivingNotification(
        final Long notificationId,
        final String senderEmail,
        final DrivingNotificationType drivingNotificationType,
        final Map<RegularUser, Integer> users
    ) {
        DrivingNotificationWebSocketDTO dto = new DrivingNotificationWebSocketDTO(
            notificationId,
            senderEmail,
            drivingNotificationType
        );
        if(drivingNotificationType.equals(DrivingNotificationType.LINKED_USER)){
            webSocketService.sendPassengerAgreementNotification(dto, users);
        }
        else{
            webSocketService.sendDeletedDrivingNotification(dto, users);
        }
    }

    private LocalDateTime getStartedDate(LocalDateTime chosenDateTime) throws InvalidStartedDateTimeException {
        if(chosenDateTime != null){
            if(LocalDateTime.now().plusHours(5).isAfter(chosenDateTime)){
                return chosenDateTime;
            }

            throw new InvalidStartedDateTimeException();
        }

        return LocalDateTime.now();

    }

    public DrivingNotificationDTO updateStatus(final Long id, final String email, final boolean accepted) throws EntityNotFoundException {
        DrivingNotification drivingNotification = drivingNotificationRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(id, EntityType.DRIVING_NOTIFICATION));
        updateReceiversReviewedByUserEmail(drivingNotification.getReceiversReviewed(), email, accepted);
        drivingNotificationRepository.save(drivingNotification);

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


    public void shouldFindDriver(DrivingNotification drivingNotification) throws EntityNotFoundException, PassengerNotHaveTokensException {
        Map<RegularUser, Integer> receiversReviewed = drivingNotification.getReceiversReviewed();
        Driver driver = driverService.getDriverForDriving(drivingNotification);
        receiversReviewed.put(drivingNotification.getSender(), 0);
        if (driver == null) {
            webSocketService.sendDrivingStatus(DRIVER_NOT_FOUND_PATH, DRIVER_NOT_FOUND_MESSAGE, receiversReviewed);
        } else {
            Set<RegularUser> passengers = getListOfUsers(drivingNotification.getReceiversReviewed());
            Driving driving = drivingService.create(
                    drivingNotification.getDuration(),
                    drivingNotification.getStarted(),
                    drivingNotification.getStarted().plusMinutes(2),
                    drivingNotification.getRoute(),
                    DrivingStatus.PAYING,
                    driver.getId(),
                    passengers,
                    new HashMap<>(),
                    drivingNotification.getPrice());
            if (isPaidDriving(passengers, driving.getPrice())) {
                int minutesToStartDrive = calculateMinutesForStartDriving(driver.getId(), drivingNotification.getRoute());
                driving.setStarted(LocalDateTime.now().plusMinutes(minutesToStartDrive));
                driving.setDrivingStatus(DrivingStatus.ACCEPTED);
                DrivingDTO drivingDTO = drivingService.save(driving);
                DrivingStatusNotificationDTO drivingStatusNotificationDTO = new DrivingStatusNotificationDTO(driver.getId(), minutesToStartDrive, DrivingStatus.ACCEPTED, "", drivingDTO.getId(), drivingNotification.getId());
                webSocketService.sendSuccessfulDriving(drivingStatusNotificationDTO, receiversReviewed);
            } else {
                drivingService.removeDriver(driving.getId());
                webSocketService.sendDrivingStatus(UNSUCCESSFUL_PAYMENT_PATH, UNSUCCESSFUL_PAYMENT_MESSAGE, receiversReviewed);
                throw new PassengerNotHaveTokensException();
            }

        }
    }

    public int calculateMinutesForStartDriving(final Long driverId, final Route route) throws EntityNotFoundException {
//        Driver driver = driverService.getDriverById(driverId);
//        Location userLocation = route.getLocations().first().getLocation();
//        GHRequest request = new GHRequest(
//            vehicleService.getLatOfCurrentVehiclePosition(driver.getVehicle()),
//            vehicleService.getLonOfCurrentVehiclePosition(driver.getVehicle()),
//            userLocation.getLat(),
//            userLocation.getLon()
//        );
//        request.setProfile("car");
//        GHResponse routeHopper = hopper.route(request);
//        System.out.println("vreemeee" + TimeUnit.MILLISECONDS.toMinutes(routeHopper.getBest().getTime()));
//        System.out.println((routeHopper.getBest().getTime()/1000)/60);
//        return TimeUnit.MILLISECONDS.toMinutes(routeHopper.getBest().getTime()) + 1;
        return 5;
    }

    private boolean isPaidDriving(
        final  Set<RegularUser> passengers,
        final double priceForOnePassenger
    ) throws EntityNotFoundException {
        Map<Long, Double> updatedUsersTokens = new HashMap<>();
        double missingTokens = 0;
        for(RegularUser passenger : passengers) {
            double passengerTokens = tokenBankService.getTokensForUser(passenger.getId());
            if (passengerTokens >= priceForOnePassenger) {
                updatedUsersTokens.put(passenger.getId(), passengerTokens - priceForOnePassenger);
            } else {
                missingTokens += priceForOnePassenger - passengerTokens;
                updatedUsersTokens.put(passenger.getId(), 0.0);
            }
        }
            for(Map.Entry<Long, Double> passengerTokens : updatedUsersTokens.entrySet()) {
                if (passengerTokens.getValue() > 0 && missingTokens > 0) {
                    if (passengerTokens.getValue() >= missingTokens) {
                        passengerTokens.setValue(passengerTokens.getValue() - missingTokens);
                        break;
                    } else {
                        missingTokens -= passengerTokens.getValue();
                        passengerTokens.setValue(0.0);
                    }
                }
            }


        if(missingTokens == 0){
           for(Map.Entry<Long,Double> passengerToken : updatedUsersTokens.entrySet()){
               tokenBankService.updateNumOfTokens(passengerToken.getKey(), passengerToken.getValue());
           }
           return true;
        }

        return false;
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
        final VehicleTypeInfo vehicleTypeInfo
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
                vehicleTypeInfo,
                receiversReviewed
            )
        );
    }
}
