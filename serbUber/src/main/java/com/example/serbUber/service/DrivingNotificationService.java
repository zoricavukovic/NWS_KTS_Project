package com.example.serbUber.service;

import com.example.serbUber.dto.DrivingDTO;
import com.example.serbUber.dto.DrivingNotificationDTO;
import com.example.serbUber.dto.DrivingNotificationWebSocketDTO;
import com.example.serbUber.dto.DrivingStatusNotificationDTO;
import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.exception.EntityType;
import com.example.serbUber.exception.ExcessiveNumOfPassengersException;
import com.example.serbUber.exception.InvalidStartedDateTimeException;
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
import net.bytebuddy.asm.Advice;
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
            ) throws EntityNotFoundException, ExcessiveNumOfPassengersException, InvalidStartedDateTimeException {
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
        webSocketService.sendDrivingNotification(dto, users);
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
//        shouldFindDriver(drivingNotification);
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
        Driver driver = driverService.getDriverForDriving(drivingNotification);
        receiversReviewed.put(drivingNotification.getSender(), 0);
        if (driver == null) {
            DrivingStatusNotificationDTO drivingStatusNotificationDTO = new DrivingStatusNotificationDTO(0L, 0, DrivingStatus.PENDING, "Not found driver", null, drivingNotification.getId());
            webSocketService.sendDrivingStatus(drivingStatusNotificationDTO, receiversReviewed);
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
            if (isPaidDriving(passengers, drivingNotification.getPrice())) {
                drivingService.paidDriving(driving.getId());
                int minutesToStartDrive = calculateMinutesForStartDriving(driver.getId(), drivingNotification.getRoute());
                driving.setStarted(LocalDateTime.now().plusMinutes(minutesToStartDrive));
                DrivingDTO drivingDTO = drivingService.save(driving);
                DrivingStatusNotificationDTO drivingStatusNotificationDTO = new DrivingStatusNotificationDTO(driver.getId(), minutesToStartDrive, DrivingStatus.ACCEPTED, "", drivingDTO.getId(), drivingNotification.getId());
                webSocketService.sendDrivingStatus(drivingStatusNotificationDTO, receiversReviewed);
                System.out.println("placenoooo" + drivingStatusNotificationDTO.getMinutes());

            } else {
                drivingService.removeDriver(driving.getId());
                DrivingStatusNotificationDTO drivingStatusNotificationDTO = new DrivingStatusNotificationDTO(driver.getId(), 0, DrivingStatus.PAYING, "Payment was not successful. Please, check your tokens!", driving.getId(), drivingNotification.getId());
                webSocketService.sendDrivingStatus(drivingStatusNotificationDTO, receiversReviewed);
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

    public boolean isPaidDriving(final  Set<RegularUser> passengers, final double price){
        double priceForOnePassenger = Math.round(price/passengers.size()*100.0)/100.0;
        boolean isPaid = true;
        Map<Long, Double> usersEnoughTokens = haveUsersEnoughTokens(passengers, priceForOnePassenger);
        if(usersEnoughTokens.size()>0 && !usersEnoughTokens.containsValue(0)){
            usersEnoughTokens.forEach((passengerId, priceForRide) -> {
                try {
                    tokenBankService.updateNumOfTokens(passengerId, priceForRide);
                } catch (EntityNotFoundException e) {
                    System.out.println("Token bank not found");
                }
            });
        }
        else{
            isPaid = false;
        }
       return isPaid;
    }

    private Map<Long, Double> haveUsersEnoughTokens(
        final  Set<RegularUser> passengers,
        final double priceForOnePassenger
    ) {
        Map<Long, Double> usersHaveEnoughTokens = new HashMap<>();
        Set<Long> usersNotHaveEnoughTokens = new HashSet<>();
        passengers.forEach((passenger) -> {
            double passengerTokens = 0;
            try {
                passengerTokens = tokenBankService.getTokensForUser(passenger.getId());
            } catch (EntityNotFoundException e) {
                //BLALLLA
            }
            if(passengerTokens > priceForOnePassenger){
                usersHaveEnoughTokens.put(passenger.getId(), passengerTokens);
            }
            else{
                usersNotHaveEnoughTokens.add(passenger.getId());
            }
        });

        if(usersNotHaveEnoughTokens.size() > 0){
            double priceForUsersNotHaveEnoughTokens = usersNotHaveEnoughTokens.size() * priceForOnePassenger;
            double addedPrice = priceForUsersNotHaveEnoughTokens / usersHaveEnoughTokens.size();
            for(Map.Entry<Long, Double> passengerTokens : usersHaveEnoughTokens.entrySet()){
                if(passengerTokens.getValue() - addedPrice > 0){
                    passengerTokens.setValue(priceForOnePassenger + addedPrice);
                }
                else{
                    passengerTokens.setValue(0.0);
                }
            }
        }
        else{
            for(Map.Entry<Long, Double> passengerTokens : usersHaveEnoughTokens.entrySet()) {
                passengerTokens.setValue(priceForOnePassenger);
            }
        }


        return usersHaveEnoughTokens;
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
