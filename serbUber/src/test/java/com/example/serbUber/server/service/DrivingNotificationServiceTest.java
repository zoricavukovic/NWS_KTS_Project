package com.example.serbUber.server.service;

import com.beust.ah.A;
import com.example.serbUber.dto.DrivingDTO;
import com.example.serbUber.dto.DrivingNotificationDTO;
import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.exception.ExcessiveNumOfPassengersException;
import com.example.serbUber.exception.InvalidChosenTimeForReservationException;
import com.example.serbUber.model.*;
import com.example.serbUber.model.user.RegularUser;
import com.example.serbUber.model.user.Role;
import com.example.serbUber.repository.DrivingNotificationRepository;
import com.example.serbUber.repository.DrivingRepository;
import com.example.serbUber.request.DrivingLocationIndexRequest;
import com.example.serbUber.request.LocationRequest;
import com.example.serbUber.request.RouteRequest;
import com.example.serbUber.service.*;
import com.example.serbUber.service.payment.TokenBankService;
import com.example.serbUber.service.user.DriverService;
import com.example.serbUber.service.user.RegularUserService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static com.example.serbUber.server.helper.Constants.*;
import static com.example.serbUber.server.helper.RegularUserConstants.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.DisplayName.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DrivingNotificationServiceTest {

    @Mock
    private DrivingNotificationRepository drivingNotificationRepository;

    @Mock
    private RegularUserService regularUserService;

    @Mock
    private DriverService driverService;

    @Mock
    private WebSocketService webSocketService;

    @Mock
    private DrivingService drivingService;

    @Mock
    private RouteService routeService;

    @Mock
    private VehicleTypeInfoService vehicleTypeInfoService;

    @Mock
    private TokenBankService tokenBankService;

    @Captor
    private ArgumentCaptor<DrivingNotification> postArgumentCaptor;

    @InjectMocks
    private DrivingNotificationService drivingNotificationService;

    @BeforeAll
    public void setup() {
        MockitoAnnotations.openMocks(this);

        when(drivingNotificationRepository.findById(NOT_EXIST_OBJECT_ID))
            .thenReturn(Optional.empty());
    }

    @Test
    @DisplayName("T1-Should throw driving notification not found exception")
    public void shouldThrowDrivingNotificationNotFoundException() {

        assertThrows(EntityNotFoundException.class,
            () -> drivingNotificationService.updateStatus(NOT_EXIST_OBJECT_ID, USER_EMAIL, true));

        verify(drivingNotificationRepository, times(0)).save(any(DrivingNotification.class));
    }

    @Test
    @DisplayName("T2-Should success update driving notification user accept/reject status")
    public void shouldSuccessDrivingNotificationUserAcceptDriving() throws EntityNotFoundException {

        Map<RegularUser, Integer> receiversReviewed = new HashMap<>();
        receiversReviewed.put(FIRST_USER, NOT_REVIEWED_LINKED_REQUEST);
        receiversReviewed.put(SECOND_USER, NOT_REVIEWED_LINKED_REQUEST);
        DrivingNotification drivingNotification = new DrivingNotification(
            new Route(), PRICE, new RegularUser(), STARTED, DURATION, true, true, new VehicleTypeInfo(),
            receiversReviewed, true
        );

        when(drivingNotificationRepository.findById(EXIST_OBJECT_ID))
            .thenReturn(Optional.of(drivingNotification));

        when(drivingNotificationRepository.save(any(DrivingNotification.class))).thenReturn(drivingNotification);

        DrivingNotificationDTO drivingNotificationDTO = drivingNotificationService.updateStatus(
            EXIST_OBJECT_ID,
            SECOND_USER_EMAIL,
            true
        );
        verify(drivingNotificationRepository, times(1)).save(postArgumentCaptor.capture());
        Assertions.assertEquals(drivingNotificationDTO.getPrice(), drivingNotification.getPrice());
        Assertions.assertEquals(postArgumentCaptor.getValue().getReceiversReviewed().get(FIRST_USER), NOT_REVIEWED_LINKED_REQUEST);
        Assertions.assertEquals(postArgumentCaptor.getValue().getReceiversReviewed().get(SECOND_USER), ACCEPT_DRIVING);
    }

    @Test
    @DisplayName("Should throw exception when chosen time for ride reservation is not valid")
    public void createDrivingNotification_throwInvalidChosenTimeForReservationException() throws EntityNotFoundException {
        when(vehicleTypeInfoService.isCorrectNumberOfSeats("SUV", 1)).thenReturn(true);
        RouteRequest routeRequest = createRouteRequest();
        assertThrows(InvalidChosenTimeForReservationException.class,
                () -> drivingNotificationService.createDrivingNotificationDTO(
                    routeRequest, "ana@gmail.com", 5, new ArrayList<>(), 5,
                        false, false, "SUV",
                        getInvalidTimeForReservationAfter(), true
                ));

        assertThrows(InvalidChosenTimeForReservationException.class,
                () -> drivingNotificationService.createDrivingNotificationDTO(
                        routeRequest, "ana@gmail.com", 5, new ArrayList<>(), 5,
                        false, false, "SUV",
                        getInvalidTimeForReservationBefore(), true
                ));
    }

//    @Test
//    @DisplayName("Should continue execute when chosen time for ride reservation is valid")
//    public void createDrivingNotification_validChosenTimeForReservation() throws EntityNotFoundException {
//        when(vehicleTypeInfoService.isCorrectNumberOfSeats("SUV", 1)).thenReturn(true);
//        RouteRequest routeRequest = createRouteRequest();
//        Route route = createRoute();
//        when(routeService.createRoute(
//                routeRequest.getLocations(),routeRequest.getTimeInMin(),
//                routeRequest.getDistance(), routeRequest.getRoutePathIndex())
//        ).thenReturn();
//        assertDoesNotThrow(
//                () -> drivingNotificationService.createDrivingNotificationDTO(
//                        routeRequest, "ana@gmail.com", 5, new ArrayList<>(), 5,
//                        false, false, "SUV",
//                        getValidTimeForReservation(), true
//                ));
//    }




//    @Test
//    @DisplayName("T3-Should throw excessive num of passengers exception")
//    public void createDrivingNotificationDTOThrowExcessiveNumOfPassengersException() throws EntityNotFoundException {
//
//       when(vehicleTypeInfoService.isCorrectNumberOfSeats("SUV", 6))
//               .thenReturn(false);
//        Assertions.assertThrows(ExcessiveNumOfPassengersException.class,
//                () -> drivingNotificationService.createDrivingNotificationDTO(
//                        new RouteRequest(),
//                        "",
//                        ));
//
//        verify(drivingNotificationRepository, times(0)).save(any(DrivingNotification.class));
//    }

    private LocalDateTime getInvalidTimeForReservationAfter(){
        return LocalDateTime.now().plusHours(6);
    }

    private LocalDateTime getInvalidTimeForReservationBefore(){
        return LocalDateTime.now().plusMinutes(25);
    }

    private LocalDateTime getValidTimeForReservation(){
        return LocalDateTime.now().plusHours(1);
    }

    private RouteRequest createRouteRequest(){
        LocationRequest startLocation = new LocationRequest(
                "Novi Sad", "Bulevar Oslobodjenja",
                "55", "21000",
                19.833950, 45.258300
        );

        LocationRequest endLocation = new LocationRequest(
                "Novi Sad", "Bulevar Cara Lazara",
                "5", "21000",
                19.848760, 45.246710
        );
        DrivingLocationIndexRequest startLocationIndex = new DrivingLocationIndexRequest(startLocation, 1);
        DrivingLocationIndexRequest endLocationIndex = new DrivingLocationIndexRequest(endLocation, 2);
        List<DrivingLocationIndexRequest> locations = new ArrayList<>();
        locations.add(startLocationIndex);
        locations.add(endLocationIndex);
        List<Integer> routeIndexList = new ArrayList<>();
        routeIndexList.add(0);
        routeIndexList.add(0);

        return new RouteRequest(5, 2000, locations, routeIndexList );
    }

//    private Route createRoute(List<LocationRequest> locations, double timeinMin, double distance){
//
//        return new Route(,timeinMin, distance);
//    }




}
