package com.example.serbUber.service;

import com.example.serbUber.dto.DrivingDTO;
import com.example.serbUber.dto.DrivingNotificationDTO;
import com.example.serbUber.dto.DrivingStatusNotificationDTO;
import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.exception.EntityType;
import com.example.serbUber.model.*;
import com.example.serbUber.model.user.Driver;
import com.example.serbUber.model.user.RegularUser;
import com.example.serbUber.model.user.User;
import com.example.serbUber.repository.DrivingNotificationRepository;
import com.example.serbUber.request.DrivingLocationIndexRequest;
import com.example.serbUber.service.interfaces.IDrivingNotificationService;
import com.example.serbUber.service.user.DriverService;
import com.example.serbUber.service.user.RegularUserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;

@Component
@Qualifier("drivingNotificationServiceConfiguration")
public class DrivingNotificationService implements IDrivingNotificationService {
    private final DrivingNotificationRepository drivingNotificationRepository;
    private final RegularUserService regularUserService;
    private final WebSocketService webSocketService;
    private final VehicleService vehicleService;
    private final DriverService driverService;
    private final DrivingService drivingService;
    private final RouteService routeService;


    public DrivingNotificationService(
        final DrivingNotificationRepository drivingNotificationRepository,
        final RegularUserService regularUserService,
        final WebSocketService webSocketService,
        final VehicleService vehicleService,
        final DriverService driverService,
        final DrivingService drivingService,
        final RouteService routeService
    ){
        this.drivingNotificationRepository = drivingNotificationRepository;
        this.regularUserService = regularUserService;
        this.webSocketService = webSocketService;
        this.vehicleService = vehicleService;
        this.driverService = driverService;
        this.drivingService = drivingService;
        this.routeService = routeService;
    }

    public DrivingNotificationDTO createDrivingNotificationDTO(
            final List<DrivingLocationIndexRequest> locations,
            final String senderEmail,
            final double price,
            final List<String> passengers,
            final LocalDateTime started,
            final int duration,
            final boolean babySeat,
            final boolean petFriendly,
            final String vehicleType,
            final double time,
            final double distance
    ) throws EntityNotFoundException {
        RegularUser sender = regularUserService.getRegularByEmail(senderEmail);
        Set<RegularUser> receivers = new HashSet<>();
        passengers.forEach(passengerEmail -> {
            try {
                receivers.add(regularUserService.getRegularByEmail(passengerEmail));
            } catch (EntityNotFoundException e) {
                throw new RuntimeException(e); ////???
            }
        });
        //TODO:
        Vehicle selectedVehicle = vehicleService.getVehicleByType(vehicleType);
        Route route = routeService.createRoute(locations, time, distance);
        DrivingNotification notification = createDrivingNotification(
                route, price, receivers, sender, started, duration, babySeat, petFriendly, selectedVehicle
        );
        if(passengers.size() > 0){
            webSocketService.sendDrivingNotification(notification);
        }
        else{
            shouldFindDriver(notification);
        }

        return new DrivingNotificationDTO(notification);
    }

    public DrivingNotificationDTO acceptDriving(final Long id) throws EntityNotFoundException {
        DrivingNotification drivingNotification = drivingNotificationRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(id, EntityType.DRIVING_NOTIFICATION));
        drivingNotification.setAnsweredPassengers(drivingNotification.getAnsweredPassengers()+1);
        drivingNotificationRepository.save(drivingNotification);
        shouldFindDriver(drivingNotification);
        return new DrivingNotificationDTO(drivingNotification);
    }

    private boolean allPassengersAcceptDriving(DrivingNotification drivingNotification){

        return drivingNotification.getAnsweredPassengers() == drivingNotification.getReceivers().size();
    }

    public void shouldFindDriver(DrivingNotification drivingNotification) throws EntityNotFoundException {
        if(allPassengersAcceptDriving(drivingNotification)){
            Driver driver = driverService.getDriverForDriving(drivingNotification);
            if(driver == null){
                DrivingStatusNotificationDTO drivingStatusNotificationDTO = new DrivingStatusNotificationDTO(0L, 0, DrivingStatus.PENDING,"Not found driver", drivingNotification.getId());
                webSocketService.sendDrivingStatus(drivingStatusNotificationDTO, drivingNotification.getReceivers(), drivingNotification.getSender().getEmail());
                return;
            }
            DrivingDTO drivingDTO = drivingService.create(
                    drivingNotification.getDuration(),
                    drivingNotification.getStarted(),
                    drivingNotification.getStarted().plusMinutes(2),
                    drivingNotification.getRoute(),
                    DrivingStatus.PAYING,
                    driver.getId(),
                    drivingNotification.getReceivers(),
                    new HashMap<>(),
                    drivingNotification.getPrice());
            //poziv placanja

        }
    }




    public DrivingNotificationDTO getDrivingNotification(Long id) throws EntityNotFoundException {
        DrivingNotification drivingNotification =  drivingNotificationRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(id, EntityType.DRIVING_NOTIFICATION));

        return new DrivingNotificationDTO(drivingNotification);
    }

    private DrivingNotification createDrivingNotification(
        final Route route,
        final double price,
        final Set<RegularUser> receivers,
        final User sender,
        final LocalDateTime started,
        final int duration,
        final boolean babySeat,
        final boolean petFriendly,
        final Vehicle vehicle
    )
    {

        return drivingNotificationRepository.save(
            new DrivingNotification(route, price, sender, started, duration, babySeat, petFriendly, vehicle, receivers)
        );
    }
}
