package com.example.serbUber.server.service;

import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.model.VehicleType;
import com.example.serbUber.model.VehicleTypeInfo;
import com.example.serbUber.repository.VehicleTypeInfoRepository;
import com.example.serbUber.service.VehicleTypeInfoService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import static com.example.serbUber.server.helper.VehicleTypeInfoConstants.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
    }

    @Test
    @DisplayName("T2-Should return false, number of passengers is greater than number of seats")
    public void shouldReturnFalseIsCorrectNumberOfSeats() {

        VehicleTypeInfo vehicleTypeInfo = new VehicleTypeInfo(VehicleType.SUV, 100, 4);
        when(vehicleTypeInfoRepository.getVehicleTypeInfoByName(VehicleType.SUV))
                .thenReturn(Optional.of(vehicleTypeInfo));

        Assertions.assertDoesNotThrow(() -> vehicleTypeInfoService.get(VehicleType.SUV));

        assertFalse(vehicleTypeInfoService.isCorrectNumberOfSeats(VEHICLE_TYPE_INFO_SUV, 3));
    }

    @ParameterizedTest
    @DisplayName("T2-Should return true, number of seats is greater than number of passengers")
    @ValueSource(ints = {1, 2})
    public void shouldReturnTrueIsCorrectNumberOfSeats(int numberOfPassengers) {
        VehicleTypeInfo vehicleTypeInfo = new VehicleTypeInfo(VehicleType.SUV, 100, 5);
        when(vehicleTypeInfoRepository.getVehicleTypeInfoByName(VehicleType.SUV))
                .thenReturn(Optional.of(vehicleTypeInfo));

        Assertions.assertDoesNotThrow(() -> vehicleTypeInfoService.get(VehicleType.SUV));

        assertTrue(vehicleTypeInfoService.isCorrectNumberOfSeats(VEHICLE_TYPE_INFO_SUV, numberOfPassengers));
    }
}
