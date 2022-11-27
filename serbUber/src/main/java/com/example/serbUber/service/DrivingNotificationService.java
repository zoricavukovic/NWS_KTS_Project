package com.example.serbUber.service;

import com.example.serbUber.dto.DrivingNotificationDTO;
import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.exception.EntityType;
import com.example.serbUber.model.DrivingNotification;
import com.example.serbUber.model.Route;
import com.example.serbUber.model.user.RegularUser;
import com.example.serbUber.repository.DrivingNotificationRepository;
import com.example.serbUber.request.RouteRequest;
import com.example.serbUber.service.interfaces.IDrivingNotificationService;
import com.example.serbUber.service.user.RegularUserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Component
@Qualifier("drivingNotificationServiceConfiguration")
public class DrivingNotificationService implements IDrivingNotificationService {
    private final DrivingNotificationRepository drivingNotificationRepository;
    private final RegularUserService regularUserService;

    public DrivingNotificationService(final DrivingNotificationRepository drivingNotificationRepository, final RegularUserService regularUserService){
        this.drivingNotificationRepository = drivingNotificationRepository;
        this.regularUserService = regularUserService;
    }

    public void create( ///driving dto
            final double lonStarted,
            final double latStarted,
            final double lonEnd,
            final double latEnd,
            final String senderEmail,
            final double price,
            final List<String> passengers
    ) throws EntityNotFoundException {
        RegularUser sender = regularUserService.getRegularByEmail(senderEmail);
        Set<RegularUser> users = new HashSet<>();
        for(String email : passengers){
            users.add(regularUserService.getRegularByEmail(email));
        }

        drivingNotificationRepository.save(new DrivingNotification(lonStarted, latStarted, lonEnd, latEnd, price, sender, users,0));


    }

    public int setDrivingNotificationAnswered(Long id) throws EntityNotFoundException {
        Optional<DrivingNotification> drivingNotification = drivingNotificationRepository.findById(id);
        if(drivingNotification.isPresent()){
            drivingNotification.get().setAnsweredPassengers(drivingNotification.get().getAnsweredPassengers()+1);
            drivingNotificationRepository.save(drivingNotification.get());
            return drivingNotification.get().getAnsweredPassengers();
        }
        throw new EntityNotFoundException(id, EntityType.DRIVING); ///niej drivingg
    }

    public DrivingNotificationDTO getDrivingNotification(Long id) throws EntityNotFoundException {
        Optional<DrivingNotification> drivingNotification = drivingNotificationRepository.findById(id);
        if(drivingNotification.isPresent()){
            return new DrivingNotificationDTO(drivingNotification.get());
        }
        throw new EntityNotFoundException(id, EntityType.DRIVING_NOTIFICATION);
    }
}
