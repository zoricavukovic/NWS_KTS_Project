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
import com.google.maps.errors.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static com.example.serbUber.exception.ErrorMessagesConstants.*;
import static com.example.serbUber.model.DrivingNotification.getListOfUsers;
import static com.example.serbUber.util.Constants.*;

@Component
@Qualifier("drivingNotificationServiceConfiguration")
public class DrivingNotificationService implements IDrivingNotificationService {
    private DrivingNotificationRepository drivingNotificationRepository;
    private RegularUserService regularUserService;
    private WebSocketService webSocketService;
    private DriverService driverService;
    private DrivingService drivingService;
    private RouteService routeService;
    private TokenBankService tokenBankService;
    private VehicleTypeInfoService vehicleTypeInfoService;

    @Autowired
    public DrivingNotificationService(
        final DrivingNotificationRepository drivingNotificationRepository,
        final RegularUserService regularUserService,
        final WebSocketService webSocketService,
        final DriverService driverService,
        final DrivingService drivingService,
        final RouteService routeService,
        final TokenBankService tokenBankService,
        final VehicleTypeInfoService vehicleTypeInfoService
    ){
        this.drivingNotificationRepository = drivingNotificationRepository;
        this.regularUserService = regularUserService;
        this.webSocketService = webSocketService;
        this.driverService = driverService;
        this.drivingService = drivingService;
        this.routeService = routeService;
        this.tokenBankService = tokenBankService;
        this.vehicleTypeInfoService = vehicleTypeInfoService;
    }

