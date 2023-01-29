package com.example.serbUber.server.service;

import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.model.Driving;
import com.example.serbUber.model.DrivingStatus;
import com.example.serbUber.model.VehicleType;
import com.example.serbUber.model.VehicleTypeInfo;
import com.example.serbUber.repository.VehicleTypeInfoRepository;
import com.example.serbUber.service.VehicleTypeInfoService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Optional;

import static com.example.serbUber.exception.EntityType.VEHICLE_TYPE_INFO;
import static com.example.serbUber.server.helper.Constants.*;
import static com.example.serbUber.server.helper.VehicleTypeInfoConstants.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.DisplayName.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class VehicleTypeInfoServiceTest {
    @Mock
    private VehicleTypeInfoRepository vehicleTypeInfoRepository;
    @InjectMocks
    private VehicleTypeInfoService vehicleTypeInfoService;

    @BeforeAll
    public void setup() {
        MockitoAnnotations.openMocks(this);

    }

    @Test
    @DisplayName("T1-Should throw vehicle type info not found exception")
    public void shouldThrowVehicleTypeInfoNotFoundIsCorrectNumberOfSeats() {

        when(vehicleTypeInfoRepository.getVehicleTypeInfoByName(VehicleType.SUV))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> vehicleTypeInfoService.get(VehicleType.SUV));

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> vehicleTypeInfoService.isCorrectNumberOfSeats(VEHICLE_TYPE_INFO_SUV, 5));
    }

    @Test
    @DisplayName("T2-Should return false, number of passengers is greater than number of seats")
    public void shouldReturnFalseIsCorrectNumberOfSeats() throws EntityNotFoundException {

        VehicleTypeInfo vehicleTypeInfo = new VehicleTypeInfo(VehicleType.SUV, 100, 4);
        when(vehicleTypeInfoRepository.getVehicleTypeInfoByName(VehicleType.SUV))
                .thenReturn(Optional.of(vehicleTypeInfo));

        Assertions.assertDoesNotThrow(() -> vehicleTypeInfoService.get(VehicleType.SUV));

        assertFalse(vehicleTypeInfoService.isCorrectNumberOfSeats(VEHICLE_TYPE_INFO_SUV, 5));
    }

    @Test
    @DisplayName("T2-Should return true, number of seats is greater than number of passengers")
    public void shouldReturnTrueIsCorrectNumberOfSeats() throws EntityNotFoundException {
        VehicleTypeInfo vehicleTypeInfo = new VehicleTypeInfo(VehicleType.SUV, 100, 5);
        when(vehicleTypeInfoRepository.getVehicleTypeInfoByName(VehicleType.SUV))
                .thenReturn(Optional.of(vehicleTypeInfo));

        Assertions.assertDoesNotThrow(() -> vehicleTypeInfoService.get(VehicleType.SUV));

        assertTrue(vehicleTypeInfoService.isCorrectNumberOfSeats(VEHICLE_TYPE_INFO_SUV, 4));
    }
}
