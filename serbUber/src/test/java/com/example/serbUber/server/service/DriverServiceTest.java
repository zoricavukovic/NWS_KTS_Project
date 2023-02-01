package com.example.serbUber.server.service;

import com.example.serbUber.model.*;
import com.example.serbUber.model.user.Driver;
import com.example.serbUber.repository.user.DriverRepository;
import com.example.serbUber.service.*;
import com.example.serbUber.service.user.DriverService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static com.example.serbUber.server.service.helper.Constants.*;
import static com.example.serbUber.server.service.helper.DriverConstants.*;
import static com.example.serbUber.server.service.helper.DrivingConstants.*;
import static com.example.serbUber.server.service.helper.DrivingNotificationUtil.getDrivingNotification;
import static com.example.serbUber.server.service.helper.LocationHelper.*;
import static com.example.serbUber.server.service.helper.RegularUserConstants.FIRST_USER;
import static com.example.serbUber.server.service.helper.VehicleTypeInfoConstants.VEHICLE_TYPE_INFO_SUV;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.DisplayName.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DriverServiceTest {

    @Mock
    private DriverRepository driverRepository;
    @Mock
    private RouteService routeService;
    @InjectMocks
    private DriverService driverService;

    @BeforeAll
    public void setup() {
        MockitoAnnotations.openMocks(this);
        doReturn(3.0).when(routeService).calculateMinutesForDistance(LOCATION_LON_LAT_1[1], LOCATION_LON_LAT_1[0], LOCATION_LON_LAT_3[1], LOCATION_LON_LAT_3[0]);
        doReturn(9.0).when(routeService).calculateMinutesForDistance(LOCATION_LON_LAT_2[1], LOCATION_LON_LAT_2[0], LOCATION_LON_LAT_3[1], LOCATION_LON_LAT_3[0]);
        doReturn(10.0).when(routeService).calculateMinutesForDistance(LOCATION_LON_LAT_2[1], LOCATION_LON_LAT_2[0], LOCATION_LON_LAT_1[1], LOCATION_LON_LAT_1[0]);
        doReturn(12.0).when(routeService).calculateMinutesForDistance(LOCATION_LON_LAT_1[1], LOCATION_LON_LAT_1[0], LOCATION_LON_LAT_2[1], LOCATION_LON_LAT_2[0]);
        doReturn(12.0).when(routeService).calculateMinutesForDistance(LOCATION_LON_LAT_3[1], LOCATION_LON_LAT_3[0], LOCATION_LON_LAT_2[1], LOCATION_LON_LAT_2[0]);
        doReturn(0.0).when(routeService).calculateMinutesForDistance(LOCATION_LON_LAT_2[1], LOCATION_LON_LAT_2[0], LOCATION_LON_LAT_2[1], LOCATION_LON_LAT_2[0]);
    }

    @Test()
    @DisplayName("T1-Should return null for driver, active drivers list is empty")
    public void getDriverForDriving_returnNull_activeDriversListEmpty() {
        DrivingNotification drivingNotification = new DrivingNotification(
                ROUTE, PRICE, FIRST_USER, LocalDateTime.now(), DURATION, false, false, VEHICLE_TYPE_INFO_SUV,
                new HashMap<>(), false);
        when(driverRepository.getActiveDriversWhichVehicleMatchParams(VehicleType.SUV)).thenReturn(Collections.emptyList());
        assertNull(driverService.getDriverForDriving(drivingNotification));
    }

    @Test()
    @DisplayName("T2-Should return null for driver, driver's vehicle is not have match parameters")
    public void getDriverForDriving_returnNull_vehicleNotHaveMatchParameters() {
        DrivingNotification drivingNotification = getDrivingNotification(FIRST_USER, LocalDateTime.now(), false, PRICE, false);
        drivingNotification.setBabySeat(true);
        List<Driver> drivers = new ArrayList<>();
        drivers.add(createDriver(DRIVER_ID_1, DRIVER_EMAIL_1, VEHICLE_1, THIRD_LOCATION));
        drivers.add(createDriver(DRIVER_ID_2, DRIVER_EMAIL_2, VEHICLE_2, THIRD_LOCATION));
        drivers.add(createDriver(DRIVER_ID_3, DRIVER_EMAIL_3, VEHICLE_3, THIRD_LOCATION));
        when(driverRepository.getActiveDriversWhichVehicleMatchParams(VehicleType.SUV)).thenReturn(drivers);

        assertNull(driverService.getDriverForDriving(drivingNotification));
        verify(routeService, never()).calculateMinutesForDistance(anyDouble(), anyDouble(), anyDouble(), anyDouble());
    }

    @Test()
    @DisplayName("T3-Should return nearest driver, all drivers are free or free with future drivings")
    public void getDriverForDriving_returnNearestDriver_allFreeDrivers() {
        SortedSet<DrivingLocationIndex> drivingLocationIndexSet = createDrivingLocationIndex();
        Route route = createTestRoute(drivingLocationIndexSet, 10, 3000);
        DrivingNotification drivingNotification = getDrivingNotification(FIRST_USER, LocalDateTime.now(), false, PRICE, false);
        drivingNotification.setRoute(route);

        mockAllFreeDrivers();

        Driver actualDriver = driverService.getDriverForDriving(drivingNotification);

        Assertions.assertEquals(DRIVER_EMAIL_1, actualDriver.getEmail());
    }

    @Test()
    @DisplayName("T4-Should return driver, his shift is not ended soon, all drivers are free or free with future drivings")
    public void getDriverForDriving_returnDriverInShift_allFreeDrivers() {
        SortedSet<DrivingLocationIndex> drivingLocationIndexSet = createDrivingLocationIndex();
        Route route = createTestRoute(drivingLocationIndexSet, 10, 3000);
        DrivingNotification drivingNotification = getDrivingNotification(FIRST_USER, LocalDateTime.now(), false, PRICE, false);
        drivingNotification.setRoute(route);

        mockDriverInShiftAllFreeDrivers();

        Driver actualDriver = driverService.getDriverForDriving(drivingNotification);
        Assertions.assertEquals(DRIVER_EMAIL_3, actualDriver.getEmail());
    }

    @Test()
    @DisplayName("T5-Should return null for driver, all drivers are busy with future drivings or shift is ended soon")
    public void getDriverForDriving_returnNull_allDriversBusyWithFutureDrivings() {
        SortedSet<DrivingLocationIndex> drivingLocationIndexSet = createDrivingLocationIndex();
        Route route = createTestRoute(drivingLocationIndexSet, 10, 3000);
        DrivingNotification drivingNotification = getDrivingNotification(FIRST_USER, LocalDateTime.now(), false, PRICE, false);
        drivingNotification.setRoute(route);

        mockDriversBusyWithFutureDrivings();
        Driver actualDriver = driverService.getDriverForDriving(drivingNotification);
        assertNull(actualDriver);
    }

    @Test()
    @DisplayName("T6-Should return driver who soon end active driving, all drivers are busy without future drivings")
    public void getDriverForDriving_returnDriverWhoSoonEndDriving_allDriversBusyWithoutFutureDrivings() {
        SortedSet<DrivingLocationIndex> drivingLocationIndexSet = createDrivingLocationIndex();
        Route route = createTestRoute(drivingLocationIndexSet, 10, 3000);
        DrivingNotification drivingNotification = new DrivingNotification(
                route, PRICE, FIRST_USER, LocalDateTime.now(), DURATION, false, false, VEHICLE_TYPE_INFO_SUV,
                new HashMap<>(), false);

        mockAndGetDriversWhoSoonEndDriving_allDriversBusyWithoutFutureDrivings();
        Driver actualDriver = driverService.getDriverForDriving(drivingNotification);
        Assertions.assertEquals(DRIVER_EMAIL_1, actualDriver.getEmail());
    }

    @Test()
    @DisplayName("T7-Should return driver who soon end active driving and shift is not ended soon, all drivers are busy")
    public void getDriverForDriving_returnDriverWhoSoonEndDriving_ShiftNotEndedSoon_allDriversBusy() {
        SortedSet<DrivingLocationIndex> drivingLocationIndexSet = createDrivingLocationIndex();
        Route route = createTestRoute(drivingLocationIndexSet, 10, 3000);
        DrivingNotification drivingNotification = getDrivingNotification(FIRST_USER, LocalDateTime.now(), false, PRICE, false);
        drivingNotification.setRoute(route);

        mockDriversWhoseShiftNotEndedSoon_allDriversBusy();

        Driver actualDriver = driverService.getDriverForDriving(drivingNotification);
        Assertions.assertEquals(DRIVER_EMAIL_2, actualDriver.getEmail());
    }

    private void mockAllFreeDrivers() {
        List<Driver> drivers = new ArrayList<>();
        Driver driver_1 = createDriver(DRIVER_ID_1, DRIVER_EMAIL_1, VEHICLE_1, THIRD_LOCATION);
        drivers.add(driver_1);

        Driver driver_2 = createDriver(DRIVER_ID_2, DRIVER_EMAIL_2, VEHICLE_2, SECOND_LOCATION);
        Driving driving_1 = createFutureDriving(25, driver_2);
        List<Driving> drivings_2 = new ArrayList<>();
        drivings_2.add(driving_1);
        driver_2.setDrivings(drivings_2);
        drivers.add(driver_2);

        Driver driver_3 = createDriver(DRIVER_ID_3, DRIVER_EMAIL_3, VEHICLE_3, SECOND_LOCATION);
        Driving driving_2 = createFutureDriving(20, driver_3);
        List<Driving> drivings_3 = new ArrayList<>();
        drivings_3.add(driving_2);
        driver_3.setDrivings(drivings_3);
        drivers.add(driver_3);

        when(driverRepository.getActiveDriversWhichVehicleMatchParams(VehicleType.SUV)).thenReturn(drivers);
    }

    private void mockDriverInShiftAllFreeDrivers() {
        List<Driver> drivers = new ArrayList<>();
        Driver driver_1 = createDriver(DRIVER_ID_1, DRIVER_EMAIL_1, VEHICLE_1, THIRD_LOCATION);
        driver_1.setWorkingMinutes(478);
        drivers.add(driver_1);

        Driver driver_2 = createDriver(DRIVER_ID_2, DRIVER_EMAIL_2, VEHICLE_2, SECOND_LOCATION);
        driver_2.setWorkingMinutes(479);
        drivers.add(driver_2);

        Driver driver_3 = createDriver(DRIVER_ID_3, DRIVER_EMAIL_3, VEHICLE_3, SECOND_LOCATION);
        driver_3.setWorkingMinutes(400);
        Driving driving_2 = createFutureDriving(20, driver_3);
        List<Driving> drivings_3 = new ArrayList<>();
        drivings_3.add(driving_2);
        driver_3.setDrivings(drivings_3);
        drivers.add(driver_3);

        when(driverRepository.getActiveDriversWhichVehicleMatchParams(VehicleType.SUV)).thenReturn(drivers);
    }

    private void mockDriversBusyWithFutureDrivings() {
        List<Driver> drivers = new ArrayList<>();
        Driver driver_1 = createDriver(DRIVER_ID_1, DRIVER_EMAIL_1, VEHICLE_1, THIRD_LOCATION);
        driver_1.setWorkingMinutes(478);
        drivers.add(driver_1);

        Driver driver_2 = createDriver(DRIVER_ID_2, DRIVER_EMAIL_2, VEHICLE_2, SECOND_LOCATION);
        List<Driving> drivings_2 = new ArrayList<>();
        Driving driving_1 = createActiveDriving(8, driver_2);
        drivings_2.add(driving_1);
        Driving driving_2 = createFutureDriving(10, driver_2);
        drivings_2.add(driving_2);
        driver_2.setDrive(true);
        driver_2.setDrivings(drivings_2);
        drivers.add(driver_2);

        Driver driver_3 = createDriver(DRIVER_ID_3, DRIVER_EMAIL_3, VEHICLE_3, SECOND_LOCATION);
        List<Driving> drivings_3 = new ArrayList<>();
        Driving driving_3 = createActiveDriving(5, driver_3);
        Driving driving_4 = createFutureDriving(15, driver_3);
        drivings_3.add(driving_3);
        drivings_3.add(driving_4);
        driver_3.setDrive(true);
        driver_3.setDrivings(drivings_3);
        drivers.add(driver_3);

        when(driverRepository.getActiveDriversWhichVehicleMatchParams(VehicleType.SUV)).thenReturn(drivers);
    }

    private void mockDriversWhoseShiftNotEndedSoon_allDriversBusy() {
        List<Driver> drivers = new ArrayList<>();
        Driver driver_1 = createDriver(DRIVER_ID_1, DRIVER_EMAIL_1, VEHICLE_1, SECOND_LOCATION);
        Driving driving_1 = createActiveDriving(8, driver_1);
        List<Driving> drivings_1 = new ArrayList<>();
        drivings_1.add(driving_1);
        driver_1.setDrive(true);
        driver_1.setDrivings(drivings_1);
        driver_1.setWorkingMinutes(478);
        drivers.add(driver_1);

        Driver driver_2 = createDriver(DRIVER_ID_2, DRIVER_EMAIL_2, VEHICLE_2, SECOND_LOCATION);
        Driving driving_2 = createActiveDriving(8, driver_2);
        List<Driving> drivings_2 = new ArrayList<>();
        drivings_2.add(driving_2);
        driver_2.setDrive(true);
        driver_2.setDrivings(drivings_2);
        drivers.add(driver_2);

        Driver driver_3 = createDriver(DRIVER_ID_3, DRIVER_EMAIL_3, VEHICLE_3, FIRST_LOCATION);
        List<Driving> drivings_3 = new ArrayList<>();
        Driving driving_3 = createActiveDriving(5, driver_3);
        Driving driving_4 = createFutureDriving(20, driver_3);
        drivings_3.add(driving_4);
        drivings_3.add(driving_3);
        driver_3.setDrive(true);
        driver_3.setDrivings(drivings_3);
        drivers.add(driver_3);

        when(driverRepository.getActiveDriversWhichVehicleMatchParams(VehicleType.SUV)).thenReturn(drivers);
    }

    private void mockAndGetDriversWhoSoonEndDriving_allDriversBusyWithoutFutureDrivings() {
        List<Driver> drivers = new ArrayList<>();
        Driver driver_1 = createDriver(DRIVER_ID_1, DRIVER_EMAIL_1, VEHICLE_1, SECOND_LOCATION);
        Driving driving_1 = createActiveDriving(8, driver_1);
        List<Driving> drivings_1 = new ArrayList<>();
        drivings_1.add(driving_1);
        driver_1.setDrive(true);
        driver_1.setDrivings(drivings_1);
        drivers.add(driver_1);

        Driver driver_2 = createDriver(DRIVER_ID_2, DRIVER_EMAIL_2, VEHICLE_2, SECOND_LOCATION);
        Driving driving_2 = createActiveDriving(8, driver_2);
        List<Driving> drivings_2 = new ArrayList<>();
        drivings_2.add(driving_2);
        driver_2.setDrive(true);
        driver_2.setDrivings(drivings_2);
        drivers.add(driver_2);

        Driver driver_3 = createDriver(DRIVER_ID_3, DRIVER_EMAIL_3, VEHICLE_3, FIRST_LOCATION);
        Driving driving_3 = createActiveDriving(5, driver_3);
        List<Driving> drivings_3 = new ArrayList<>();
        drivings_3.add(driving_3);
        driver_3.setDrive(true);
        driver_3.setDrivings(drivings_3);
        drivers.add(driver_3);

        when(driverRepository.getActiveDriversWhichVehicleMatchParams(VehicleType.SUV)).thenReturn(drivers);
    }
}