    public List<DrivingNotification> getAllNotReservation(){

        return drivingNotificationRepository.findAllNotReservation();
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

    public void deleteOutdatedReservationWithoutDriverNotification(final DrivingNotification notification){
        Map<RegularUser, Integer> receiversReviewed = notification.getReceiversReviewed();
        receiversReviewed.put(notification.getSender(), 0);
        webSocketService.sendDrivingStatus(DRIVER_NOT_FOUND_PATH, DRIVER_NOT_FOUND_MESSAGE, receiversReviewed);
        drivingNotificationRepository.deleteById(notification.getId());
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
            final double duration,
            final boolean babySeat,
            final boolean petFriendly,
            final String vehicleType,
            final LocalDateTime chosenDateTime,
            final boolean isReservation
    ) throws EntityNotFoundException, ExcessiveNumOfPassengersException, PassengerNotHaveTokensException, InvalidChosenTimeForReservationException, NotFoundException {
        RegularUser sender = regularUserService.getRegularByEmail(senderEmail);

        Map<RegularUser, Integer> receiversReviewed = new HashMap<>();
        for (String passengerEmail : passengers){
            receiversReviewed.put(regularUserService.getRegularByEmail(passengerEmail), NotificationReviewedType.NOT_REVIEWED.ordinal());
        }
        VehicleTypeInfo vehicleTypeInfo = vehicleTypeInfoService.get(VehicleType.getVehicleType(vehicleType));

        if(!vehicleTypeInfoService.isCorrectNumberOfSeats(vehicleTypeInfo, passengers.size()+1)) {
            throw new ExcessiveNumOfPassengersException(vehicleType);
        }

        Route route = routeService.createRoute(routeRequest.getLocations(), routeRequest.getTimeInMin(), routeRequest.getDistance(), routeRequest.getRoutePathIndex());
        LocalDateTime startedDateTime = getStartedDate(chosenDateTime, isReservation);
        DrivingNotification notification = createDrivingNotification(
            route, price, receiversReviewed, sender, startedDateTime,
            duration, babySeat, petFriendly, vehicleTypeInfo, isReservation
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

    private LocalDateTime getStartedDate(final LocalDateTime chosenDateTime, final boolean isReservation)
            throws InvalidChosenTimeForReservationException {

        return isReservation ? getStartedDateForReservation(chosenDateTime) : LocalDateTime.now();
    }

    private LocalDateTime getStartedDateForReservation(LocalDateTime chosenDateTime) throws InvalidChosenTimeForReservationException {

        if (LocalDateTime.now().plusHours(5).isBefore(chosenDateTime)){
            throw new InvalidChosenTimeForReservationException(INVALID_CHOSEN_TIME_AFTER_FOR_RESERVATION_MESSAGE);
        }

        if (chosenDateTime.isBefore(LocalDateTime.now().plusMinutes(HALF_AN_HOUR))){

            throw new InvalidChosenTimeForReservationException(INVALID_CHOSEN_TIME_BEFORE_FOR_RESERVATION_MESSAGE);
        }

        return chosenDateTime;
    }

    public DrivingNotificationDTO updateStatus(final Long id, final String email, final boolean accepted) throws EntityNotFoundException {
        DrivingNotification drivingNotification = drivingNotificationRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(id, EntityType.DRIVING_NOTIFICATION));
        updateReceiversReviewedByUserEmail(drivingNotification.getReceiversReviewed(), email, accepted);
        drivingNotificationRepository.save(drivingNotification);

        return new DrivingNotificationDTO(drivingNotification);
    }

    public boolean checkIfDrivingNotificationIsOutdated(final DrivingNotification drivingNotification) {

        return drivingNotification.getStarted().plusMinutes(TEN_MINUTES).isBefore(LocalDateTime.now());
    }

    public boolean checkIfUsersReviewed(final DrivingNotification drivingNotification) {

        Map<RegularUser, Integer> receiversReviewed = drivingNotification.getReceiversReviewed();
        boolean allPassengersReviewed = true;
        for(Map.Entry<RegularUser, Integer> receiverReview : receiversReviewed.entrySet()){
            if(receiverReview.getValue() == 1){
                sendPassengersNotAcceptDrivingNotification(receiversReviewed.keySet(), receiverReview.getKey().getEmail(), drivingNotification.getSender().getEmail());
                delete(drivingNotification);
                allPassengersReviewed = false;
                break;
            }
            else if(receiverReview.getValue() == 2){
                allPassengersReviewed = false;
                break;
            }
        }

        return allPassengersReviewed;
    }

    public List<DrivingNotification> getAllReservation() {

        return drivingNotificationRepository.findAllReservation();
    }

    public boolean checkTimeOfStartingReservationRide(final LocalDateTime started) {
        long minutesBetweenStartingRideAndNow = ChronoUnit.MINUTES.between(LocalDateTime.now(), started);

        return minutesBetweenStartingRideAndNow >= TWENTY_MINUTES && minutesBetweenStartingRideAndNow <= HALF_AN_HOUR;
    }

    public boolean checkTimeOfStartingReservationIsSoonRide(final LocalDateTime started) {
        long minutesBetweenStartingRideAndNow = ChronoUnit.MINUTES.between(LocalDateTime.now(), started);

        return minutesBetweenStartingRideAndNow < TWENTY_MINUTES;
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

    public void shouldFindDriver(final DrivingNotification drivingNotification) throws EntityNotFoundException, PassengerNotHaveTokensException {
        if (!drivingNotification.isReservation()){
            findDriverNow(drivingNotification);
            delete(drivingNotification);
        }
        else {
            Map<RegularUser, Integer> receiversReviewed = drivingNotification.getReceiversReviewed();
            receiversReviewed.put(drivingNotification.getSender(), 0);
            webSocketService.sendSuccessfulCreateReservation(getListOfUsers(receiversReviewed));
        }
    }

    public Driving findDriverNow(DrivingNotification drivingNotification) throws PassengerNotHaveTokensException, EntityNotFoundException {
        Map<RegularUser, Integer> receiversReviewed = drivingNotification.getReceiversReviewed();
        Driver driver = driverService.getDriverForDriving(drivingNotification);
        receiversReviewed.put(drivingNotification.getSender(), 0);
        if (driver == null && !drivingNotification.isReservation()) {
            webSocketService.sendDrivingStatus(DRIVER_NOT_FOUND_PATH, DRIVER_NOT_FOUND_MESSAGE, receiversReviewed);
        } else if (driver != null) {
            Set<RegularUser> passengers = getListOfUsers(receiversReviewed);
            Driving driving = drivingService.create(
                drivingNotification.getRoute().getTimeInMin(),
                drivingNotification.getStarted(),
                drivingNotification.getRoute(),
                DrivingStatus.PAYING,
                driver.getId(),
                passengers,
                drivingNotification.getPrice());
            passengers.remove(drivingNotification.getSender());
            if (isPaidDriving(driving.getPrice(), passengers, drivingNotification.getSender())) {
                double minutesToStartDrive = driverService.calculateMinutesToStartDriving(driver, driving);
                if(drivingNotification.isReservation()){
                    driving.setStarted(drivingNotification.getStarted());
                }
                else {
                    driving.setStarted(LocalDateTime.now().plusMinutes((long) minutesToStartDrive));
                }
                driving.setDrivingStatus(DrivingStatus.ACCEPTED);
                driving.setReservation(drivingNotification.isReservation());

                DrivingDTO drivingDTO = drivingService.save(driving);
                DrivingStatusNotificationDTO drivingStatusNotificationDTO = new DrivingStatusNotificationDTO(
                    driver.getId(), minutesToStartDrive, DrivingStatus.ACCEPTED, "",
                    drivingDTO.getId(), drivingNotification.getId(), driver.getVehicle().getId()
                );
                if(drivingNotification.isReservation()) {
                    delete(drivingNotification);
                }
                webSocketService.sendSuccessfulDriving(drivingStatusNotificationDTO, receiversReviewed);
                webSocketService.sendNewDrivingNotification(drivingStatusNotificationDTO, driver.getEmail());

                return driving;
            } else {
                drivingService.removeDriver(driving.getId());
                webSocketService.sendDrivingStatus(UNSUCCESSFUL_PAYMENT_PATH, UNSUCCESSFUL_PAYMENT_MESSAGE, receiversReviewed);
                delete(drivingNotification);
                throw new PassengerNotHaveTokensException();
            }

        }

        return null;
    }

    private boolean isPaidDriving(
        final double price,
        final  Set<RegularUser> passengers,
        final RegularUser sender
    ) throws EntityNotFoundException {
        Map<Long, Double> updatedUsersTokens = new HashMap<>();
        double priceForOnePassenger = getPriceForOnePassenger(price, passengers.size() + 1);
        double priceForSender = getPriceForSender(price, passengers.size()*priceForOnePassenger);
        double missingTokens = getMissingTokens(sender, priceForSender, updatedUsersTokens, START_MISSING_NUM_OF_TOKENS);

        for(RegularUser passenger : passengers) {
            missingTokens = getMissingTokens(passenger, priceForOnePassenger, updatedUsersTokens, missingTokens);
        }

        missingTokens = secondTryPayment(updatedUsersTokens, missingTokens);


        if(missingTokens == 0){
           for(Map.Entry<Long,Double> passengerToken : updatedUsersTokens.entrySet()){
               tokenBankService.updateNumOfTokens(passengerToken.getKey(), passengerToken.getValue());
           }
           return true;
        }

        return false;
    }

    private double secondTryPayment(Map<Long, Double> updatedUsersTokens, double missingTokens) {
        for(Map.Entry<Long, Double> passengerTokens : updatedUsersTokens.entrySet()) {
            if (passengerTokens.getValue() > 0 && missingTokens > 0) {
                if (passengerTokens.getValue() >= missingTokens) {
                    passengerTokens.setValue(passengerTokens.getValue() - missingTokens);
                    missingTokens = 0;
                    break;
                } else {
                    missingTokens -= passengerTokens.getValue();
                    passengerTokens.setValue(WITHOUT_TOKENS);
                }
            }
        }
        return missingTokens;
    }

    private double getMissingTokens(RegularUser user, double priceForUser, Map<Long, Double> updatedUsersTokens, double missingTokens) throws EntityNotFoundException {
        double tokensForUser = tokenBankService.getTokensForUser(user.getId());
        if (tokensForUser >= priceForUser) {
            updatedUsersTokens.put(user.getId(), tokensForUser - priceForUser);
        } else {
            missingTokens += priceForUser - tokensForUser;
            updatedUsersTokens.put(user.getId(), WITHOUT_TOKENS);
        }
        return missingTokens;
    }

    private double getPriceForSender(double price, double paidPrice) {

        return price - paidPrice;
    }

    private double getPriceForOnePassenger(double price, int numOfPassengers) {

        return Math.floor(price / numOfPassengers);
    }

    private DrivingNotification createDrivingNotification(
        final Route route,
        final double price,
        final Map<RegularUser, Integer> receiversReviewed,
        final RegularUser sender,
        final LocalDateTime started,
        final double duration,
        final boolean babySeat,
        final boolean petFriendly,
        final VehicleTypeInfo vehicleTypeInfo,
        final boolean isReservation
    ){

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
                receiversReviewed,
                isReservation
            )
        );
    }
}
