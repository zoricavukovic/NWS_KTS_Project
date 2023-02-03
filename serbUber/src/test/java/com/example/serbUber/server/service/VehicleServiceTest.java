package com.example.serbUber.server.service;

import com.example.serbUber.dto.VehicleCurrentLocationDTO;
import com.example.serbUber.dto.VehicleCurrentLocationForLocustDTO;
import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.model.*;
import com.example.serbUber.model.user.Driver;
import com.example.serbUber.repository.VehicleRepository;
import com.example.serbUber.service.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.example.serbUber.server.service.helper.Constants.*;
import static com.example.serbUber.server.service.helper.Constants.EXIST_OBJECT_ID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.DisplayName.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class VehicleServiceTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private WebSocketService webSocketService;

    @Captor
    private ArgumentCaptor<Vehicle> vehicleArgumentCaptor;

    @InjectMocks
    private VehicleService vehicleService;

    @BeforeAll
    public void setup() {
        MockitoAnnotations.openMocks(this);

        when(vehicleRepository.findById(NOT_EXIST_OBJECT_ID))
                .thenReturn(Optional.empty());

        when(vehicleRepository.getDriverByVehicleId(NOT_EXIST_OBJECT_ID))
                .thenReturn(Optional.empty());
    }

    @Test
    @DisplayName("T1-Should throw vehicle not found exception when calls getVehicleById")
    public void shouldThrowVehicleNotFoundExceptionWhenCallsGetVehicleById() {

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> vehicleService.getVehicleById(NOT_EXIST_OBJECT_ID));
    }

    @Test
    @DisplayName("T2-Should successfully return vehicle when calls getVehicleById")
    public void shouldSuccessfullyReturnVehicleWhenCallsGetVehicleById() throws EntityNotFoundException {

        when(vehicleRepository.findById(EXIST_OBJECT_ID))
                .thenReturn(Optional.of(EXIST_VEHICLE));

        Vehicle vehicleResponse = vehicleService.getVehicleById(EXIST_OBJECT_ID);
        Assertions.assertEquals(EXIST_VEHICLE.getId(), vehicleResponse.getId());
    }

    @Test
    @DisplayName("T3-Should throw driver not found exception when calls getDriverByVehicleId")
    public void shouldThrowDriverNotFoundExceptionWhenCallsGetDriverById() {

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> vehicleService.getDriverByVehicleId(NOT_EXIST_OBJECT_ID));
    }

    @Test
    @DisplayName("T4-Should successfully return driver when calls getDriverByVehicleId")
    public void shouldSuccessfullyReturnDriverWhenCallsGetDriverById() throws EntityNotFoundException {

        when(vehicleRepository.getDriverByVehicleId(EXIST_OBJECT_ID))
                .thenReturn(Optional.of(EXIST_DRIVER));

        Driver driverResponse = vehicleService.getDriverByVehicleId(EXIST_OBJECT_ID);
        Assertions.assertEquals(EXIST_DRIVER.getId(), driverResponse.getId());
    }

    @Test
    @DisplayName("T5-Should throw vehicle not found exception when calling update current position")
    public void shouldThrowVehicleNotFoundExceptionWhenCallingUpdateCurrentPosition() {

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> vehicleService.updateCurrentPosition(NOT_EXIST_OBJECT_ID, LON, LAT, EXPECTED_CROSSED_WAYPOINTS, 0));

        verify(vehicleRepository, never()).save(any(Vehicle.class));
        verify(webSocketService, never()).sendVehicleCurrentLocation(any(VehicleCurrentLocationDTO.class));
    }

    @ParameterizedTest
    @DisplayName("T6-Should update current position successfully")
    @ValueSource(ints = {-1,0,1})
    public void ShouldUpdateCurrentLocationSuccessfully(int currentLocationIndex) throws EntityNotFoundException {

        Vehicle vehicle = EXIST_VEHICLE;
        vehicle.setCurrentLocationIndex(currentLocationIndex);
        vehicle.setCurrentStop(LOCATION);

        when(vehicleRepository.findById(EXIST_OBJECT_ID))
                .thenReturn(Optional.of(vehicle));

        when(vehicleRepository.getDriverByVehicleId(EXIST_OBJECT_ID))
                .thenReturn(Optional.of(EXIST_DRIVER));

        Vehicle updatedVehicle = updateVehicle(vehicle, NEW_LAT, NEW_LON, EXPECTED_CROSSED_WAYPOINTS);

        when(vehicleRepository.save(updatedVehicle)).thenReturn(updatedVehicle);
        doNothing().when(webSocketService).sendVehicleCurrentLocation(any(VehicleCurrentLocationDTO.class));

        VehicleCurrentLocationForLocustDTO response = vehicleService.updateCurrentPosition(EXIST_OBJECT_ID, NEW_LON, NEW_LAT, EXPECTED_CROSSED_WAYPOINTS, CHOSEN_ROUTE_IDX);

        verify(vehicleRepository).save(vehicleArgumentCaptor.capture());
        verify(webSocketService).sendVehicleCurrentLocation(any(VehicleCurrentLocationDTO.class));
        Assertions.assertEquals(NEW_LAT, vehicleArgumentCaptor.getValue().getCurrentStop().getLat());
        Assertions.assertEquals(NEW_LON, vehicleArgumentCaptor.getValue().getCurrentStop().getLon());
        Assertions.assertEquals(EXPECTED_CROSSED_WAYPOINTS, response.getCrossedWaypoints());
        Assertions.assertEquals(vehicle.getId(), response.getVehicleId());
        Assertions.assertEquals(EXIST_DRIVER.getId(), response.getDriverId());
    }

    @Test
    @DisplayName("T7-Should throw driver not found exception when calling update current position")
    public void shouldThrowDriverNotFoundExceptionWhenCallingUpdateCurrentPosition() {
        Vehicle vehicle = EXIST_VEHICLE;
        vehicle.setCurrentLocationIndex(EXPECTED_CURRENT_LOCATION_INDEX_FOR_STARTED_DRIVING);
        vehicle.setCurrentStop(LOCATION);

        when(vehicleRepository.findById(EXIST_OBJECT_ID))
                .thenReturn(Optional.of(vehicle));

        when(vehicleRepository.getDriverByVehicleId(EXIST_OBJECT_ID))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> vehicleService.updateCurrentPosition(EXIST_OBJECT_ID, LON, LAT, EXPECTED_CROSSED_WAYPOINTS, 0));

        verify(vehicleRepository, never()).save(any(Vehicle.class));
        verify(webSocketService, never()).sendVehicleCurrentLocation(any(VehicleCurrentLocationDTO.class));
    }

    private Vehicle updateVehicle(Vehicle vehicle, double lat, double lng, int crossedWaypoints) {
        Location location = vehicle.getCurrentStop();
        location.setLat(lat);
        location.setLon(lng);
        vehicle.setCurrentStop(location);
        vehicle.setCrossedWaypoints(crossedWaypoints);
        vehicle.setCurrentLocationIndex(vehicle.getCurrentLocationIndex() + 1);

        return vehicle;
    }

    @Test
    @DisplayName("T8-VehicleCurrentLocationDTO creation, should set in drive when has active route")
    public void shouldSetInDriveWhenHasActiveRoute() {

        VehicleWithDriverId vehicleWithDriverId = new VehicleWithDriverId(EXIST_VEHICLE, EXIST_OBJECT_ID, true);
        vehicleWithDriverId.getVehicle().setActiveRoute(ROUTE);
        vehicleWithDriverId.getVehicle().setCurrentStop(LOCATION);

        VehicleCurrentLocationDTO vehicleCurrentLocationDTO = new VehicleCurrentLocationDTO(vehicleWithDriverId);

        Assertions.assertTrue(vehicleCurrentLocationDTO.isInDrive());
        Assertions.assertEquals(LOCATION, vehicleCurrentLocationDTO.getCurrentLocation());
    }

    @Test
    @DisplayName("T9-VehicleCurrentLocationDTO creation, should set not in drive when don't have active route")
    public void shouldSetNotInDriveWhenNotActiveRoute() {
        VehicleWithDriverId vehicleWithDriverId = new VehicleWithDriverId(EXIST_VEHICLE, EXIST_OBJECT_ID, true);
        vehicleWithDriverId.getVehicle().setActiveRoute(null);

        VehicleCurrentLocationDTO vehicleCurrentLocationDTO = new VehicleCurrentLocationDTO(vehicleWithDriverId);

        Assertions.assertFalse(vehicleCurrentLocationDTO.isInDrive());
    }

    @Test
    @DisplayName("T10-VehicleCurrentLocationForLocustDTO creation, should set empty lists for route index and waypoints")
    public void shouldSetEmptyRouteIndexAndWayPoints() {
        VehicleWithDriverId vehicleWithDriverId = new VehicleWithDriverId(EXIST_VEHICLE, EXIST_OBJECT_ID, true);
        vehicleWithDriverId.getVehicle().setActiveRoute(null);

        VehicleCurrentLocationForLocustDTO vehicleCurrentLocationForLocustDTO = new VehicleCurrentLocationForLocustDTO(vehicleWithDriverId);

        Assertions.assertFalse(vehicleCurrentLocationForLocustDTO.isInDrive());
        Assertions.assertEquals(0, vehicleCurrentLocationForLocustDTO.getWaypoints().size());
        Assertions.assertEquals(0, vehicleCurrentLocationForLocustDTO.getChosenRouteIdx().size());
    }

    @Test
    @DisplayName("T11-VehicleCurrentLocationForLocustDTO creation, should set populated lists for route index and waypoints")
    public void shouldPopulateRouteIndexAndWayPointsLists() {
        VehicleWithDriverId vehicleWithDriverId = new VehicleWithDriverId(EXIST_VEHICLE, EXIST_OBJECT_ID, true);
        vehicleWithDriverId.getVehicle().setActiveRoute(ROUTE);
        vehicleWithDriverId.getVehicle().setCurrentStop(LOCATION);

        VehicleCurrentLocationForLocustDTO vehicleCurrentLocationForLocustDTO = new VehicleCurrentLocationForLocustDTO(vehicleWithDriverId);

        Assertions.assertTrue(vehicleCurrentLocationForLocustDTO.isInDrive());
        Assertions.assertEquals(1, vehicleCurrentLocationForLocustDTO.getWaypoints().size());
        Assertions.assertEquals(1, vehicleCurrentLocationForLocustDTO.getChosenRouteIdx().size());
    }
}
