package com.example.serbUber.server.service;

import com.example.serbUber.dto.DrivingDTO;
import com.example.serbUber.model.Driving;
import com.example.serbUber.model.user.Driver;
import com.example.serbUber.service.*;
import com.example.serbUber.service.user.DriverService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.mockito.Mockito.*;

import static com.example.serbUber.server.service.helper.Constants.*;
import static com.example.serbUber.server.service.helper.DriverConstants.*;
import static com.example.serbUber.server.service.helper.DriverConstants.DRIVER_ID;
import static com.example.serbUber.server.service.helper.DriverConstants.NOT_REJECTED_DRIVING;


@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.DisplayName.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SchedulerDriverTest {

    @Mock
    private DriverService driverService;

    @Mock
    private DrivingService drivingService;

    @Mock
    private WebSocketService webSocketService;

    @InjectMocks
    private SchedulerDriver schedulerDriver;

    @BeforeAll
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("T1 - all active drivings should be empty, nothing will be called")
    public void startDrivingShouldNotBeCalledEmptyActiveList() {

        when(driverService.getActiveDrivers()).thenReturn(new ArrayList<>());
        schedulerDriver.createOnMyWayDrivingForDriver();

        verify(drivingService, times(0)).driverHasFutureDriving(any());
        verify(drivingService, times(0)).createDrivingToDeparture(any(), any(), any(), anySet());
        verify(webSocketService, times(0)).startDrivingToDeparture(anySet(), anyDouble());
    }

    @ParameterizedTest
    @DisplayName("T2 - should create on my way for driver")
    @MethodSource("getActiveDriversWithFutureDriving")
    public void shouldCreateOnMyWayForDriver(Driver driver) {
        when(driverService.getActiveDrivers()).thenReturn(List.of(driver));
        when(drivingService.driverHasFutureDriving(driver.getId())).thenReturn(NOT_REJECTED_DRIVING);
        when(driverService.isTimeToGoToDeparture(driver, NOT_REJECTED_DRIVING)).thenReturn(true);
        when( drivingService.createDrivingToDeparture(driver, driver.getVehicle().getCurrentStop(), NOT_REJECTED_DRIVING.getRoute(), NOT_REJECTED_DRIVING.getUsers()))
                .thenReturn(new DrivingDTO(NOT_REJECTED_DRIVING));
        doNothing().when(webSocketService).startDrivingToDeparture(NOT_REJECTED_DRIVING.getUsers(), NOT_REJECTED_DRIVING.getDuration());

        schedulerDriver.createOnMyWayDrivingForDriver();

        verify(drivingService, times(1)).driverHasFutureDriving(driver.getId());
        verify(drivingService, times(1)).createDrivingToDeparture(driver, driver.getVehicle().getCurrentStop(), NOT_REJECTED_DRIVING.getRoute(), NOT_REJECTED_DRIVING.getUsers());
        verify(webSocketService, times(1)).startDrivingToDeparture(NOT_REJECTED_DRIVING.getUsers(), NOT_REJECTED_DRIVING.getDuration());
    }

    @Test
    @DisplayName("T3 - should not create on my way for driver")
    public void shouldNotCreateOnMyWayForDriver() {
        List<Driver> activeDrivers = createActiveDriversList();

        when(driverService.getActiveDrivers()).thenReturn(activeDrivers);
        when(drivingService.driverHasFutureDriving(DRIVER_ID)).thenReturn(NOT_REJECTED_DRIVING);
        when(drivingService.driverHasFutureDriving(DRIVER_ID_2)).thenReturn(NOT_REJECTED_DRIVING);
        when(drivingService.driverHasFutureDriving(DRIVER_ID_3)).thenReturn(null);
        when(drivingService.driverHasFutureDriving(DRIVER_ID_4)).thenReturn(NOT_REJECTED_DRIVING);

        when(driverService.isTimeToGoToDeparture(any(Driver.class), any(Driving.class))).thenReturn(false);

        schedulerDriver.createOnMyWayDrivingForDriver();

        verify(drivingService, times(4)).driverHasFutureDriving(anyLong());
        verify(drivingService, times(0)).createDrivingToDeparture(any(), any(), any(), anySet());
        verify(webSocketService, times(0)).startDrivingToDeparture(anySet(), anyDouble());
    }

    private List<Arguments> getActiveDriversWithFutureDriving() {
        return Arrays.asList(Arguments.arguments(
                createDriver(DRIVER_ID, DRIVER_1.getEmail(), VEHICLE_1, LOCATION)
                ),
                Arguments.arguments(
                 createDriver(DRIVER_ID_2, DRIVER_EMAIL_2, VEHICLE_2, LOCATION)
                ));
    }

}
