package com.example.serbUber.server.service;

import com.example.serbUber.dto.VehicleTypeInfoDTO;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.serbUber.server.service.helper.VehicleTypeInfoConstants.*;
import static org.junit.jupiter.api.Assertions.*;
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
    public void getVehicleTypeInfoByName_throwEntityNotFoundException() {
        when(vehicleTypeInfoRepository.getVehicleTypeInfoByName(VehicleType.SUV))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> vehicleTypeInfoService.get(VehicleType.SUV));
    }

    @Test
    @DisplayName("T2-Should return false, number of passengers is greater than number of seats")
    public void isCorrectNumberOfSeats_returnFalse() {
        VehicleTypeInfo vehicleTypeInfo = new VehicleTypeInfo(VehicleType.SUV, 100, 4);
        when(vehicleTypeInfoRepository.getVehicleTypeInfoByName(VehicleType.SUV))
                .thenReturn(Optional.of(vehicleTypeInfo));

        Assertions.assertDoesNotThrow(() -> vehicleTypeInfoService.get(VehicleType.SUV));

        assertFalse(vehicleTypeInfoService.isCorrectNumberOfSeats(VEHICLE_TYPE_INFO_SUV, 3));
    }

    @ParameterizedTest
    @DisplayName("T3-Should return true, number of seats is greater than number of passengers")
    @ValueSource(ints = {1, 2})
    public void isCorrectNumberOfSeats_returnTrue(int numberOfPassengers) {
        when(vehicleTypeInfoRepository.getVehicleTypeInfoByName(VehicleType.SUV))
                .thenReturn(Optional.of(VEHICLE_TYPE_INFO_SUV));

        Assertions.assertDoesNotThrow(() -> vehicleTypeInfoService.get(VehicleType.SUV));
        assertTrue(vehicleTypeInfoService.isCorrectNumberOfSeats(VEHICLE_TYPE_INFO_SUV, numberOfPassengers));
    }

    @Test
    @DisplayName("T4-Should return correct calculated prices for vehicle types")
    public void getPriceForVehicleAndChosenRoute_returnCorrectCalculatedPricesForVehicleTypes() throws EntityNotFoundException {
        when(vehicleTypeInfoRepository.getVehicleTypeInfoByName(VehicleType.SUV))
                .thenReturn(Optional.of(VEHICLE_TYPE_INFO_SUV));
        assertEquals(6, vehicleTypeInfoService.getPriceForVehicleAndChosenRoute(3, VehicleType.SUV));
    }

    @Test
    @DisplayName("T5-Should throw exception, not found vehicle type")
    public void getPriceForVehicleAndChosenRoute_throwEntityNotFoundException()  {
        when(vehicleTypeInfoRepository.getVehicleTypeInfoByName(VehicleType.SUV))
                .thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,
                () -> vehicleTypeInfoService.getPriceForVehicleAndChosenRoute(3, VehicleType.SUV));
    }

    @Test
    @DisplayName("T6-Should return price for vehicle type")
    public void getPriceForVehicle_returnPrice() throws EntityNotFoundException {
        when(vehicleTypeInfoRepository.getVehicleTypeInfoByName(VehicleType.SUV))
                .thenReturn(Optional.of(VEHICLE_TYPE_INFO_SUV));
        assertEquals(3.0, vehicleTypeInfoService.getPriceForVehicle(VehicleType.SUV));
    }

    @Test
    @DisplayName("T7-Should throw exception, not found vehicle type")
    public void getPriceForVehicle_throwEntityNotFoundException()  {

        when(vehicleTypeInfoRepository.getVehicleTypeInfoByName(VehicleType.SUV))
                .thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,
                () -> vehicleTypeInfoService.getPriceForVehicle( VehicleType.SUV));
    }

    @Test
    @DisplayName("T8-Should return vehicle type for get vehicle type")
    public void get_returnVehicleType() throws EntityNotFoundException {
        when(vehicleTypeInfoRepository.getVehicleTypeInfoByName(VehicleType.SUV))
                .thenReturn(Optional.of(VEHICLE_TYPE_INFO_SUV));
        assertEquals(VEHICLE_TYPE_INFO_SUV, vehicleTypeInfoService.get(VehicleType.SUV));
    }

    @Test
    @DisplayName("T9-Should throw exception for get vehicle type")
    public void get_throwEntityNotFoundException() {
        when(vehicleTypeInfoRepository.getVehicleTypeInfoByName(VehicleType.SUV))
                .thenReturn(Optional.empty());
       assertThrows(EntityNotFoundException.class, () -> vehicleTypeInfoService.get(VehicleType.SUV));
    }

    @Test
    @DisplayName("T10-Should return all vehicle types")
    public void getAll_returnAllVehicleTypes() {
        List<VehicleTypeInfo> vehicleTypeInfoList = new ArrayList<>();
        vehicleTypeInfoList.add(VEHICLE_TYPE_INFO_CAR);
        vehicleTypeInfoList.add(VEHICLE_TYPE_INFO_VAN);
        vehicleTypeInfoList.add(VEHICLE_TYPE_INFO_SUV);
        when(vehicleTypeInfoRepository.findAll())
                .thenReturn(vehicleTypeInfoList);
        List<VehicleTypeInfoDTO> vehicleTypeInfoDTOS = vehicleTypeInfoService.getAll();

        assertEquals(vehicleTypeInfoDTOS.get(0).getVehicleType(), VEHICLE_TYPE_INFO_CAR.getVehicleType());
        assertEquals(vehicleTypeInfoDTOS.get(1).getVehicleType(), VEHICLE_TYPE_INFO_VAN.getVehicleType());
        assertEquals(vehicleTypeInfoDTOS.get(2).getVehicleType(), VEHICLE_TYPE_INFO_SUV.getVehicleType());
    }

    @Test
    @DisplayName("T11-Should return empty list of vehicle types")
    public void getAll_returnEmptyList() {
        List<VehicleTypeInfo> vehicleTypeInfoList = new ArrayList<>();
        when(vehicleTypeInfoRepository.findAll())
                .thenReturn(vehicleTypeInfoList);
        List<VehicleTypeInfoDTO> vehicleTypeInfoDTOS = vehicleTypeInfoService.getAll();

        assertEquals(vehicleTypeInfoDTOS.size(),0);
    }
}
