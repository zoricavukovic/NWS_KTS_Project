package com.example.serbUber.server.service;

import com.example.serbUber.dto.DrivingDTO;
import com.example.serbUber.dto.SimpleDrivingInfoDTO;
import com.example.serbUber.dto.VehicleCurrentLocationDTO;
import com.example.serbUber.exception.DriverAlreadyHasStartedDrivingException;
import com.example.serbUber.exception.DrivingShouldNotStartYetException;
import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.model.*;
import com.example.serbUber.model.user.RegularUser;
import com.example.serbUber.model.user.Role;
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
import java.util.*;

import static com.example.serbUber.model.DrivingStatus.ACCEPTED;
import static com.example.serbUber.server.helper.Constants.*;
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


    List<Arguments> getValidStartTimeForStartingDriving(){

        return Arrays.asList(arguments(LocalDateTime.now().plusMinutes(5)),
                arguments(LocalDateTime.now().plusMinutes(4)));
    }

}
