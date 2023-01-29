package com.example.serbUber.server.service;

import com.beust.ah.A;
import com.example.serbUber.dto.DrivingDTO;
import com.example.serbUber.dto.SimpleDrivingInfoDTO;
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
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static com.example.serbUber.model.DrivingStatus.ACCEPTED;
import static com.example.serbUber.server.helper.Constants.*;
import static com.example.serbUber.server.helper.DriverConstants.EXIST_DRIVER;
import static com.example.serbUber.server.helper.DriverConstants.EXIST_DRIVER_EMAIL;
import static com.example.serbUber.server.helper.RegularUserConstants.FIRST_USER_ID;
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
    @DisplayName("T7-Should not found future drivings for driver")
    public void shouldNotFoundFutureDrivingsForDriver() {

        List<Driving> drivings = new LinkedList<>();

        when(drivingRepository.driverHasFutureDriving(EXIST_DRIVER.getId()))
            .thenReturn(drivings);

        Driving driving = drivingService.driverHasFutureDriving(EXIST_DRIVER.getId());

        Assertions.assertNull(driving);
        verify(drivingRepository).driverHasFutureDriving(EXIST_DRIVER.getId());
    }

    @Test
    @DisplayName("T8-Should found future driving for driver")
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
    @DisplayName("T9-Should not found active driving for user")
    public void shouldNotFoundActiveDrivingForUser() {

        List<Driving> drivings = new LinkedList<>();

        when(drivingRepository.getActiveDrivingForUser(anyLong(), any(LocalDateTime.class)))
            .thenReturn(drivings);

        SimpleDrivingInfoDTO activeDriving = drivingService.checkUserHasActiveDriving(FIRST_USER_ID);

        Assertions.assertNull(activeDriving);
    }

    @Test
    @DisplayName("T10-Should found active driving for user")
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

        Assertions.assertTrue(activeSimpleDrivingDTO.isActive());
        Assertions.assertEquals(activeDrivingDTO.getCost(), activeSimpleDrivingDTO.getCost());
    }
}
