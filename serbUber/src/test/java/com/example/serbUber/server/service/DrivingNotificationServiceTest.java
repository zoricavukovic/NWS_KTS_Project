package com.example.serbUber.server.service;

import com.example.serbUber.dto.DrivingDTO;
import com.example.serbUber.dto.DrivingNotificationDTO;
import com.example.serbUber.dto.DrivingNotificationWebSocketDTO;
import com.example.serbUber.dto.DrivingStatusNotificationDTO;
import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.exception.ExcessiveNumOfPassengersException;
import com.example.serbUber.exception.InvalidChosenTimeForReservationException;
import com.example.serbUber.exception.PassengerNotHaveTokensException;
import com.example.serbUber.model.*;
import com.example.serbUber.model.user.Driver;
import com.example.serbUber.model.user.RegularUser;
import com.example.serbUber.repository.DrivingNotificationRepository;
import com.example.serbUber.request.DrivingLocationIndexRequest;
import com.example.serbUber.request.LocationRequest;
import com.example.serbUber.request.RouteRequest;
import com.example.serbUber.service.*;
import com.example.serbUber.service.payment.TokenBankService;
import com.example.serbUber.service.user.DriverService;
import com.example.serbUber.service.user.RegularUserService;
import com.google.maps.errors.NotFoundException;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static com.example.serbUber.server.service.helper.Constants.*;
import static com.example.serbUber.server.service.helper.DrivingNotificationUtil.getDrivingNotification;
import static com.example.serbUber.server.service.helper.RegularUserConstants.*;
import static com.example.serbUber.server.service.helper.VehicleTypeInfoConstants.SUV;
import static com.example.serbUber.server.service.helper.VehicleTypeInfoConstants.VEHICLE_TYPE_INFO_SUV;
import static com.example.serbUber.util.Constants.DRIVER_NOT_FOUND_MESSAGE;
import static com.example.serbUber.util.Constants.DRIVER_NOT_FOUND_PATH;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.DisplayName.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DrivingNotificationServiceTest {

    @Mock
    private DrivingNotificationRepository drivingNotificationRepository;

    @Mock
    private RegularUserService regularUserService;

    @Mock
    private DriverService driverService;

    @Mock
    private WebSocketService webSocketService;

    @Mock
    private DrivingService drivingService;

    @Mock
    private RouteService routeService;

    @Mock
    private VehicleTypeInfoService vehicleTypeInfoService;

    @Mock
    private TokenBankService tokenBankService;

    @Captor
    private ArgumentCaptor<DrivingNotification> postArgumentCaptor;

    @Captor
    private ArgumentCaptor<Driving> drivingArgumentCaptor;

    @InjectMocks
    private DrivingNotificationService drivingNotificationService;

    @BeforeAll
    public void setup() {
        MockitoAnnotations.openMocks(this);

        when(drivingNotificationRepository.findById(NOT_EXIST_OBJECT_ID))
                .thenReturn(Optional.empty());
    }

    @Test
    @DisplayName("T1-Should throw driving notification not found exception")
    public void shouldThrowDrivingNotificationNotFoundException() {

        assertThrows(EntityNotFoundException.class,
                () -> drivingNotificationService.updateStatus(NOT_EXIST_OBJECT_ID, USER_EMAIL, true));

        verify(drivingNotificationRepository, times(0)).save(any(DrivingNotification.class));
    }

    @Test
    @DisplayName("T2-Should success update driving notification user accept/reject status")
    public void shouldSuccessDrivingNotificationUserAcceptDriving() throws EntityNotFoundException {

        Map<RegularUser, Integer> receiversReviewed = getRegularUserIntegerMap();
        DrivingNotification drivingNotification = mockAndGetDrivingNotification(new RegularUser(), receiversReviewed, STARTED, true);
        when(drivingNotificationRepository.findById(EXIST_OBJECT_ID))
                .thenReturn(Optional.of(drivingNotification));

        DrivingNotificationDTO drivingNotificationDTO = drivingNotificationService.updateStatus(
                EXIST_OBJECT_ID,
                SECOND_USER_EMAIL,
                true
        );
        verify(drivingNotificationRepository).save(postArgumentCaptor.capture());
        Assertions.assertEquals(drivingNotificationDTO.getPrice(), drivingNotification.getPrice());
        Assertions.assertEquals(postArgumentCaptor.getValue().getReceiversReviewed().get(FIRST_USER), NOT_REVIEWED_LINKED_REQUEST);
        Assertions.assertEquals(postArgumentCaptor.getValue().getReceiversReviewed().get(SECOND_USER), ACCEPT_DRIVING);
    }

    @Test
    @DisplayName("T3-Should throw not found exception when user is not found")
    public void createDrivingNotificationDTO_throwEntityNotFoundExceptionForRegularUser() throws EntityNotFoundException {
        when(regularUserService.getRegularByEmail(NOT_EXIST_USER_EMAIL)).thenThrow(EntityNotFoundException.class);
        assertThrows(EntityNotFoundException.class, () -> drivingNotificationService.createDrivingRequest(
                new RouteRequest(), NOT_EXIST_USER_EMAIL, PRICE, new ArrayList<>(), DURATION,
                false, false, "SUV", null, false));
        verify(vehicleTypeInfoService, never()).isCorrectNumberOfSeats(any(VehicleTypeInfo.class), anyInt());
        verify(regularUserService).getRegularByEmail(anyString());
    }

    @Test
    @DisplayName("T4-Should throw exception when passenger is not found")
    public void createDrivingNotificationDTO_throwEntityNotFoundExceptionForPassenger() throws EntityNotFoundException {
        when(regularUserService.getRegularByEmail(FIRST_USER_EMAIL)).thenReturn(FIRST_USER);
        when(regularUserService.getRegularByEmail(NOT_EXIST_USER_EMAIL)).thenThrow(EntityNotFoundException.class);
        List<String> users = new ArrayList<>();
        users.add(NOT_EXIST_USER_EMAIL);
        assertThrows(EntityNotFoundException.class, () -> drivingNotificationService.createDrivingRequest(
                new RouteRequest(), FIRST_USER_EMAIL, PRICE, users, DURATION,
                false, false, SUV, null, false));
        verify(vehicleTypeInfoService, never()).get(any(VehicleType.class));
        verify(routeService, never()).createRoute(anyList(), anyDouble(), anyDouble(), anyList());
    }

    @Test
    @DisplayName("T5-Should throw exception when vehicle type info is not found")
    public void createDrivingNotificationDTO_throwEntityNotFoundExceptionForVehicleTypeInfo() throws EntityNotFoundException {
        mockRegularUserService();
        when(vehicleTypeInfoService.get(VehicleType.SUV)).thenThrow(EntityNotFoundException.class);
        List<String> users = new ArrayList<>();
        users.add(SECOND_USER_EMAIL);
        assertThrows(EntityNotFoundException.class, () -> drivingNotificationService.createDrivingRequest(
                new RouteRequest(), FIRST_USER_EMAIL, PRICE, users, DURATION,
                false, false, SUV, null, false));
        verify(vehicleTypeInfoService, never()).isCorrectNumberOfSeats(any(VehicleTypeInfo.class), anyInt());
    }

    @Test
    @DisplayName("T6-Should throw exception when number of passengers is greater than num of seats")
    public void createDrivingNotificationDTO_throwExcessiveNumOfPassengersException() throws EntityNotFoundException {
        mockRegularUserService();
        mockVehicleTypeInfoService();
        mockVehicleSeats(3, false);

        List<String> passengers = Arrays.asList(SECOND_USER_EMAIL, SECOND_USER_EMAIL);
        assertThrows(ExcessiveNumOfPassengersException.class,
                () -> drivingNotificationService.createDrivingRequest(
                        new RouteRequest(), FIRST_USER_EMAIL, PRICE, passengers, DURATION, false, false, SUV, null, false
                )
        );

        verify(routeService, never()).createRoute(anyList(), anyDouble(), anyDouble(), anyList());
    }

    private void mockVehicleSeats(int numOfPassengers, boolean isCorrectNumberOfSeats) {
        when(vehicleTypeInfoService.isCorrectNumberOfSeats(VEHICLE_TYPE_INFO_SUV, numOfPassengers))
                .thenReturn(isCorrectNumberOfSeats);
    }

    private void mockVehicleTypeInfoService() throws EntityNotFoundException {
        when(vehicleTypeInfoService.get(VehicleType.SUV)).thenReturn(VEHICLE_TYPE_INFO_SUV);
    }

    private void mockRegularUserService() throws EntityNotFoundException {
        when(regularUserService.getRegularByEmail(FIRST_USER_EMAIL)).thenReturn(FIRST_USER);
        when(regularUserService.getRegularByEmail(SECOND_USER_EMAIL)).thenReturn(SECOND_USER);
    }

    private void mockOneRegularUser(String userEmail, RegularUser user) throws EntityNotFoundException {
        when(regularUserService.getRegularByEmail(userEmail)).thenReturn(user);
    }

    private void mockRoute() {
        when(routeService.createRoute(anyList(), anyDouble(), anyDouble(), anyList())).thenReturn(ROUTE);
    }

    @ParameterizedTest
    @DisplayName("T7-Should throw exception when time for ride reservation is invalid")
    @MethodSource("getInvalidDatesForReservation")
    public void createDrivingNotificationDTO_throwInvalidChosenTimeForReservationException(LocalDateTime chosenDateTime) throws EntityNotFoundException {
        mockRegularUserService();
        mockVehicleTypeInfoService();
        mockVehicleSeats(2, true);
        mockRoute();
        List<String> passengers = List.of(SECOND_USER_EMAIL);

        assertThrows(InvalidChosenTimeForReservationException.class, () -> drivingNotificationService.createDrivingRequest(
                createRouteRequest(), FIRST_USER_EMAIL, PRICE, passengers, DURATION,
                false, false, SUV, chosenDateTime, true));
    }

    @Test
    @DisplayName("T8-Should send notification about ride request to linked passengers")
    public void createDrivingNotificationDTO_sendWebSocketForDrivingNotification() throws EntityNotFoundException, InvalidChosenTimeForReservationException, ExcessiveNumOfPassengersException, NotFoundException, PassengerNotHaveTokensException {
        mockRegularUserService();
        mockVehicleTypeInfoService();
        mockVehicleSeats(2, true);
        mockRoute();
        List<String> passengers = List.of(SECOND_USER_EMAIL);
        Map<RegularUser, Integer> receiversReviewed = getRegularUserIntegerMap();
        DrivingNotification drivingNotification = mockAndGetDrivingNotification(FIRST_USER, receiversReviewed, LocalDateTime.now(), false);
        doNothing().when(webSocketService).sendPassengerAgreementNotification(any(DrivingNotificationWebSocketDTO.class), anyMap());

        DrivingNotificationDTO drivingNotificationDTO = drivingNotificationService.createDrivingRequest(createRouteRequest(), FIRST_USER_EMAIL, PRICE, passengers, DURATION,
                false, false, SUV, null, false);

        Assertions.assertEquals(drivingNotification.getRoute(), drivingNotificationDTO.getRoute());
        Assertions.assertNotNull(drivingNotificationDTO.getStarted());
        verify(webSocketService).sendPassengerAgreementNotification(any(DrivingNotificationWebSocketDTO.class), anyMap());
        verify(webSocketService, never()).sendDeletedDrivingNotification(any(DrivingNotificationWebSocketDTO.class), anyMap());
    }

    private DrivingNotification mockAndGetDrivingNotification(
            RegularUser regularUser,
            Map<RegularUser, Integer> receiversReviewed,
            LocalDateTime startTime,
            boolean isReservation
    ) {
        DrivingNotification drivingNotification = new DrivingNotification(
                ROUTE, PRICE, regularUser, startTime, DURATION, false, false, VEHICLE_TYPE_INFO_SUV,
                receiversReviewed, isReservation);
        drivingNotification.setId(EXIST_OBJECT_ID);
        when(drivingNotificationRepository.save(any(DrivingNotification.class))).thenReturn(drivingNotification);

        return drivingNotification;
    }

    private Map<RegularUser, Integer> getRegularUserIntegerMap() {
        Map<RegularUser, Integer> receiversReviewed = new HashMap<>();
        receiversReviewed.put(FIRST_USER, NOT_REVIEWED_LINKED_REQUEST);
        receiversReviewed.put(SECOND_USER, NOT_REVIEWED_LINKED_REQUEST);

        return receiversReviewed;
    }

    @Test
    @DisplayName("T9-Should send notification about failed ride request, driver is not found and ride is not reservation")
    public void createDrivingNotificationDTO_driverNotFound() throws EntityNotFoundException, InvalidChosenTimeForReservationException, ExcessiveNumOfPassengersException, NotFoundException, PassengerNotHaveTokensException {
        mockOneRegularUser(FIRST_USER_EMAIL, FIRST_USER);
        mockVehicleTypeInfoService();
        mockVehicleSeats(1, true);
        mockRoute();
        DrivingNotification drivingNotification = mockAndGetDrivingNotification(FIRST_USER, new HashMap<>(), LocalDateTime.now(), false);

        when(driverService.getDriverForDriving(drivingNotification)).thenReturn(null);
        doNothing().when(webSocketService).sendDrivingStatus(anyString(), anyString(), anyMap());
        doNothing().when(drivingNotificationRepository).deleteById(drivingNotification.getId());

        DrivingNotificationDTO drivingNotificationDTO = drivingNotificationService.createDrivingRequest(createRouteRequest(), FIRST_USER_EMAIL, PRICE, new ArrayList<>(), DURATION,
                false, false, SUV, null, false);

        Assertions.assertEquals(drivingNotification.getRoute(), drivingNotificationDTO.getRoute());
        Assertions.assertNotNull(drivingNotificationDTO.getStarted());

        verify(webSocketService).sendDrivingStatus(anyString(), anyString(), anyMap());
        verify(drivingService, never()).create(
                anyDouble(), any(LocalDateTime.class), any(Route.class), any(DrivingStatus.class), anyLong(), anySet(), anyDouble());
        verify(drivingNotificationRepository).deleteById(drivingNotification.getId());
    }

    @Test
    @DisplayName("T10-Should send notification about successfully creating reservation ride")
    public void createDrivingNotificationDTO_successReservation() throws EntityNotFoundException, InvalidChosenTimeForReservationException, ExcessiveNumOfPassengersException, NotFoundException, PassengerNotHaveTokensException {
        LocalDateTime chosenTimeForReservation = getValidTimeForReservation();
        mockOneRegularUser(FIRST_USER_EMAIL, FIRST_USER);
        mockVehicleTypeInfoService();
        mockVehicleSeats(1, true);
        mockRoute();
        DrivingNotification drivingNotification = mockAndGetDrivingNotification(FIRST_USER, new HashMap<>(), chosenTimeForReservation, true);
        doNothing().when(webSocketService).sendSuccessfulCreateReservation(anySet());

        DrivingNotificationDTO drivingNotificationDTO = drivingNotificationService.createDrivingRequest(createRouteRequest(), FIRST_USER_EMAIL, PRICE, new ArrayList<>(), DURATION,
                false, false, SUV, chosenTimeForReservation, true);

        Assertions.assertEquals(drivingNotification.getRoute(), drivingNotificationDTO.getRoute());
        Assertions.assertNotNull(drivingNotificationDTO.getStarted());
        verify(webSocketService).sendSuccessfulCreateReservation(anySet());
        verify(driverService, never()).getDriverForDriving(any(DrivingNotification.class));
    }

    @Test
    @DisplayName("T11-Should throw entity not found for getting tokens for user, driver is found and ride is not reservation")
    public void createDrivingNotificationDTO_tokensForUserNotFound() throws EntityNotFoundException {
        mockOneRegularUser(THIRD_USER_EMAIL, USER_WITHOUT_TOKEN_BANK);
        mockVehicleTypeInfoService();
        mockVehicleSeats( 1, true);
        mockRoute();
        DrivingNotification drivingNotification = mockAndGetDrivingNotification(USER_WITHOUT_TOKEN_BANK, new HashMap<>(), LocalDateTime.now(), false);
        when(driverService.getDriverForDriving(drivingNotification)).thenReturn(EXIST_DRIVER);
        mockDriving(drivingNotification, USER_WITHOUT_TOKEN_BANK, EXIST_OBJECT_ID, false);
        when(tokenBankService.getTokensForUser(THIRD_USER_ID)).thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () ->
                drivingNotificationService.createDrivingRequest(
                        createRouteRequest(), THIRD_USER_EMAIL, PRICE, new ArrayList<>(), DURATION,
                        false, false, SUV, null, false
                )
        );

        verify(driverService, never()).calculateMinutesToStartDriving(any(Driver.class), any(Driving.class));
        verify(drivingService, never()).removeDriver(anyLong());
    }

    private Driving mockDriving(DrivingNotification drivingNotification, RegularUser regularUser, Long drivingId, boolean moreUsers) throws EntityNotFoundException {
        Set<RegularUser> regularUsers = getRegularUsers(regularUser);
        if (moreUsers) {
            regularUsers.add(SECOND_USER);
            regularUsers.add(THIRD_USER);
        }
        Driving driving = new Driving(DURATION, drivingNotification.getStarted(), null, drivingNotification.getRoute(),
                DrivingStatus.PAYING, EXIST_DRIVER, drivingNotification.getPrice());
        driving.setId(drivingId);
        when(drivingService.create(drivingNotification.getRoute().getTimeInMin(), drivingNotification.getStarted(),
                drivingNotification.getRoute(), DrivingStatus.PAYING, DRIVER_ID, regularUsers, drivingNotification.getPrice()))
                .thenReturn(driving);

        return driving;
    }

    @ParameterizedTest
    @DisplayName("T12-Should successfully create ride, driver is found, paying is successful and ride is not reservation")
    @ValueSource(doubles = {2, 3})
    public void createDrivingNotificationDTO_UserHasEnoughTokens(double numOfTokens) throws EntityNotFoundException, InvalidChosenTimeForReservationException, ExcessiveNumOfPassengersException, NotFoundException, PassengerNotHaveTokensException {
        mockOneRegularUser(FIRST_USER_EMAIL, FIRST_USER);
        mockVehicleTypeInfoService();
        mockVehicleSeats( 1, true);
        mockRoute();
        DrivingNotification drivingNotification = mockAndGetDrivingNotification(FIRST_USER, new HashMap<>(), LocalDateTime.now(), false);
        when(driverService.getDriverForDriving(drivingNotification)).thenReturn(EXIST_DRIVER);
        Driving driving = mockDriving(drivingNotification, FIRST_USER, EXIST_OBJECT_ID, false);

        when(tokenBankService.getTokensForUser(FIRST_USER.getId())).thenReturn(numOfTokens);
        mockUpdatingNumOfTokensForUsers();

        driving.setDrivingStatus(DrivingStatus.ACCEPTED);
        mockSuccessfullyCreatedDriving(driving, drivingNotification);

        drivingNotificationService.createDrivingRequest(createRouteRequest(), FIRST_USER_EMAIL, PRICE, new ArrayList<>(), DURATION,
                false, false, SUV, null, false);

        verify(drivingNotificationRepository).deleteById(anyLong());
        verify(drivingService).save(drivingArgumentCaptor.capture());
        Assertions.assertEquals(DrivingStatus.ACCEPTED, drivingArgumentCaptor.getValue().getDrivingStatus());
        Assertions.assertEquals(EXIST_DRIVER.getEmail(), drivingArgumentCaptor.getValue().getDriver().getEmail());
    }

    private void mockSuccessfullyCreatedDriving(Driving driving, DrivingNotification drivingNotification) {
        when(driverService.calculateMinutesToStartDriving(EXIST_DRIVER, driving)).thenReturn(TIME_IN_MIN);
        when(drivingService.save(driving)).thenReturn(driving);
        handleNotificationsWhenSuccessfulCreatedDriving(drivingNotification);
    }

    private void mockUpdatingNumOfTokensForUsers() throws EntityNotFoundException {
        when(tokenBankService.updateNumOfTokensForUsers(anyMap())).thenReturn(true);
    }

    @Test
    @DisplayName("T13-Should throw passenger not have tokens exception, driver is found and ride is not reservation")
    public void createDrivingNotificationDTO_UserNotHaveEnoughTokens() throws EntityNotFoundException {
        mockOneRegularUser(SECOND_USER_EMAIL, SECOND_USER);
        mockVehicleTypeInfoService();
        mockVehicleSeats(1, true);
        mockRoute();
        DrivingNotification drivingNotification = mockAndGetDrivingNotification(SECOND_USER, new HashMap<>(), LocalDateTime.now(), false);
        when(driverService.getDriverForDriving(drivingNotification)).thenReturn(EXIST_DRIVER);
        Driving driving = mockDriving(drivingNotification, SECOND_USER, EXIST_OBJECT_ID, false);

        when(tokenBankService.getTokensForUser(SECOND_USER_ID)).thenReturn(1d);
        when(drivingService.removeDriver(EXIST_OBJECT_ID)).thenReturn(getDrivingWithoutDriver(driving));
        doNothing().when(drivingNotificationRepository).deleteById(EXIST_OBJECT_ID);
        doNothing().when(webSocketService).sendDrivingStatus(anyString(), anyString(), anyMap());

        assertThrows(PassengerNotHaveTokensException.class, () ->
                drivingNotificationService.createDrivingRequest(
                        createRouteRequest(), SECOND_USER_EMAIL, PRICE, new ArrayList<>(), DURATION,
                        false, false, SUV, null, false
                )
        );

        verify(tokenBankService, never()).updateNumOfTokens(anyLong(), anyDouble());
        verify(drivingService).removeDriver(EXIST_OBJECT_ID);
    }

    @Test
    @DisplayName("T14-Should not create driving, when driver is not found and ride is reservation")
    public void findDriverNow_notCreateDrivingWhenDriverIsNotFoundAndRideIsReservation() throws PassengerNotHaveTokensException, EntityNotFoundException {
        DrivingNotification drivingNotification = getDrivingNotification(FIRST_USER, LocalDateTime.now(), true, PRICE, false);
        when(driverService.getDriverForDriving(drivingNotification)).thenReturn(null);
        Driving driving = drivingNotificationService.createDrivingIfFoundDriverAndSuccessfullyPaid(drivingNotification);

        Assertions.assertNull(driving);

        verify(driverService).getDriverForDriving(drivingNotification);
        verify(webSocketService, never()).sendDrivingStatus(anyString(), anyString(), anyMap());
        verify(drivingService, never()).create(anyDouble(), any(LocalDateTime.class), any(Route.class), any(DrivingStatus.class), anyLong(), anySet(), anyDouble());
    }

    @Test
    @DisplayName("T15-Should send message about not creating driving, when driver is not found and ride is not reservation")
    public void findDriverNow_notSendMessageAboutNotCreatingDrivingWhenDriverIsNotFound() throws PassengerNotHaveTokensException, EntityNotFoundException {

        DrivingNotification drivingNotification = getDrivingNotification(FIRST_USER, LocalDateTime.now(), false, PRICE, false);
        Map<RegularUser, Integer> receiversReviewed = drivingNotification.getReceiversReviewed();
        receiversReviewed.put(drivingNotification.getSender(), 0);
        when(driverService.getDriverForDriving(drivingNotification)).thenReturn(null);
        doNothing().when(webSocketService).sendDrivingStatus(DRIVER_NOT_FOUND_PATH, DRIVER_NOT_FOUND_MESSAGE, receiversReviewed);
        Driving driving = drivingNotificationService.createDrivingIfFoundDriverAndSuccessfullyPaid(drivingNotification);

        Assertions.assertNull(driving);

        verify(driverService).getDriverForDriving(drivingNotification);
        verify(webSocketService).sendDrivingStatus(DRIVER_NOT_FOUND_PATH, DRIVER_NOT_FOUND_MESSAGE, receiversReviewed);
        verify(drivingService, never()).create(anyDouble(), any(LocalDateTime.class), any(Route.class), any(DrivingStatus.class), anyLong(), anySet(), anyDouble());
    }

    @Test
    @DisplayName("T16-Should throw PassengerNotHaveTokensException for only one user without enough tokens for paying")
    public void findDriverNow_userDontDaveEnoughTokens() throws EntityNotFoundException {
        DrivingNotification drivingNotification = getDrivingNotification(FIRST_USER, LocalDateTime.now(), true, PRICE, false);
        drivingNotification.setId(60L);
        Driving driving = mockDriving(drivingNotification, FIRST_USER, 5L, false);
        when(driverService.getDriverForDriving(drivingNotification)).thenReturn(EXIST_DRIVER);
        when(tokenBankService.getTokensForUser(FIRST_USER.getId())).thenReturn(1d);
        when(drivingService.removeDriver(driving.getId())).thenReturn(getDrivingWithoutDriver(driving));
        doNothing().when(webSocketService).sendDrivingStatus(anyString(), anyString(), anyMap());
        doNothing().when(drivingNotificationRepository).deleteById(drivingNotification.getId());

        assertThrows(PassengerNotHaveTokensException.class, () ->
                drivingNotificationService.createDrivingIfFoundDriverAndSuccessfullyPaid(drivingNotification)
        );

        verify(driverService, never()).calculateMinutesToStartDriving(any(Driver.class), any(Driving.class));
        verify(drivingService).removeDriver(driving.getId());
    }

    @Test
    @DisplayName("T17-Should throw entity not found exception for user without token bank")
    public void findDriverNow_userDontHaveTokenBank() throws EntityNotFoundException {
        DrivingNotification drivingNotification = getDrivingNotification(USER_WITHOUT_TOKEN_BANK, LocalDateTime.now(), true, PRICE, false);
        drivingNotification.setId(60L);
        mockDriving(drivingNotification, USER_WITHOUT_TOKEN_BANK, 5L, false);
        when(driverService.getDriverForDriving(drivingNotification)).thenReturn(EXIST_DRIVER);
        when(tokenBankService.getTokensForUser(USER_WITHOUT_TOKEN_BANK.getId())).thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () ->
                drivingNotificationService.createDrivingIfFoundDriverAndSuccessfullyPaid(drivingNotification)
        );

        verify(driverService, never()).calculateMinutesToStartDriving(any(Driver.class), any(Driving.class));
        verify(drivingService, never()).removeDriver(anyLong());
    }

    private void handleNotificationsWhenSuccessfulCreatedDriving(DrivingNotification drivingNotification) {
        doNothing().when(webSocketService).sendSuccessfulDriving(any(DrivingStatusNotificationDTO.class), anyMap());
        doNothing().when(webSocketService).sendNewDrivingNotification(any(DrivingStatusNotificationDTO.class), anyString());
        if (drivingNotification.isReservation()) {
            doNothing().when(drivingNotificationRepository).deleteById(drivingNotification.getId());
        }
    }

    @Test
    @DisplayName("T18-Should create accept driving with found driver for only one user with enough tokens for paying")
    public void findDriverNow_userHasEnoughToken() throws EntityNotFoundException, PassengerNotHaveTokensException {
        DrivingNotification drivingNotification = getDrivingNotification(FIRST_USER, LocalDateTime.now(), true, PRICE, false);
        drivingNotification.setId(60L);
        Driving driving = mockDriving(drivingNotification, FIRST_USER, 5L, false);
        when(driverService.getDriverForDriving(drivingNotification)).thenReturn(EXIST_DRIVER);

        when(tokenBankService.getTokensForUser(FIRST_USER.getId())).thenReturn(2d);
        mockUpdatingNumOfTokensForUsers();

        when(driverService.calculateMinutesToStartDriving(EXIST_DRIVER, driving)).thenReturn(TIME_IN_MIN);
        driving.setDrivingStatus(DrivingStatus.ACCEPTED);
        driving.setStarted(drivingNotification.getStarted());
        when(drivingService.save(any(Driving.class))).thenReturn(driving);

        handleNotificationsWhenSuccessfulCreatedDriving(drivingNotification);

        Driving createdDriving = drivingNotificationService.createDrivingIfFoundDriverAndSuccessfullyPaid(drivingNotification);

        verify(drivingService).save(drivingArgumentCaptor.capture());
        Assertions.assertEquals(DrivingStatus.ACCEPTED, drivingArgumentCaptor.getValue().getDrivingStatus());
        Assertions.assertEquals(driving.getStarted(), drivingArgumentCaptor.getValue().getStarted());
        Assertions.assertEquals(driving.getId(), drivingArgumentCaptor.getValue().getId());
        Assertions.assertNotNull(createdDriving);

        verify(webSocketService).sendSuccessfulDriving(any(DrivingStatusNotificationDTO.class), anyMap());
        verify(webSocketService).sendNewDrivingNotification(any(DrivingStatusNotificationDTO.class), anyString());
        verify(drivingNotificationRepository).deleteById(drivingNotification.getId());
        verify(drivingService, never()).removeDriver(anyLong());
    }

    @Test
    @DisplayName("T19-Should create accept driving with found driver for only one user with enough tokens for paying, not reservation")
    public void findDriverNow_userHasEnoughTokenNotReservation() throws EntityNotFoundException, PassengerNotHaveTokensException {
        DrivingNotification drivingNotification = getDrivingNotification(FIRST_USER, LocalDateTime.now(), false, PRICE, false);
        drivingNotification.setId(60L);
        Driving driving = mockDriving(drivingNotification, FIRST_USER, 5L, false);
        when(driverService.getDriverForDriving(drivingNotification)).thenReturn(EXIST_DRIVER);

        when(tokenBankService.getTokensForUser(FIRST_USER.getId())).thenReturn(2d);
        mockUpdatingNumOfTokensForUsers();

        when(driverService.calculateMinutesToStartDriving(EXIST_DRIVER, driving)).thenReturn(TIME_IN_MIN);
        when(drivingService.save(any(Driving.class))).thenReturn(driving);
        handleNotificationsWhenSuccessfulCreatedDriving(drivingNotification);

        Driving createdDriving = drivingNotificationService.createDrivingIfFoundDriverAndSuccessfullyPaid(drivingNotification);

        verify(drivingService).save(drivingArgumentCaptor.capture());
        Assertions.assertEquals(DrivingStatus.ACCEPTED, drivingArgumentCaptor.getValue().getDrivingStatus());
        Assertions.assertNotEquals(drivingNotification.getStarted(), createdDriving.getStarted());
        Assertions.assertEquals(driving.getId(), drivingArgumentCaptor.getValue().getId());
        Assertions.assertNotNull(createdDriving);

        verify(webSocketService).sendSuccessfulDriving(any(DrivingStatusNotificationDTO.class), anyMap());
        verify(webSocketService).sendNewDrivingNotification(any(DrivingStatusNotificationDTO.class), anyString());
        verify(drivingNotificationRepository, never()).deleteById(drivingNotification.getId());
        verify(drivingService, never()).removeDriver(anyLong());
    }

    @Test
    @DisplayName("T20-Should throw PassengerNotHaveTokensException for many passengers without enough tokens for paying in sum")
    public void findDriverNow_usersDontHaveEnoughTokensInSum() throws EntityNotFoundException {
        DrivingNotification drivingNotification = getDrivingNotification(FIRST_USER, LocalDateTime.now(), true, 10, true);
        drivingNotification.setId(60L);
        Driving driving = mockDriving(drivingNotification, FIRST_USER, 5L, true);
        when(driverService.getDriverForDriving(drivingNotification)).thenReturn(EXIST_DRIVER);

        when(tokenBankService.getTokensForUser(FIRST_USER.getId())).thenReturn(1d);
        when(tokenBankService.getTokensForUser(SECOND_USER.getId())).thenReturn(6d);
        when(tokenBankService.getTokensForUser(THIRD_USER.getId())).thenReturn(2d);
        when(drivingService.removeDriver(driving.getId())).thenReturn(getDrivingWithoutDriver(driving));
        doNothing().when(webSocketService).sendDrivingStatus(anyString(), anyString(), anyMap());
        doNothing().when(drivingNotificationRepository).deleteById(drivingNotification.getId());

        assertThrows(PassengerNotHaveTokensException.class, () ->
                drivingNotificationService.createDrivingIfFoundDriverAndSuccessfullyPaid(drivingNotification)
        );

        verify(driverService, never()).calculateMinutesToStartDriving(any(Driver.class), any(Driving.class));
        verify(drivingService).removeDriver(driving.getId());
    }

    @ParameterizedTest
    @DisplayName("T21-Should create accept driving with found driver for many passengers with enough tokens for paying in sum")
    @CsvSource(value = {"1,6,3", "2,4,4", "0,0,10", "12,0,4"})
    public void findDriverNow_usersHaveEnoughTokenInSum(
            double numOfTokensForFirstUser,
            double numOfTokensForSecondUser,
            double numOfTokensForThirdUser
    ) throws EntityNotFoundException, PassengerNotHaveTokensException {
        DrivingNotification drivingNotification = getDrivingNotification(FIRST_USER, LocalDateTime.now(), true, 10, true);
        drivingNotification.setId(60L);
        Driving driving = mockDriving(drivingNotification, FIRST_USER, 5L, true);
        when(driverService.getDriverForDriving(drivingNotification)).thenReturn(EXIST_DRIVER);

        mockPayment(numOfTokensForFirstUser, numOfTokensForSecondUser, numOfTokensForThirdUser);
        when(driverService.calculateMinutesToStartDriving(EXIST_DRIVER, driving)).thenReturn(TIME_IN_MIN);
        when(drivingService.save(any(Driving.class))).thenReturn(driving);
        handleNotificationsWhenSuccessfulCreatedDriving(drivingNotification);

        Driving createdDriving = drivingNotificationService.createDrivingIfFoundDriverAndSuccessfullyPaid(drivingNotification);

        verify(drivingService).save(drivingArgumentCaptor.capture());
        Assertions.assertEquals(DrivingStatus.ACCEPTED, drivingArgumentCaptor.getValue().getDrivingStatus());
        Assertions.assertEquals(drivingNotification.getStarted(), drivingArgumentCaptor.getValue().getStarted());
        Assertions.assertEquals(driving.getId(), drivingArgumentCaptor.getValue().getId());
        Assertions.assertNotNull(createdDriving);

        verify(webSocketService).sendSuccessfulDriving(any(DrivingStatusNotificationDTO.class), anyMap());
        verify(webSocketService).sendNewDrivingNotification(any(DrivingStatusNotificationDTO.class), anyString());
        verify(drivingNotificationRepository).deleteById(drivingNotification.getId());
        verify(drivingService, never()).removeDriver(anyLong());
    }

    private void mockPayment(double numOfTokensForFirstUser, double numOfTokensForSecondUser, double numOfTokensForThirdUser) throws EntityNotFoundException {
        when(tokenBankService.getTokensForUser(FIRST_USER.getId())).thenReturn(numOfTokensForFirstUser);
        when(tokenBankService.getTokensForUser(SECOND_USER.getId())).thenReturn(numOfTokensForSecondUser);
        when(tokenBankService.getTokensForUser(THIRD_USER.getId())).thenReturn(numOfTokensForThirdUser);
        mockUpdatingNumOfTokensForUsers();
    }

    @Test
    @DisplayName("T22-Should successfully delete driving notification")
    public void delete_shouldSuccessfullyDeleteDrivingNotification() {
        DrivingNotification drivingNotification = getDrivingNotification(FIRST_USER, LocalDateTime.now(), true, PRICE, true);
        drivingNotification.setId(60L);
        doNothing().when(drivingNotificationRepository).deleteById(drivingNotification.getId());

        drivingNotificationService.delete(drivingNotification);

        verify(drivingNotificationRepository).deleteById(drivingNotification.getId());
    }

    @Test
    @DisplayName("T23-Should call find driver method and delete driving notification, not reservation")
    public void shouldFindDriver_callFindDriverAndDeleteDrivingNotification_NotReservation() throws PassengerNotHaveTokensException, EntityNotFoundException {
        DrivingNotification drivingNotification = getDrivingNotification(FIRST_USER, LocalDateTime.now(), false, PRICE, false);
        drivingNotification.setId(60L);

        Driving driving = mockDriving(drivingNotification, FIRST_USER, 5L, false);
        when(driverService.getDriverForDriving(drivingNotification)).thenReturn(EXIST_DRIVER);

        when(tokenBankService.getTokensForUser(FIRST_USER.getId())).thenReturn(2d);
        mockUpdatingNumOfTokensForUsers();

        when(driverService.calculateMinutesToStartDriving(EXIST_DRIVER, driving)).thenReturn(TIME_IN_MIN);
        when(drivingService.save(any(Driving.class))).thenReturn(driving);

        handleNotificationsWhenSuccessfulCreatedDriving(drivingNotification);
        drivingNotificationService.shouldFindDriver(drivingNotification);

        verify(driverService).getDriverForDriving(any(DrivingNotification.class));
        verify(drivingNotificationRepository).deleteById(anyLong());
        verify(webSocketService, never()).sendSuccessfulCreateReservation(anySet());
    }

    @Test
    @DisplayName("T24-Should send message about success reservation ride")
    public void shouldFindDriver_sendMessageAboutSuccessReservationRide() throws PassengerNotHaveTokensException, EntityNotFoundException {
        DrivingNotification drivingNotification = getDrivingNotification(FIRST_USER, LocalDateTime.now(), true, PRICE, false);
        drivingNotification.setId(60L);
        doNothing().when(webSocketService).sendSuccessfulCreateReservation(anySet());
        when(drivingNotificationRepository.save(drivingNotification)).thenReturn(drivingNotification);

        drivingNotificationService.shouldFindDriver(drivingNotification);

        verify(webSocketService).sendSuccessfulCreateReservation(anySet());
        verify(drivingNotificationRepository).save(any(DrivingNotification.class));
    }

    @Test
    @DisplayName("T25-Should not find driver")
    public void shouldFindDriver_driverNotFound() throws EntityNotFoundException, PassengerNotHaveTokensException {
        DrivingNotification drivingNotification = getDrivingNotification(USER_WITHOUT_TOKEN_BANK, LocalDateTime.now(), false, PRICE, false);
        drivingNotification.setId(60L);
        when(driverService.getDriverForDriving(drivingNotification)).thenReturn(null);
        doNothing().when(webSocketService).sendDrivingStatus(anyString(), anyString(), anyMap());
        doNothing().when(drivingNotificationRepository).deleteById(drivingNotification.getId());

        drivingNotificationService.shouldFindDriver(drivingNotification);

        verify(drivingNotificationRepository).deleteById(anyLong());
        verify(driverService, never()).calculateMinutesToStartDriving(any(Driver.class), any(Driving.class));
    }

    @Test
    @DisplayName("T26-Should throw EntityNotFoundException, token bank is not existed for user")
    public void shouldFindDriver_throwEntityNotFoundExceptionForTokenBank() throws EntityNotFoundException {
        DrivingNotification drivingNotification = getDrivingNotification(USER_WITHOUT_TOKEN_BANK, LocalDateTime.now(), false, PRICE, false);
        drivingNotification.setId(60L);
        mockDriving(drivingNotification, USER_WITHOUT_TOKEN_BANK, 5L, false);
        when(driverService.getDriverForDriving(drivingNotification)).thenReturn(EXIST_DRIVER);

        when(tokenBankService.getTokensForUser(USER_WITHOUT_TOKEN_BANK.getId())).thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () ->
                drivingNotificationService.shouldFindDriver(drivingNotification)
        );

        verify(driverService, never()).calculateMinutesToStartDriving(any(Driver.class), any(Driving.class));
        verify(drivingNotificationRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("T27-Should throw PassengerNotHaveTokensException, user don't have enough tokens to pay ride")
    public void shouldFindDriver_throwPassengerNotHaveTokensException() throws EntityNotFoundException {
        DrivingNotification drivingNotification = getDrivingNotification(FIRST_USER, LocalDateTime.now(), false, PRICE, false);
        drivingNotification.setId(60L);
        Driving driving = mockDriving(drivingNotification, FIRST_USER, 5L, false);
        when(driverService.getDriverForDriving(drivingNotification)).thenReturn(EXIST_DRIVER);

        when(tokenBankService.getTokensForUser(FIRST_USER.getId())).thenReturn(1d);
        when(drivingService.removeDriver(driving.getId())).thenReturn(getDrivingWithoutDriver(driving));
        doNothing().when(webSocketService).sendDrivingStatus(anyString(), anyString(), anyMap());
        doNothing().when(drivingNotificationRepository).deleteById(drivingNotification.getId());

        assertThrows(PassengerNotHaveTokensException.class, () ->
                drivingNotificationService.shouldFindDriver(drivingNotification)
        );

        verify(drivingNotificationRepository).deleteById(anyLong());
        verify(webSocketService).sendDrivingStatus(anyString(), anyString(), anyMap());
    }

    @Test
    @DisplayName("T28-Should success create driving, driver found and users paid")
    public void shouldFindDriver_SuccessfulFindDriverAndCreateDriving() throws EntityNotFoundException, PassengerNotHaveTokensException {
        DrivingNotification drivingNotification = getDrivingNotification(FIRST_USER, LocalDateTime.now(), false, PRICE, false);
        drivingNotification.setId(60L);
        Driving driving = mockDriving(drivingNotification, FIRST_USER, 5L, false);
        when(driverService.getDriverForDriving(drivingNotification)).thenReturn(EXIST_DRIVER);

        when(tokenBankService.getTokensForUser(FIRST_USER.getId())).thenReturn(3d);
        mockUpdatingNumOfTokensForUsers();

        when(driverService.calculateMinutesToStartDriving(EXIST_DRIVER, driving)).thenReturn(TIME_IN_MIN);
        when(drivingService.save(any(Driving.class))).thenReturn(driving);
        handleNotificationsWhenSuccessfulCreatedDriving(drivingNotification);

        drivingNotificationService.shouldFindDriver(drivingNotification);

        verify(drivingService).save(drivingArgumentCaptor.capture());
        Assertions.assertEquals(DrivingStatus.ACCEPTED, drivingArgumentCaptor.getValue().getDrivingStatus());
        Assertions.assertEquals(driving.getId(), drivingArgumentCaptor.getValue().getId());

        verify(webSocketService).sendSuccessfulDriving(any(DrivingStatusNotificationDTO.class), anyMap());
        verify(webSocketService).sendNewDrivingNotification(any(DrivingStatusNotificationDTO.class), anyString());
        verify(drivingNotificationRepository).deleteById(drivingNotification.getId());
        verify(drivingService, never()).removeDriver(anyLong());
    }

    @Test
    @DisplayName("T29-Should not call any method when calling should not find driver")
    public void shouldFindDriver_shouldNotCallAnyMethod() throws PassengerNotHaveTokensException, EntityNotFoundException {
        DrivingNotification drivingNotification = getDrivingNotification(FIRST_USER, LocalDateTime.now(), true, PRICE, false);
        drivingNotification.setId(60L);
        drivingNotification.setReservation(true);
        drivingNotification.setNotified(true);

        drivingNotificationService.shouldFindDriver(drivingNotification);

        verify(webSocketService, never()).sendSuccessfulCreateReservation(anySet());
        verify(driverService, never()).getDriverForDriving(any(DrivingNotification.class));
    }

    @NotNull
    private Set<RegularUser> getRegularUsers(RegularUser firstUser) {
        Set<RegularUser> regularUsers = new HashSet<>();
        regularUsers.add(firstUser);
        return regularUsers;
    }

    private DrivingDTO getDrivingWithoutDriver(Driving driving) {
        driving.setDriver(null);

        return new DrivingDTO(driving);
    }


    private List<Arguments> getInvalidDatesForReservation() {
        return Arrays.asList(Arguments.arguments(LocalDateTime.now().plusHours(6)),
                Arguments.arguments(LocalDateTime.now().plusHours(5).plusMinutes(1)),
                Arguments.arguments(LocalDateTime.now().plusMinutes(29)),
                Arguments.arguments(LocalDateTime.now().plusMinutes(25)));
    }

    private LocalDateTime getValidTimeForReservation() {
        return LocalDateTime.now().plusHours(1);
    }

    private RouteRequest createRouteRequest() {
        LocationRequest startLocation = new LocationRequest(
                "Novi Sad", "Bulevar Oslobodjenja",
                "55", "21000",
                19.833950, 45.258300
        );

        LocationRequest endLocation = new LocationRequest(
                "Novi Sad", "Bulevar Cara Lazara",
                "5", "21000",
                19.848760, 45.246710
        );
        DrivingLocationIndexRequest startLocationIndex = new DrivingLocationIndexRequest(startLocation, 1);
        DrivingLocationIndexRequest endLocationIndex = new DrivingLocationIndexRequest(endLocation, 2);
        List<DrivingLocationIndexRequest> locations = new ArrayList<>();
        locations.add(startLocationIndex);
        locations.add(endLocationIndex);
        List<Integer> routeIndexList = new ArrayList<>();
        routeIndexList.add(0);
        routeIndexList.add(0);

        return new RouteRequest(5, 2000, locations, routeIndexList);
    }
}
