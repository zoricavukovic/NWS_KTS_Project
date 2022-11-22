package com.example.serbUber.service;

import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.model.DrivingNotification;
import com.example.serbUber.model.Route;
import com.example.serbUber.model.user.RegularUser;
import com.example.serbUber.repository.DrivingNotificationRepository;
import com.example.serbUber.request.RouteRequest;
import com.example.serbUber.service.user.RegularUserService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class DrivingNotificationService {
    private final DrivingNotificationRepository drivingNotificationRepository;
    private final RegularUserService regularUserService;

    public DrivingNotificationService(final DrivingNotificationRepository drivingNotificationRepository, final RegularUserService regularUserService){
        this.drivingNotificationRepository = drivingNotificationRepository;
        this.regularUserService = regularUserService;
    }

    public void create(
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

        drivingNotificationRepository.save(new DrivingNotification(lonStarted, latStarted, lonEnd, latEnd, price, sender, users));


    }

}
