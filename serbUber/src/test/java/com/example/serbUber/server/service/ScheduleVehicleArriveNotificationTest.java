package com.example.serbUber.server.service;

import com.example.serbUber.dto.SimpleDrivingInfoDTO;
import com.example.serbUber.model.Driving;
import com.example.serbUber.model.Location;
import com.example.serbUber.model.user.Driver;
import com.example.serbUber.server.service.helper.Constants;
import com.example.serbUber.service.DrivingService;
import com.example.serbUber.service.ScheduleVehicleArriveNotification;
import com.example.serbUber.service.WebSocketService;
import com.example.serbUber.service.user.DriverService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.serbUber.model.DrivingStatus.ACCEPTED;
import static com.example.serbUber.server.service.helper.Constants.*;
import static com.example.serbUber.server.service.helper.Constants.DRIVER_ID;
import static com.example.serbUber.server.service.helper.DriverConstants.*;
import static com.example.serbUber.server.service.helper.DriverConstants.VEHICLE_1;
import static com.example.serbUber.server.service.helper.LocationHelper.THIRD_LOCATION;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.DisplayName.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ScheduleVehicleArriveNotificationTest {

    @Mock
    private DriverService driverService;


    @Mock
    private DrivingService drivingService;

    @Mock
    private WebSocketService webSocketService;

    @InjectMocks
    private ScheduleVehicleArriveNotification scheduleVehicleArriveNotification;

    @BeforeAll
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should send arrive notification")
    public void shouldSendArriveNotification() {
        Driver activeDriver = createDriver(DRIVER_ID, DRIVER_1.getEmail(), VEHICLE_1, LOCATION);
        Driving onWayDriving = new Driving(EXIST_OBJECT_ID, DURATION, LocalDateTime.now(), null, ROUTE, ACCEPTED, activeDriver, PRICE, null);

        when(driverService.getActiveDrivers()).thenReturn(List.of(activeDriver));
        when(drivingService.getTimeToDepartureDriving(activeDriver.getId())).thenReturn(onWayDriving);
        when(drivingService.driverHasFutureDriving(activeDriver.getId())).thenReturn(Constants.NOT_REJECTED_DRIVING);

        when(drivingService.save(onWayDriving)).thenReturn(onWayDriving);
        doNothing().when(webSocketService).sendVehicleArriveNotification(any(SimpleDrivingInfoDTO.class), anySet(), anyString());

        scheduleVehicleArriveNotification.createVehicleArriveNotification();

        verify(driverService, times(1)).getActiveDrivers();
        verify(drivingService, times(1)).getTimeToDepartureDriving(activeDriver.getId());
        verify(drivingService, times(1)).driverHasFutureDriving(activeDriver.getId());
        verify(drivingService, times(1)).save(onWayDriving);
        verify(webSocketService, times(1)).sendVehicleArriveNotification(any(SimpleDrivingInfoDTO.class), anySet(), anyString());
    }

    @Test
    @DisplayName("Should not send arrive notification due to missing on my way")
    public void shouldNotSendArriveNotificationMissingOnMyWayDriving() {
        Driver activeDriver = createDriver(DRIVER_ID, DRIVER_1.getEmail(), VEHICLE_1, LOCATION);

        when(driverService.getActiveDrivers()).thenReturn(List.of(activeDriver));
        when(drivingService.getTimeToDepartureDriving(activeDriver.getId())).thenReturn(null);
        when(drivingService.driverHasFutureDriving(activeDriver.getId())).thenReturn(Constants.NOT_REJECTED_DRIVING);

        scheduleVehicleArriveNotification.createVehicleArriveNotification();

        verify(driverService, times(1)).getActiveDrivers();
        verify(drivingService, times(1)).getTimeToDepartureDriving(activeDriver.getId());
        verify(drivingService, times(1)).driverHasFutureDriving(activeDriver.getId());
        verify(drivingService, times(0)).save(any());
        verify(webSocketService, times(0)).sendVehicleArriveNotification(any(SimpleDrivingInfoDTO.class), anySet(), anyString());
    }

    @Test
    @DisplayName("Should send arrive notification due to vehicle not yet arrived")
    public void shouldNotSendArriveNotificationDueToVehicleNotYetArrived() {
        Driver activeDriver = createDriver(DRIVER_ID, DRIVER_1.getEmail(), VEHICLE_1, THIRD_LOCATION);
        Driving onWayDriving = new Driving(EXIST_OBJECT_ID, DURATION, LocalDateTime.now(), null, ROUTE, ACCEPTED, activeDriver, PRICE, null);

        when(driverService.getActiveDrivers()).thenReturn(List.of(activeDriver));
        when(drivingService.getTimeToDepartureDriving(activeDriver.getId())).thenReturn(onWayDriving);
        when(drivingService.driverHasFutureDriving(activeDriver.getId())).thenReturn(Constants.NOT_REJECTED_DRIVING);

        scheduleVehicleArriveNotification.createVehicleArriveNotification();

        verify(driverService, times(1)).getActiveDrivers();
        verify(drivingService, times(1)).getTimeToDepartureDriving(activeDriver.getId());
        verify(drivingService, times(1)).driverHasFutureDriving(activeDriver.getId());
        verify(drivingService, times(0)).save(onWayDriving);
        verify(webSocketService, times(0)).sendVehicleArriveNotification(any(SimpleDrivingInfoDTO.class), anySet(), anyString());
    }

}
