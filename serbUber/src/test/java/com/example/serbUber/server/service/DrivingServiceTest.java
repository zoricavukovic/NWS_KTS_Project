package com.example.serbUber.server.service;

import com.example.serbUber.dto.DrivingDTO;
import com.example.serbUber.dto.SimpleDrivingInfoDTO;
import com.example.serbUber.dto.VehicleCurrentLocationDTO;
import com.example.serbUber.exception.DriverAlreadyHasStartedDrivingException;
import com.example.serbUber.exception.DrivingShouldNotStartYetException;
import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.model.*;
import com.example.serbUber.model.user.Driver;
import com.example.serbUber.model.user.RegularUser;
import com.example.serbUber.repository.DrivingRepository;
import com.example.serbUber.service.DrivingService;
import com.example.serbUber.service.DrivingStatusNotificationService;
import com.example.serbUber.service.RouteService;
import com.example.serbUber.service.WebSocketService;
import com.example.serbUber.service.user.UserService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;
import java.util.*;

import static com.example.serbUber.model.DrivingStatus.*;
import static com.example.serbUber.server.service.helper.DrivingConstants.*;
import static com.example.serbUber.server.service.helper.Constants.*;
import static com.example.serbUber.server.service.helper.DriverConstants.*;
import static com.example.serbUber.server.service.helper.DriverConstants.EXIST_DRIVER;
import static com.example.serbUber.server.service.helper.DriverConstants.EXIST_DRIVER_EMAIL;
import static com.example.serbUber.server.service.helper.DrivingConstants.*;
import static com.example.serbUber.server.service.helper.LocationHelper.FIRST_LOCATION;
import static com.example.serbUber.server.service.helper.RegularUserConstants.FIRST_USER;
import static com.example.serbUber.server.service.helper.UserConstants.*;
import static java.lang.Math.abs;
import static com.example.serbUber.server.service.helper.RegularUserConstants.FIRST_USER_ID;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.DisplayName.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DrivingServiceTest {

    @Mock
    private DrivingRepository drivingRepository;

    @Mock
    private UserService userService;

    @Mock
    private WebSocketService webSocketService;

    @Mock
    private DrivingStatusNotificationService drivingStatusNotificationService;

    @Mock
    private RouteService routeService;

    @Captor
    private ArgumentCaptor<Driving> drivingArgumentCaptor;

    @Captor
    private ArgumentCaptor<DrivingDTO> drivingDTOArgumentCaptor;

    @InjectMocks
    private DrivingService drivingService;

    @BeforeAll
    public void setup() {
        MockitoAnnotations.openMocks(this);

        when(drivingRepository.getDrivingById(NOT_EXIST_OBJECT_ID))
                .thenReturn(Optional.empty());
    }

    @Test
    @DisplayName("T1-Should throw driving not found exception")
    public void shouldThrowDrivingNotFoundExceptionRejectDriving() {

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> drivingService.rejectDriving(NOT_EXIST_OBJECT_ID, DRIVING_REJECTION_REASON));

        verify(drivingRepository, times(0)).save(any(Driving.class));
        verify(drivingStatusNotificationService, times(0)).create(anyString(), any(DrivingStatus.class), any(Driving.class));
        verify(webSocketService, times(0)).sendRejectDriving(anyString(), anyString(), anySet());
    }

    @Test
    @DisplayName("T2-Should success reject driving")
    public void shouldSuccessfullyRejectDriving() throws EntityNotFoundException {

        Driving driving = new Driving(EXIST_OBJECT_ID, DURATION, STARTED, END, new Route(),
                DrivingStatus.PENDING, EXIST_DRIVER, PRICE
        );
        DrivingStatusNotification drivingStatusNotification = new DrivingStatusNotification(
                DRIVING_REJECTION_REASON, DrivingStatus.REJECTED, driving
        );
        when(drivingRepository.getDrivingById(EXIST_OBJECT_ID))
                .thenReturn(Optional.of(driving));

        when(drivingRepository.save(driving)).thenReturn(driving);

        driving.setDrivingStatus(DrivingStatus.REJECTED);
        driving.getDriver().getVehicle().setCurrentLocationIndex(-1);
        driving.getDriver().getVehicle().setActiveRoute(null);

        when(drivingStatusNotificationService.create(DRIVING_REJECTION_REASON, DrivingStatus.REJECTED, driving))
                .thenReturn(drivingStatusNotification);

        doNothing().when(webSocketService).sendRejectDriving(EXIST_DRIVER_EMAIL, DRIVING_REJECTION_REASON, driving.getUsers());
        DrivingDTO drivingDTO = drivingService.rejectDriving(EXIST_OBJECT_ID, DRIVING_REJECTION_REASON);

        Assertions.assertEquals(DrivingStatus.REJECTED, drivingDTO.getDrivingStatus());
        Assertions.assertEquals(EXIST_OBJECT_ID, drivingDTO.getId());

        verify(drivingStatusNotificationService, times(1)).create(
                DRIVING_REJECTION_REASON,
                DrivingStatus.REJECTED,
                driving
        );
        verify(webSocketService, times(1)).sendRejectDriving(
                EXIST_DRIVER_EMAIL,
                DRIVING_REJECTION_REASON,
                driving.getUsers()
        );
    }

    @Test
    @DisplayName("T3-Should throw driving not found exception when calls getDrivingById")
    public void shouldThrowDrivingNotFoundExceptionWhenCallsGetDrivingByiD() {

        Assertions.assertThrows(EntityNotFoundException.class,
            () -> drivingService.getDriving(NOT_EXIST_OBJECT_ID));
    }

    @Test
    @DisplayName("T4-Should successfully return driving when calls getDrivingById")
    public void shouldSuccessfullyReturnDrivingWhenCallsGetDrivingByiD() throws EntityNotFoundException {

        Driving driving = new Driving(EXIST_OBJECT_ID, DURATION, STARTED, END, new Route(),
            DrivingStatus.PENDING, EXIST_DRIVER, PRICE
        );
        when(drivingRepository.getDrivingById(EXIST_OBJECT_ID))
            .thenReturn(Optional.of(driving));

        Driving drivingResponse = drivingService.getDriving(EXIST_OBJECT_ID);
        Assertions.assertEquals(driving.getId(), drivingResponse.getId());
    }

    @Test
    @DisplayName("T5-Should return empty list of now and future drivings")
    public void shouldReturnEmptyListOfNowAndFutureDrivingsForDriverId() {

        List<Driving> drivings = new LinkedList<>();

        when(drivingRepository.getAllNowAndFutureDrivings(EXIST_DRIVER.getId()))
            .thenReturn(drivings);

        List<DrivingDTO> drivingDTOs = drivingService.getAllNowAndFutureDrivings(EXIST_DRIVER.getId());

        Assertions.assertEquals(0, drivingDTOs.size());
        verify(drivingRepository).getAllNowAndFutureDrivings(EXIST_DRIVER.getId());
    }

    @Test
    @DisplayName("T6-Should return list of now and future drivings with many drivings")
    public void shouldReturnListOfNowAndFutureDrivingsForDriverId() {

        List<Driving> drivings = new LinkedList<>();
        Driving activeDriving = new Driving(DURATION, LocalDateTime.now().minusMinutes(1), null, ROUTE,
            ACCEPTED, EXIST_DRIVER, PRICE);
        activeDriving.setActive(true);
        DrivingDTO activeDrivingDTO = new DrivingDTO(activeDriving);

        Driving futureDriving = new Driving(DURATION, LocalDateTime.now().plusMinutes(5), null, ROUTE,
            ACCEPTED, EXIST_DRIVER, PRICE);
        DrivingDTO futureDrivingDTO = new DrivingDTO(futureDriving);

        drivings.add(activeDriving);
        drivings.add(futureDriving);
        when(drivingRepository.getAllNowAndFutureDrivings(EXIST_DRIVER.getId()))
            .thenReturn(drivings);

        List<DrivingDTO> drivingDTOs = drivingService.getAllNowAndFutureDrivings(EXIST_DRIVER.getId());

        Assertions.assertEquals(2, drivingDTOs.size());
        Assertions.assertEquals(activeDrivingDTO.getStarted(), drivingDTOs.get(0).getStarted());
        Assertions.assertEquals(futureDrivingDTO.getStarted(), drivingDTOs.get(1).getStarted());
    }

    @Test
    @DisplayName("T7-Starting driving should fail, driving not found exception")
    public void shouldThrowDrivingNotFoundExceptionWhenStartingDriving() {
        Assertions.assertThrows(EntityNotFoundException.class,
                () -> drivingService.startDriving(NOT_EXIST_OBJECT_ID));

        verify(drivingRepository, times(0)).save(any(Driving.class));
        verify(webSocketService, times(0)).startDrivingNotification(any(SimpleDrivingInfoDTO.class), anySet());
        verify(webSocketService, times(0)).sendVehicleCurrentLocation(any(VehicleCurrentLocationDTO.class));
    }

    @Test
    @DisplayName("T8-should throw driver already has started driving exception when starting driving")
    public void shouldThrowDriverAlreadyHasStartedDrivingException() {

        Driving driving = new Driving(EXIST_OBJECT_ID, DURATION, STARTED, END, new Route(),
                DrivingStatus.PENDING, EXIST_DRIVER, PRICE
        );

        when(drivingRepository.getDrivingById(EXIST_OBJECT_ID))
                .thenReturn(Optional.of(driving));

        when(drivingRepository.getActiveDrivingForDriver(driving.getDriver().getId()))
                .thenReturn(Optional.of(driving));

        Assertions.assertThrows(DriverAlreadyHasStartedDrivingException.class,
                () -> drivingService.startDriving(EXIST_OBJECT_ID));
    }

    @ParameterizedTest
    @DisplayName("T9-should throw driving should not start yet exception when starting driving")
    @MethodSource(value = "getInvalidStartingTime")
    public void shouldThrowDrivingShouldNotStartYetExceptionWhenStartingDriving(LocalDateTime starting) {

        Driving driving = new Driving(EXIST_OBJECT_ID, DURATION, starting, END, new Route(),
                DrivingStatus.PENDING, EXIST_DRIVER, PRICE
        );

        when(drivingRepository.getDrivingById(EXIST_OBJECT_ID))
                .thenReturn(Optional.of(driving));

        when(drivingRepository.getActiveDrivingForDriver(driving.getDriver().getId()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(DrivingShouldNotStartYetException.class,
                () -> drivingService.startDriving(EXIST_OBJECT_ID));
    }

    @ParameterizedTest
    @DisplayName("T10-should start driving")
    @MethodSource(value = "getValidStartTimeForStartingDriving")
    public void shouldStartDriving(LocalDateTime starting)
        throws DriverAlreadyHasStartedDrivingException, EntityNotFoundException, DrivingShouldNotStartYetException {

        Driving driving = new Driving(EXIST_OBJECT_ID, DURATION, starting, END, ROUTE,
                DrivingStatus.PENDING, EXIST_DRIVER, PRICE
        );

        when(drivingRepository.getDrivingById(EXIST_OBJECT_ID))
                .thenReturn(Optional.of(driving));

        when(drivingRepository.getActiveDrivingForDriver(driving.getDriver().getId()))
                .thenReturn(Optional.empty());

        Driving startedDriving = createStartedDriving(driving);

        when(drivingRepository.save(driving)).thenReturn(startedDriving);

        doNothing().when(webSocketService).startDrivingNotification(any(SimpleDrivingInfoDTO.class), any());
        doNothing().when(webSocketService).sendVehicleCurrentLocation(any(VehicleCurrentLocationDTO.class));

        DrivingDTO drivingResponse = drivingService.startDriving(EXIST_OBJECT_ID);

        verify(drivingRepository, times(1)).save(drivingArgumentCaptor.capture());
        Assertions.assertEquals(driving.getId(), drivingResponse.getId());
        Assertions.assertEquals(startedDriving.isActive(), drivingResponse.isActive());
        Assertions.assertEquals(ACCEPTED, drivingResponse.getDrivingStatus());
        Assertions.assertEquals(EXPECTED_CURRENT_LOCATION_INDEX_FOR_STARTED_DRIVING, drivingArgumentCaptor.getValue().getDriver().getVehicle().getCurrentLocationIndex());
        assertTrue(drivingArgumentCaptor.getValue().getDriver().isDrive());
        Assertions.assertEquals(EXPECTED_CROSSED_WAYPOINTS, drivingArgumentCaptor.getValue().getDriver().getVehicle().getCrossedWaypoints());

        verify(webSocketService, times(1)).startDrivingNotification(any(SimpleDrivingInfoDTO.class), any());
        verify(webSocketService, times(1)).sendVehicleCurrentLocation(any(VehicleCurrentLocationDTO.class));
    }

    @Test
    @DisplayName("T11-Should not found future drivings for driver")
    public void shouldNotFoundFutureDrivingsForDriver() {

        List<Driving> drivings = new LinkedList<>();

        when(drivingRepository.driverHasFutureDriving(EXIST_DRIVER.getId()))
            .thenReturn(drivings);

        Driving driving = drivingService.driverHasFutureDriving(EXIST_DRIVER.getId());

        assertNull(driving);
        verify(drivingRepository).driverHasFutureDriving(EXIST_DRIVER.getId());
    }

    @Test
    @DisplayName("T12-Should found future driving for driver")
    public void shouldFoundFutureDrivingForDriver() {

        List<Driving> drivings = new LinkedList<>();
        Driving futureDriving1 = new Driving(DURATION, LocalDateTime.now().plusMinutes(5), null, ROUTE,
            ACCEPTED, EXIST_DRIVER, PRICE);


        Driving futureDriving2 = new Driving(DURATION, LocalDateTime.now().plusMinutes(10), null, ROUTE,
            ACCEPTED, EXIST_DRIVER, PRICE);

        drivings.add(futureDriving1);
        drivings.add(futureDriving2);

        when(drivingRepository.driverHasFutureDriving(EXIST_DRIVER.getId()))
            .thenReturn(drivings);

        Driving driving = drivingService.driverHasFutureDriving(EXIST_DRIVER.getId());

        Assertions.assertNotNull(driving);
        Assertions.assertEquals(futureDriving1.getDrivingStatus(), driving.getDrivingStatus());
        Assertions.assertEquals(futureDriving1.getStarted(), driving.getStarted());
    }

    @Test
    @DisplayName("T13-Should not found active driving for user")
    public void shouldNotFoundActiveDrivingForUser() {

        List<Driving> drivings = new LinkedList<>();

        when(drivingRepository.getActiveDrivingForUser(anyLong(), any(LocalDateTime.class)))
            .thenReturn(drivings);

        SimpleDrivingInfoDTO activeDriving = drivingService.checkUserHasActiveDriving(FIRST_USER_ID);

        assertNull(activeDriving);
    }

    @Test
    @DisplayName("T14-Should found active driving for user")
    public void shouldFoundActiveDrivingForUser() {

        List<Driving> drivings = new LinkedList<>();
        Driving activeDriving = new Driving(DURATION, LocalDateTime.now().minusMinutes(1), null, ROUTE,
            ACCEPTED, EXIST_DRIVER, PRICE);
        activeDriving.setActive(true);
        SimpleDrivingInfoDTO activeDrivingDTO = new SimpleDrivingInfoDTO(activeDriving);

        drivings.add(activeDriving);

        when(drivingRepository.getActiveDrivingForUser(anyLong(), any(LocalDateTime.class)))
            .thenReturn(drivings);

        SimpleDrivingInfoDTO activeSimpleDrivingDTO = drivingService.checkUserHasActiveDriving(FIRST_USER_ID);

        assertTrue(activeSimpleDrivingDTO.isActive());
        Assertions.assertEquals(activeDrivingDTO.getCost(), activeSimpleDrivingDTO.getCost());
    }

    @Test
    @DisplayName("T15-SimpleDrivingInfoDTO creation, should set minutes from route when driving is active")
    public void shouldSetMinutesFromRouteWhenDrivingIsActive() {

        Driving driving = new Driving(EXIST_OBJECT_ID, DURATION, STARTED, END, ROUTE,
                DrivingStatus.PENDING, EXIST_DRIVER, PRICE
        );
        driving.setActive(true);

        SimpleDrivingInfoDTO simpleDrivingInfoDTO = new SimpleDrivingInfoDTO(driving);

        Assertions.assertEquals(TIME_IN_MIN, simpleDrivingInfoDTO.getMinutes());
    }

    @Test
    @DisplayName("T16-SimpleDrivingInfoDTO creation, should set minutes from route when end time is null")
    public void shouldSetMinutesFromRouteWhenDEndTimeIsNull() {

        Driving driving = new Driving(EXIST_OBJECT_ID, DURATION, STARTED, null, ROUTE,
                DrivingStatus.PENDING, EXIST_DRIVER, PRICE
        );
        driving.setActive(true);

        SimpleDrivingInfoDTO simpleDrivingInfoDTO = new SimpleDrivingInfoDTO(driving);

        Assertions.assertEquals(TIME_IN_MIN, simpleDrivingInfoDTO.getMinutes());
    }

    @Test
    @DisplayName("T17-SimpleDrivingInfoDTO creation, should set minutes like difference between end and started time")
    public void shouldSetMinutesLikeDifferenceBetweenEndAndStartTime() {

        Driving driving = new Driving(EXIST_OBJECT_ID, DURATION, STARTED, END, ROUTE,
                DrivingStatus.PENDING, EXIST_DRIVER, PRICE
        );
        SimpleDrivingInfoDTO simpleDrivingInfoDTO = new SimpleDrivingInfoDTO(driving);

        Assertions.assertEquals(DURATION, abs(simpleDrivingInfoDTO.getMinutes()));
    }

    @Test
    @DisplayName("T18-Finishing driving failed, driving not found exception")
    public void shouldThrowEntityNotFoundExceptionWhenDrivingFinishing() {

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> drivingService.finishDriving(NOT_EXIST_OBJECT_ID));

        verify(drivingRepository, times(0)).save(any(Driving.class));
        verify(webSocketService, times(0)).finishDrivingNotification(any(SimpleDrivingInfoDTO.class), anySet());
    }

    @Test
    @DisplayName("T19-Finishing driving successfully")
    public void shouldFinishDrivingSuccessfully() throws EntityNotFoundException {

        Driving driving = new Driving(EXIST_OBJECT_ID, DURATION, STARTED, END, ROUTE,
                DrivingStatus.PENDING, EXIST_DRIVER, PRICE
        );

        when(drivingRepository.getDrivingById(EXIST_OBJECT_ID))
                .thenReturn(Optional.of(driving));

        Driving finishedDriving = createFinishedDriving(driving);

        when(drivingRepository.save(driving)).thenReturn(finishedDriving);

        doNothing().when(webSocketService).finishDrivingNotification(any(SimpleDrivingInfoDTO.class), any());

        DrivingDTO drivingResponse = this.drivingService.finishDriving(EXIST_OBJECT_ID);

        verify(drivingRepository, times(1)).save(drivingArgumentCaptor.capture());
        verify(webSocketService, times(1)).finishDrivingNotification(any(SimpleDrivingInfoDTO.class), any());
        Assertions.assertEquals(driving.getId(), drivingResponse.getId());
        assertFalse(drivingResponse.isActive());
        Assertions.assertEquals(FINISHED, drivingResponse.getDrivingStatus());
        Assertions.assertEquals(EXPECTED_CURRENT_LOCATION_INDEX_FOR_FINISHED_DRIVING, drivingArgumentCaptor.getValue().getDriver().getVehicle().getCurrentLocationIndex());
        assertNull(drivingArgumentCaptor.getValue().getDriver().getVehicle().getActiveRoute());
        Assertions.assertEquals(EXPECTED_CROSSED_WAYPOINTS, drivingArgumentCaptor.getValue().getDriver().getVehicle().getCrossedWaypoints());
    }

    @ParameterizedTest
    @DisplayName("T20-Should reject outdated drivings")
    @CsvSource(value = {"4,5", "1,3"})
    public void shouldRejectOutdatedDrivings(int minutesForFirst, int minutesForSecond) {
        List<Driving> drivings = createDrivingList(minutesForFirst, minutesForSecond);

        when(drivingService.getAcceptedNotActiveDrivings()).thenReturn(drivings);
        doNothing().when(webSocketService).sendRejectedOutdatedDriving(anySet(), anyString(), anyLong());
        when(drivingRepository.save(drivings.get(0))).thenReturn(drivings.get(0));
        when(drivingRepository.save(drivings.get(1))).thenReturn(drivings.get(1));

        drivingService.rejectOutdatedDrivings();

        verify(drivingRepository, times(2)).save(drivingArgumentCaptor.capture());
        verify(webSocketService, times(2)).sendRejectedOutdatedDriving(anySet(), anyString(), anyLong());
        List<Driving> changedDrivings = drivingArgumentCaptor.getAllValues();

        changedDrivings.forEach(driving -> {
            Assertions.assertEquals(REJECTED,driving.getDrivingStatus());
        });
    }

    @ParameterizedTest
    @DisplayName("T21-Should skip all, there is not outdated outdated drivings")
    @CsvSource(value = {"6,7"})
    public void shouldNotRejectDrivings(int minutesForFirst, int minutesForSecond) {
        List<Driving> drivings = createDrivingList(minutesForFirst, minutesForSecond);

        when(drivingService.getAcceptedNotActiveDrivings()).thenReturn(drivings);

        drivingService.rejectOutdatedDrivings();

        verify(drivingRepository, times(0)).save(drivingArgumentCaptor.capture());
        verify(webSocketService, times(0)).sendRejectedOutdatedDriving(anySet(), anyString(), anyLong());
        List<Driving> changedDrivings = drivingArgumentCaptor.getAllValues();

        Assertions.assertEquals(0, changedDrivings.size());
    }

    @Test
    @DisplayName("T15 - Should return true, passenger has active driving")
    public void isPassengersAlreadyHaveRide_returnTrue_passengerHaveActiveDriving() throws EntityNotFoundException {
        when(userService.getUserByEmail(USER_EMAIL_1)).thenReturn(createUser(USER_ID_1, USER_EMAIL_1));
        Driving activeDriving =
                createActiveDriving(5, createDriver(DRIVER_ID_1, DRIVER_EMAIL_1, VEHICLE_1, FIRST_LOCATION));
        List<Driving> drivings = new ArrayList<>();
        drivings.add(activeDriving);
        when(drivingRepository
                .getActiveDrivingForUser(anyLong(), any(LocalDateTime.class)))
                .thenReturn(drivings);
        List<String> passengers = new ArrayList<>();
        passengers.add(USER_EMAIL_1);
        assertTrue(drivingService.checkIfPassengersAreBusy(passengers, LocalDateTime.now()));
    }

    @Test
    @DisplayName("T16 - Should return false, passenger don't have active driving")
    public void isPassengersAlreadyHaveRide_returnFalse_notHaveActiveDriving() throws EntityNotFoundException {
        when(userService.getUserByEmail(USER_EMAIL_1)).thenReturn(createUser(USER_ID_1, USER_EMAIL_1));
        List<Driving> drivings = new ArrayList<>();
        when(drivingRepository
                .getActiveDrivingForUser(anyLong(), any(LocalDateTime.class)))
                .thenReturn(drivings);
        List<String> passengers = new ArrayList<>();
        passengers.add(USER_EMAIL_1);
        assertFalse(drivingService.checkIfPassengersAreBusy(passengers, LocalDateTime.now()));
    }

    @Test
    @DisplayName("T17 - Should return false, passenger have future driving before/after driving")
    public void isPassengersAlreadyHaveRide_returnFalse_haveFutureDriving() throws EntityNotFoundException {
        when(userService.getUserByEmail(USER_EMAIL_1)).thenReturn(createUser(USER_ID_1, USER_EMAIL_1));
        List<Driving> drivings = new ArrayList<>();
        Driving driving = createFutureDriving(5, createDriver(DRIVER_ID_1, DRIVER_EMAIL_1, VEHICLE_1, FIRST_LOCATION));
        drivings.add(driving);
        when(drivingRepository
                .getActiveDrivingForUser(anyLong(), any(LocalDateTime.class)))
                .thenReturn(drivings);
        List<String> passengers = new ArrayList<>();
        passengers.add(USER_EMAIL_1);
        assertFalse(drivingService.checkIfPassengersAreBusy(passengers, LocalDateTime.now().plusMinutes(40)));
    }

    @Test
    @DisplayName("T18 - Should remove driver, return null for driver")
    public void removeDriver_returnDrivingWithoutDriver() throws EntityNotFoundException {
        Driving driving = createFutureDriving(10,
                createDriver(DRIVER_ID_1, DRIVER_EMAIL_1, VEHICLE_1, FIRST_LOCATION));
        when(drivingRepository.getDrivingById(DRIVING_ID_1)).thenReturn(Optional.of(driving));

        assertNull(drivingService.removeDriver(DRIVING_ID_1).getDriverId());
    }

    @Test
    @DisplayName("T19 - Should throw exception, driving is not found")
    public void removeDriver_throwException() {
        when(drivingRepository.getDrivingById(DRIVING_ID_1)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> drivingService.removeDriver(DRIVING_ID_1));
    }

    @Test
    @DisplayName("T20 - Should return saved driving")
    public void save_returnSavedDriving() {
        Driving driving = createFutureDriving(10,
                createDriver(DRIVER_ID_1, DRIVER_EMAIL_1, VEHICLE_1, FIRST_LOCATION));
        when(drivingRepository.save(any(Driving.class))).thenReturn(driving);
        assertEquals(driving.getDriver().getId(), drivingService.save(driving).getDriverId());
    }

    @Test
    @DisplayName("T21 - Should return created driving")
    public void create_returnCreatedDriving() throws EntityNotFoundException {
        Driver driver = createDriver(DRIVER_ID_1, DRIVER_EMAIL_1, VEHICLE_1, FIRST_LOCATION);
        Driving driving = createFutureDriving(10, driver);
        Set<RegularUser> users = new HashSet<>();
        users.add(FIRST_USER);

        when(drivingRepository.getAllDrivingsForUserEmail(anyString())).thenReturn(new ArrayList<>());
        when(userService.getDriverById(DRIVER_ID_1)).thenReturn(driver);
        when(drivingRepository.save(any(Driving.class))).thenReturn(driving);

        assertEquals(driving.getDriver().getId(),
                drivingService.create(
                        DURATION,
                        driving.getStarted(),
                        driving.getRoute(),
                        ACCEPTED,
                        DRIVER_ID_1,
                        users,
                        PRICE).getDriver().getId());
    }

    @Test
    @DisplayName("T22 - Should throw exception, driving is not found")
    public void create_throwEntityNotFoundException() throws EntityNotFoundException {
        Set<RegularUser> users = new HashSet<>();
        users.add(FIRST_USER);
        when(userService.getDriverById(anyLong())).thenThrow(EntityNotFoundException.class);
        assertThrows(EntityNotFoundException.class, () -> drivingService.create(
                DURATION,
                LocalDateTime.now(),
                ROUTE,
                ACCEPTED,
                DRIVER_ID_1,
                users,
                PRICE));
        verify(drivingRepository, times(0)).getAllDrivingsForUserEmail(anyString());
        verify(drivingRepository, times(0)).save(any(Driving.class));
    }


    private List<Arguments> getInvalidStartingTime(){

        return Arrays.asList(arguments(LocalDateTime.now().plusMinutes(6).plusSeconds(1)),
            arguments(LocalDateTime.now().plusMinutes(7)));
    }

    List<Arguments> getValidStartTimeForStartingDriving(){

        return Arrays.asList(arguments(LocalDateTime.now().plusMinutes(5)),
            arguments(LocalDateTime.now().plusMinutes(4)));
    }

    @Test
    @DisplayName("T8 - Should return drivings for user")
    public void getAllDrivingsForUserEmail_returnDrivingsForUser(){
        List<Driving> drivings = new ArrayList<>();
        Driving driving_1 = createFutureDriving(5, DRIVER_1);
        Driving driving_2 = createActiveDriving(5, DRIVER_2);
        drivings.add(driving_1);
        drivings.add(driving_2);
        when(drivingRepository.getAllDrivingsForUserEmail(USER_EMAIL_1)).thenReturn(drivings);

        assertEquals(2, drivingService.getAllDrivingsForUserEmail(USER_EMAIL_1).size());
    }


}
