package com.example.serbUber.server.service;

import com.example.serbUber.dto.DrivingDTO;
import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.exception.EntityType;
import com.example.serbUber.model.Driving;
import com.example.serbUber.model.DrivingStatus;
import com.example.serbUber.model.Route;
import com.example.serbUber.model.user.Driver;
import com.example.serbUber.repository.DrivingRepository;
import com.example.serbUber.service.DrivingService;
import com.example.serbUber.service.DrivingStatusNotificationService;
import com.example.serbUber.service.RouteService;
import com.example.serbUber.service.WebSocketService;
import com.example.serbUber.service.user.UserService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static com.example.serbUber.server.helper.Constants.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.DisplayName.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DrivingServiceTest {

    @InjectMocks
    private DrivingService drivingService;

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

    @BeforeAll
    public void setup() {
        MockitoAnnotations.openMocks(this);

        when(drivingRepository.getDrivingById(DRIVING_NOT_EXIST_ID))
                .thenReturn(Optional.empty());

        when(drivingRepository.getDrivingById(DRIVING_EXIST_ID))
                .thenReturn(Optional.of(NOT_REJECTED_DRIVING));
    }

    @Test
    @DisplayName("T1-Should throw driving not found exception")
    public void shouldThrowDrivingNotFoundException() {

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> {drivingService.rejectDriving(DRIVING_NOT_EXIST_ID, DRIVING_REJECTION_REASON);});

        verify(drivingRepository, times(0)).save(any(Driving.class));
        verify(webSocketService, times(0)).sendRejectDriving(anyString(), anyString(), anySet());
    }

    @Test
    @DisplayName("T2-Should reject driving")
    public void shouldRejectDriving() throws EntityNotFoundException {

        Driving rejectedDriving = NOT_REJECTED_DRIVING;
        rejectedDriving.setDrivingStatus(DrivingStatus.REJECTED);
        rejectedDriving.getDriver().getVehicle().setCurrentLocationIndex(-1);
        rejectedDriving.getDriver().getVehicle().setActiveRoute(null);

        when(drivingStatusNotificationService.create(DRIVING_REJECTION_REASON, DrivingStatus.REJECTED, rejectedDriving))
                .thenReturn(REJECTED_DRIVING_STATUS_NOTIFICATION);

        doNothing().when(webSocketService).sendRejectDriving(anyString(), anyString(), anySet());
        DrivingDTO drivingDTO = drivingService.rejectDriving(DRIVING_EXIST_ID, DRIVING_REJECTION_REASON);

        Assertions.assertEquals(DrivingStatus.REJECTED, drivingDTO.getDrivingStatus());
        Assertions.assertEquals(DRIVING_EXIST_ID, drivingDTO.getId());

    }

}
