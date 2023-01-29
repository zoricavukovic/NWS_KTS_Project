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
import com.example.serbUber.model.token.PayingInfo;
import com.example.serbUber.model.token.TokenBank;
import com.example.serbUber.model.token.TokenTransaction;
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
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static com.example.serbUber.server.helper.Constants.*;
import static com.example.serbUber.server.helper.DriverConstants.DRIVER_ID;
import static com.example.serbUber.server.helper.DriverConstants.EXIST_DRIVER;
import static com.example.serbUber.server.helper.VehicleTypeInfoConstants.*;
import static com.example.serbUber.server.helper.RegularUserConstants.*;
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

        Map<RegularUser, Integer> receiversReviewed = new HashMap<>();
        receiversReviewed.put(FIRST_USER, NOT_REVIEWED_LINKED_REQUEST);
        receiversReviewed.put(SECOND_USER, NOT_REVIEWED_LINKED_REQUEST);
        DrivingNotification drivingNotification = new DrivingNotification(
            new Route(), PRICE, new RegularUser(), STARTED, DURATION, true, true, new VehicleTypeInfo(),
            receiversReviewed, true
        );

        when(drivingNotificationRepository.findById(EXIST_OBJECT_ID))
            .thenReturn(Optional.of(drivingNotification));

        when(drivingNotificationRepository.save(any(DrivingNotification.class))).thenReturn(drivingNotification);

        DrivingNotificationDTO drivingNotificationDTO = drivingNotificationService.updateStatus(
            EXIST_OBJECT_ID,
            SECOND_USER_EMAIL,
            true
        );
        verify(drivingNotificationRepository, times(1)).save(postArgumentCaptor.capture());
        Assertions.assertEquals(drivingNotificationDTO.getPrice(), drivingNotification.getPrice());
        Assertions.assertEquals(postArgumentCaptor.getValue().getReceiversReviewed().get(FIRST_USER), NOT_REVIEWED_LINKED_REQUEST);
        Assertions.assertEquals(postArgumentCaptor.getValue().getReceiversReviewed().get(SECOND_USER), ACCEPT_DRIVING);
    }

    @Test
    @DisplayName("T3-Should throw not found exception when user is not found")
    public void createDrivingNotificationDTO_throwEntityNotFoundExceptionForRegularUser() throws EntityNotFoundException {
        when(regularUserService.getRegularByEmail(NOT_EXIST_USER_EMAIL)).thenThrow(EntityNotFoundException.class);
        assertThrows(EntityNotFoundException.class, () -> drivingNotificationService.createDrivingNotificationDTO(
                new RouteRequest(), NOT_EXIST_USER_EMAIL, PRICE, new ArrayList<>(), DURATION,
                false, false, "SUV", null, false));
        verify(vehicleTypeInfoService, times(0)).isCorrectNumberOfSeats(any(VehicleTypeInfo.class), anyInt());
    }

    @Test
    @DisplayName("T4-Should throw exception when passenger is not found")
    public void createDrivingNotificationDTO_throwEntityNotFoundExceptionForPassenger() throws EntityNotFoundException {
        when(regularUserService.getRegularByEmail(FIRST_USER_EMAIL)).thenReturn(FIRST_USER);
        when(regularUserService.getRegularByEmail(NOT_EXIST_USER_EMAIL)).thenThrow(EntityNotFoundException.class);
        List<String> users = new ArrayList<>();
        users.add(NOT_EXIST_USER_EMAIL);
        assertThrows(EntityNotFoundException.class, () -> drivingNotificationService.createDrivingNotificationDTO(
                new RouteRequest(), FIRST_USER_EMAIL, PRICE, users, DURATION,
                false, false, SUV, null, false));
        verify(routeService, times(0)).createRoute(anyList(), anyDouble(), anyDouble(), anyList());
    }

    @Test
    @DisplayName("T5-Should throw exception when vehicle type info is not found")
    public void createDrivingNotificationDTO_throwEntityNotFoundExceptionForVehicleTypeInfo() throws EntityNotFoundException {
        when(vehicleTypeInfoService.get(VehicleType.SUV)).thenThrow(EntityNotFoundException.class);
        List<String> users = new ArrayList<>();
        users.add(SECOND_USER_EMAIL);
        assertThrows(EntityNotFoundException.class, () -> drivingNotificationService.createDrivingNotificationDTO(
            new RouteRequest(), FIRST_USER_EMAIL, PRICE, users, DURATION,
            false, false, SUV, null, false));
        verify(vehicleTypeInfoService, times(0)).isCorrectNumberOfSeats(any(VehicleTypeInfo.class), anyInt());
    }

    @Test
    @DisplayName("T6-Should throw exception when number of passengers is greater than num of seats")
    public void createDrivingNotificationDTO_throwExcessiveNumOfPassengersException() throws EntityNotFoundException {
        when(regularUserService.getRegularByEmail(FIRST_USER_EMAIL)).thenReturn(FIRST_USER);
        when(regularUserService.getRegularByEmail(SECOND_USER_EMAIL)).thenReturn(SECOND_USER);
        when(vehicleTypeInfoService.get(VehicleType.SUV)).thenReturn(VEHICLE_TYPE_INFO_SUV);
        List<String> users = new ArrayList<>();
        users.add(SECOND_USER_EMAIL);
        users.add(SECOND_USER_EMAIL);
        when(vehicleTypeInfoService.isCorrectNumberOfSeats(VEHICLE_TYPE_INFO_SUV, 3))
                .thenReturn(false);
        assertThrows(ExcessiveNumOfPassengersException.class, () -> drivingNotificationService.createDrivingNotificationDTO(
                new RouteRequest(), FIRST_USER_EMAIL, PRICE, users, DURATION,
                false, false, SUV, null, false));
        verify(routeService, times(0)).createRoute(anyList(), anyDouble(), anyDouble(), anyList());
    }

    @ParameterizedTest
    @DisplayName("T7-Should throw exception when time for ride reservation is invalid")
    @MethodSource("getInvalidDatesForReservation")
    public void createDrivingNotificationDTO_throwInvalidChosenTimeForReservationException(LocalDateTime chosenDateTime) throws EntityNotFoundException {
        when(regularUserService.getRegularByEmail(FIRST_USER_EMAIL)).thenReturn(FIRST_USER);
        when(regularUserService.getRegularByEmail(SECOND_USER_EMAIL)).thenReturn(SECOND_USER);
        when(vehicleTypeInfoService.get(VehicleType.SUV)).thenReturn(VEHICLE_TYPE_INFO_SUV);
        when(vehicleTypeInfoService.isCorrectNumberOfSeats(VEHICLE_TYPE_INFO_SUV, 2)).thenReturn(true);
        when(routeService.createRoute(anyList(), anyDouble(), anyDouble(), anyList())).thenReturn(ROUTE);
        List<String> users = new ArrayList<>();
        users.add(SECOND_USER_EMAIL);

        assertThrows(InvalidChosenTimeForReservationException.class, () -> drivingNotificationService.createDrivingNotificationDTO(
                createRouteRequest(), FIRST_USER_EMAIL, PRICE, users, DURATION,
                false, false, SUV, chosenDateTime, true));
    }

    @Test
    @DisplayName("T8-Should send notification about ride request to linked passengers")
    public void createDrivingNotificationDTO_sendWebSocketForDrivingNotification() throws EntityNotFoundException, InvalidChosenTimeForReservationException, ExcessiveNumOfPassengersException, NotFoundException, PassengerNotHaveTokensException {
        when(regularUserService.getRegularByEmail(FIRST_USER_EMAIL)).thenReturn(FIRST_USER);
        when(regularUserService.getRegularByEmail(SECOND_USER_EMAIL)).thenReturn(SECOND_USER);
        when(vehicleTypeInfoService.get(VehicleType.SUV)).thenReturn(VEHICLE_TYPE_INFO_SUV);
        when(vehicleTypeInfoService.isCorrectNumberOfSeats(VEHICLE_TYPE_INFO_SUV, 2)).thenReturn(true);
        when(routeService.createRoute(anyList(), anyDouble(), anyDouble(), anyList())).thenReturn(ROUTE);
        List<String> users = new ArrayList<>();
        users.add(SECOND_USER_EMAIL);
        Map<RegularUser, Integer> receiversReviewed = new HashMap<>();
        receiversReviewed.put(FIRST_USER, NOT_REVIEWED_LINKED_REQUEST);
        receiversReviewed.put(SECOND_USER, NOT_REVIEWED_LINKED_REQUEST);
        DrivingNotification drivingNotification = new DrivingNotification(
                ROUTE, PRICE, FIRST_USER, LocalDateTime.now(), DURATION, false, false, VEHICLE_TYPE_INFO_SUV,
                 receiversReviewed,false);

        when(drivingNotificationRepository.save(any(DrivingNotification.class))).thenReturn(drivingNotification);
        doNothing().when(webSocketService).sendPassengerAgreementNotification(any(DrivingNotificationWebSocketDTO.class), anyMap());

        DrivingNotificationDTO drivingNotificationDTO = drivingNotificationService.createDrivingNotificationDTO(createRouteRequest(), FIRST_USER_EMAIL, PRICE, users, DURATION,
                false, false, SUV, null, false);

        Assertions.assertEquals(drivingNotification.getRoute(), drivingNotificationDTO.getRoute());
        Assertions.assertNotNull(drivingNotificationDTO.getStarted());
        verify(webSocketService, times(1)).sendPassengerAgreementNotification(any(DrivingNotificationWebSocketDTO.class), anyMap());
    }

    @Test
    @DisplayName("T9-Should send notification about failed ride request, driver is not found and ride is not reservation")
    public void createDrivingNotificationDTO_driverNotFound() throws EntityNotFoundException, InvalidChosenTimeForReservationException, ExcessiveNumOfPassengersException, NotFoundException, PassengerNotHaveTokensException {
        when(regularUserService.getRegularByEmail(FIRST_USER_EMAIL)).thenReturn(FIRST_USER);
        when(vehicleTypeInfoService.get(VehicleType.SUV)).thenReturn(VEHICLE_TYPE_INFO_SUV);
        when(vehicleTypeInfoService.isCorrectNumberOfSeats(VEHICLE_TYPE_INFO_SUV, 1)).thenReturn(true);
        when(routeService.createRoute(anyList(), anyDouble(), anyDouble(), anyList())).thenReturn(ROUTE);

        DrivingNotification drivingNotification = new DrivingNotification(
            ROUTE, PRICE, FIRST_USER, LocalDateTime.now(), DURATION, false, false, VEHICLE_TYPE_INFO_SUV,
            new HashMap<>(),false);
        when(drivingNotificationRepository.save(any(DrivingNotification.class))).thenReturn(drivingNotification);

        when(driverService.getDriverForDriving(drivingNotification)).thenReturn(null);
        doNothing().when(webSocketService).sendDrivingStatus(anyString(), anyString(), anyMap());
        doNothing().when(drivingNotificationRepository).deleteById(drivingNotification.getId());

        DrivingNotificationDTO drivingNotificationDTO = drivingNotificationService.createDrivingNotificationDTO(createRouteRequest(), FIRST_USER_EMAIL, PRICE, new ArrayList<>(), DURATION,
            false, false, SUV, null, false);

        Assertions.assertEquals(drivingNotification.getRoute(), drivingNotificationDTO.getRoute());
        Assertions.assertNotNull(drivingNotificationDTO.getStarted());
        verify(webSocketService, times(1)).sendDrivingStatus(anyString(), anyString(), anyMap());
        verify(drivingService, times(0)).create(
            anyDouble(), any(LocalDateTime.class), any(Route.class), any(DrivingStatus.class), anyLong(), anySet(), anyDouble());

        verify(drivingNotificationRepository, times(1)).deleteById(drivingNotification.getId());
    }

    @Test
    @DisplayName("T10-Should send notification about successfully creating reservation ride")
    public void createDrivingNotificationDTO_successReservation() throws EntityNotFoundException, InvalidChosenTimeForReservationException, ExcessiveNumOfPassengersException, NotFoundException, PassengerNotHaveTokensException {
        LocalDateTime chosenTimeForReservation = getValidTimeForReservation();
        when(regularUserService.getRegularByEmail(FIRST_USER_EMAIL)).thenReturn(FIRST_USER);
        when(vehicleTypeInfoService.get(VehicleType.SUV)).thenReturn(VEHICLE_TYPE_INFO_SUV);
        when(vehicleTypeInfoService.isCorrectNumberOfSeats(VEHICLE_TYPE_INFO_SUV, 1)).thenReturn(true);
        when(routeService.createRoute(anyList(), anyDouble(), anyDouble(), anyList())).thenReturn(ROUTE);

        DrivingNotification drivingNotification = new DrivingNotification(
            ROUTE, PRICE, FIRST_USER, chosenTimeForReservation, DURATION, false, false, VEHICLE_TYPE_INFO_SUV,
            new HashMap<>(),true);
        when(drivingNotificationRepository.save(any(DrivingNotification.class))).thenReturn(drivingNotification);
        doNothing().when(webSocketService).sendSuccessfulCreateReservation(anySet());

        DrivingNotificationDTO drivingNotificationDTO = drivingNotificationService.createDrivingNotificationDTO(createRouteRequest(), FIRST_USER_EMAIL, PRICE, new ArrayList<>(), DURATION,
            false, false, SUV, chosenTimeForReservation, true);

        Assertions.assertEquals(drivingNotification.getRoute(), drivingNotificationDTO.getRoute());
        Assertions.assertNotNull(drivingNotificationDTO.getStarted());
        verify(webSocketService, times(1)).sendSuccessfulCreateReservation(anySet());
        verify(driverService, times(0)).getDriverForDriving(any(DrivingNotification.class));
    }

    @Test
    @DisplayName("T11-Should throw entity not found for getting tokens for user, driver is found and ride is not reservation")
    public void createDrivingNotificationDTO_tokensForUserNotFound() throws EntityNotFoundException {
        when(regularUserService.getRegularByEmail(THIRD_USER_EMAIL)).thenReturn(USER_WITHOUT_TOKEN_BANK);
        when(vehicleTypeInfoService.get(VehicleType.SUV)).thenReturn(VEHICLE_TYPE_INFO_SUV);
        when(vehicleTypeInfoService.isCorrectNumberOfSeats(VEHICLE_TYPE_INFO_SUV, 1)).thenReturn(true);
        when(routeService.createRoute(anyList(), anyDouble(), anyDouble(), anyList())).thenReturn(ROUTE);

        DrivingNotification drivingNotification = new DrivingNotification(
            ROUTE, PRICE, USER_WITHOUT_TOKEN_BANK, LocalDateTime.now(), DURATION, false, false, VEHICLE_TYPE_INFO_SUV,
            new HashMap<>(),false);
        when(drivingNotificationRepository.save(any(DrivingNotification.class))).thenReturn(drivingNotification);

        when(driverService.getDriverForDriving(drivingNotification)).thenReturn(EXIST_DRIVER);

        Set<RegularUser> regularUsers = new HashSet<>();
        regularUsers.add(USER_WITHOUT_TOKEN_BANK);
        Driving driving = new Driving(DURATION, drivingNotification.getStarted(), null, drivingNotification.getRoute(),
            DrivingStatus.PAYING, EXIST_DRIVER, PRICE);
        when(drivingService.create(drivingNotification.getRoute().getTimeInMin(), drivingNotification.getStarted(),
            drivingNotification.getRoute(), DrivingStatus.PAYING, DRIVER_ID, regularUsers, drivingNotification.getPrice()))
            .thenReturn(driving);
        when(tokenBankService.getTokensForUser(THIRD_USER_ID)).thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () ->
                drivingNotificationService.createDrivingNotificationDTO(
                    createRouteRequest(), THIRD_USER_EMAIL, PRICE, new ArrayList<>(), DURATION,
                false, false, SUV, null, false
                )
        );

        verify(driverService, times(0)).calculateMinutesToStartDriving(any(Driver.class), any(Driving.class));
        verify(drivingService, times(0)).removeDriver(anyLong());
    }

    @ParameterizedTest
    @DisplayName("T12-Should successfully create ride, driver is found, paying is successful and ride is not reservation")
    @ValueSource(doubles = {2, 3})
    public void createDrivingNotificationDTO_UserHasEnoughTokens(double numOfTokens) throws EntityNotFoundException, InvalidChosenTimeForReservationException, ExcessiveNumOfPassengersException, NotFoundException, PassengerNotHaveTokensException {
        when(regularUserService.getRegularByEmail(FIRST_USER_EMAIL)).thenReturn(FIRST_USER);
        when(vehicleTypeInfoService.get(VehicleType.SUV)).thenReturn(VEHICLE_TYPE_INFO_SUV);
        when(vehicleTypeInfoService.isCorrectNumberOfSeats(VEHICLE_TYPE_INFO_SUV, 1)).thenReturn(true);
        when(routeService.createRoute(anyList(), anyDouble(), anyDouble(), anyList())).thenReturn(ROUTE);

        DrivingNotification drivingNotification = new DrivingNotification(
            ROUTE, PRICE, FIRST_USER, LocalDateTime.now(), DURATION, false, false, VEHICLE_TYPE_INFO_SUV,
            new HashMap<>(),false);
        when(drivingNotificationRepository.save(any(DrivingNotification.class))).thenReturn(drivingNotification);

        when(driverService.getDriverForDriving(drivingNotification)).thenReturn(EXIST_DRIVER);
        Set<RegularUser> regularUsers = new HashSet<>();
        regularUsers.add(FIRST_USER);
        Driving driving = new Driving(DURATION, drivingNotification.getStarted(), null, drivingNotification.getRoute(),
            DrivingStatus.PAYING, EXIST_DRIVER, PRICE);
        when(drivingService.create(drivingNotification.getRoute().getTimeInMin(), drivingNotification.getStarted(),
            drivingNotification.getRoute(), DrivingStatus.PAYING, DRIVER_ID, regularUsers, drivingNotification.getPrice()))
            .thenReturn(driving);
        when(tokenBankService.getTokensForUser(FIRST_USER.getId())).thenReturn(numOfTokens);
        
        //TODO: NAPRAVITI TOKEN UPDATES
//        doNothing().when(tokenBankService).updateNumOfTokens(FIRST_USER.getId(), numOfTokens-PRICE);

        driving.setDrivingStatus(DrivingStatus.ACCEPTED);

        when(driverService.calculateMinutesToStartDriving(EXIST_DRIVER, driving)).thenReturn(TIME_IN_MIN);
        when(drivingService.save(driving)).thenReturn(new DrivingDTO(driving));
        doNothing().when(webSocketService).sendSuccessfulDriving(any(DrivingStatusNotificationDTO.class), anyMap());
        doNothing().when(webSocketService).sendNewDrivingNotification(any(DrivingStatusNotificationDTO.class), anyString());

        drivingNotificationService.createDrivingNotificationDTO(createRouteRequest(), FIRST_USER_EMAIL, PRICE, new ArrayList<>(), DURATION,
            false, false, SUV, null, false);

        verify(drivingNotificationRepository, times(0)).deleteById(anyLong());
        verify(drivingService, times(1)).save(drivingArgumentCaptor.capture());
        Assertions.assertEquals(DrivingStatus.ACCEPTED, drivingArgumentCaptor.getValue().getDrivingStatus());
        Assertions.assertEquals(EXIST_DRIVER.getEmail(), drivingArgumentCaptor.getValue().getDriver().getEmail());
    }

    @Test
    @DisplayName("T13-Should throw passenger not have tokens exception, driver is found and ride is not reservation")
    public void createDrivingNotificationDTO_UserNotHaveEnoughTokens() throws EntityNotFoundException {
        when(regularUserService.getRegularByEmail(SECOND_USER_EMAIL)).thenReturn(SECOND_USER);
        when(vehicleTypeInfoService.get(VehicleType.SUV)).thenReturn(VEHICLE_TYPE_INFO_SUV);
        when(vehicleTypeInfoService.isCorrectNumberOfSeats(VEHICLE_TYPE_INFO_SUV, 1)).thenReturn(true);
        when(routeService.createRoute(anyList(), anyDouble(), anyDouble(), anyList())).thenReturn(ROUTE);

        DrivingNotification drivingNotification = new DrivingNotification(
            ROUTE, PRICE, SECOND_USER, LocalDateTime.now(), DURATION, false, false, VEHICLE_TYPE_INFO_SUV,
            new HashMap<>(),false);
        drivingNotification.setId(EXIST_OBJECT_ID);
        when(drivingNotificationRepository.save(any(DrivingNotification.class))).thenReturn(drivingNotification);

        when(driverService.getDriverForDriving(drivingNotification)).thenReturn(EXIST_DRIVER);
        Set<RegularUser> regularUsers = new HashSet<>();
        regularUsers.add(SECOND_USER);
        Driving driving = new Driving(DURATION, drivingNotification.getStarted(), null, drivingNotification.getRoute(),
            DrivingStatus.PAYING, EXIST_DRIVER, PRICE);
        driving.setId(EXIST_OBJECT_ID);
        when(drivingService.create(drivingNotification.getRoute().getTimeInMin(), drivingNotification.getStarted(),
            drivingNotification.getRoute(), DrivingStatus.PAYING, DRIVER_ID, regularUsers, drivingNotification.getPrice()))
            .thenReturn(driving);

        when(tokenBankService.getTokensForUser(SECOND_USER_ID)).thenReturn(1d);
        when(drivingService.removeDriver(EXIST_OBJECT_ID)).thenReturn(getDrivingWithoutDriver(driving));
        doNothing().when(drivingNotificationRepository).deleteById(EXIST_OBJECT_ID);
        doNothing().when(webSocketService).sendDrivingStatus(anyString(), anyString(), anyMap());

        assertThrows(PassengerNotHaveTokensException.class, () ->
            drivingNotificationService.createDrivingNotificationDTO(
                createRouteRequest(), SECOND_USER_EMAIL, PRICE, new ArrayList<>(), DURATION,
                false, false, SUV, null, false
            )
        );

        verify(tokenBankService, times(0)).updateNumOfTokens(anyLong(), anyDouble());
        verify(drivingService).removeDriver(EXIST_OBJECT_ID);
    }

    private DrivingDTO getDrivingWithoutDriver(Driving driving) {
        driving.setDriver(null);

        return new DrivingDTO(driving);
    }


    private List<Arguments> getInvalidDatesForReservation(){
        return Arrays.asList(Arguments.arguments(LocalDateTime.now().plusHours(6)),
            Arguments.arguments(LocalDateTime.now().plusHours(5).plusMinutes(1)),
            Arguments.arguments(LocalDateTime.now().plusMinutes(29)),
                Arguments.arguments(LocalDateTime.now().plusMinutes(25)));
    }

    private LocalDateTime getValidTimeForReservation(){
        return LocalDateTime.now().plusHours(1);
    }

    private RouteRequest createRouteRequest(){
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

        return new RouteRequest(5, 2000, locations, routeIndexList );
    }
}
