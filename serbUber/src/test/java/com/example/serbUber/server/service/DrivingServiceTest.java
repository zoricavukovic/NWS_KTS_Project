package com.example.serbUber.server.service;

import com.example.serbUber.dto.DrivingDTO;
import com.example.serbUber.dto.SimpleDrivingInfoDTO;
import com.example.serbUber.dto.VehicleCurrentLocationDTO;
import com.example.serbUber.exception.DriverAlreadyHasStartedDrivingException;
import com.example.serbUber.exception.DrivingShouldNotStartYetException;
import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.model.*;
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
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.example.serbUber.model.DrivingStatus.ACCEPTED;
import static com.example.serbUber.model.DrivingStatus.FINISHED;
import static com.example.serbUber.server.helper.Constants.*;
import static java.lang.Math.abs;
import static com.example.serbUber.server.helper.DriverConstants.EXIST_DRIVER;
import static com.example.serbUber.server.helper.DriverConstants.EXIST_DRIVER_EMAIL;
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
    @DisplayName("T5-Starting driving should fail, driving not found exception")
    public void shouldThrowDrivingNotFoundExceptionWhenStartingDriving() {
        Assertions.assertThrows(EntityNotFoundException.class,
                () -> drivingService.startDriving(NOT_EXIST_OBJECT_ID));

        verify(drivingRepository, times(0)).save(any(Driving.class));
        verify(webSocketService, times(0)).startDrivingNotification(any(SimpleDrivingInfoDTO.class), anySet());
        verify(webSocketService, times(0)).sendVehicleCurrentLocation(any(VehicleCurrentLocationDTO.class));
    }

    @Test
    @DisplayName("T6-should throw driver already has started driving exception when starting driving")
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
    @DisplayName("T7-should throw driving should not start yet exception when starting driving")
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

    List<Arguments> getInvalidStartingTime(){

        return Arrays.asList(arguments(LocalDateTime.now().plusMinutes(6).plusSeconds(1)),
                arguments(LocalDateTime.now().plusMinutes(7)));
    }

    @ParameterizedTest
    @DisplayName("T8-should start driving")
    @MethodSource(value = "getValidStartTimeForStartingDriving")
    public void shouldStartDriving(LocalDateTime starting)
            throws DriverAlreadyHasStartedDrivingException, DrivingShouldNotStartYetException, EntityNotFoundException
    {

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
        Assertions.assertTrue(drivingArgumentCaptor.getValue().getDriver().isDrive());
        Assertions.assertEquals(EXPECTED_CROSSED_WAYPOINTS, drivingArgumentCaptor.getValue().getDriver().getVehicle().getCrossedWaypoints());

        verify(webSocketService, times(1)).startDrivingNotification(any(SimpleDrivingInfoDTO.class), any());
        verify(webSocketService, times(1)).sendVehicleCurrentLocation(any(VehicleCurrentLocationDTO.class));
    }

    List<Arguments> getValidStartTimeForStartingDriving(){

        return Arrays.asList(arguments(LocalDateTime.now().plusMinutes(5)),
                arguments(LocalDateTime.now().plusMinutes(4)));
    }

    @Test
    @DisplayName("T9-SimpleDrivingInfoDTO creation, should set minutes from route when driving is active")
    public void shouldSetMinutesFromRouteWhenDrivingIsActive() {

        Driving driving = new Driving(EXIST_OBJECT_ID, DURATION, STARTED, END, ROUTE,
                DrivingStatus.PENDING, EXIST_DRIVER, PRICE
        );
        driving.setActive(true);

        SimpleDrivingInfoDTO simpleDrivingInfoDTO = new SimpleDrivingInfoDTO(driving);

        Assertions.assertEquals(TIME_IN_MIN, simpleDrivingInfoDTO.getMinutes());
    }

    @Test
    @DisplayName("T10-SimpleDrivingInfoDTO creation, should set minutes from route when end time is null")
    public void shouldSetMinutesFromRouteWhenDEndTimeIsNull() {

        Driving driving = new Driving(EXIST_OBJECT_ID, DURATION, STARTED, null, ROUTE,
                DrivingStatus.PENDING, EXIST_DRIVER, PRICE
        );
        driving.setActive(true);

        SimpleDrivingInfoDTO simpleDrivingInfoDTO = new SimpleDrivingInfoDTO(driving);

        Assertions.assertEquals(TIME_IN_MIN, simpleDrivingInfoDTO.getMinutes());
    }

    @Test
    @DisplayName("T11-SimpleDrivingInfoDTO creation, should set minutes like difference between end and started time")
    public void shouldSetMinutesLikeDifferenceBetweenEndAndStartTime() {

        Driving driving = new Driving(EXIST_OBJECT_ID, DURATION, STARTED, END, ROUTE,
                DrivingStatus.PENDING, EXIST_DRIVER, PRICE
        );
        SimpleDrivingInfoDTO simpleDrivingInfoDTO = new SimpleDrivingInfoDTO(driving);

        Assertions.assertEquals(DURATION, abs(simpleDrivingInfoDTO.getMinutes()));
    }

    @Test
    @DisplayName("T12-Finishing driving failed, driving not found exception")
    public void shouldThrowEntityNotFoundExceptionWhenDrivingFinishing() {

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> drivingService.finishDriving(NOT_EXIST_OBJECT_ID));

        verify(drivingRepository, times(0)).save(any(Driving.class));
        verify(webSocketService, times(0)).finishDrivingNotification(any(SimpleDrivingInfoDTO.class), anySet());
    }

    @Test
    @DisplayName("T13-Finishing driving successfully")
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
        Assertions.assertFalse(drivingResponse.isActive());
        Assertions.assertEquals(FINISHED, drivingResponse.getDrivingStatus());
        Assertions.assertEquals(EXPECTED_CURRENT_LOCATION_INDEX_FOR_FINISHED_DRIVING, drivingArgumentCaptor.getValue().getDriver().getVehicle().getCurrentLocationIndex());
        Assertions.assertNull(drivingArgumentCaptor.getValue().getDriver().getVehicle().getActiveRoute());
        Assertions.assertEquals(EXPECTED_CROSSED_WAYPOINTS, drivingArgumentCaptor.getValue().getDriver().getVehicle().getCrossedWaypoints());
    }

    private Driving createFinishedDriving(Driving driving) {
        driving.setActive(false);
        driving.setDrivingStatus(FINISHED);
        driving.setEnd(LocalDateTime.now());
        driving.getDriver().getVehicle().setCurrentLocationIndex(-1);
        driving.getDriver().getVehicle().setActiveRoute(null);
        driving.getDriver().getVehicle().setCrossedWaypoints(0);
        driving.getDriver().setDrive(false);

        return driving;
    }

    private Driving createStartedDriving(Driving driving) {
        driving.setStarted(LocalDateTime.now());
        driving.setActive(true);
        driving.getDriver().getVehicle().setActiveRoute(driving.getRoute());
        driving.getDriver().getVehicle().setCurrentLocationIndex(0);
        driving.getDriver().getVehicle().setCrossedWaypoints(0);
        driving.getDriver().getVehicle().setCurrentStop(driving.getRoute().getLocations().first().getLocation());
        driving.getDriver().setDrive(true);
        driving.setDrivingStatus(DrivingStatus.ACCEPTED);

        return driving;
    }

}
