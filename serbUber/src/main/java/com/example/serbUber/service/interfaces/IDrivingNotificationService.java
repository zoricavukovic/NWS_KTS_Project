package com.example.serbUber.service.interfaces;

import com.example.serbUber.dto.DrivingNotificationDTO;
import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.model.DrivingNotificationType;
import com.example.serbUber.model.Location;
import com.example.serbUber.model.user.RegularUser;
import com.example.serbUber.model.user.User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
public interface IDrivingNotificationService {

    List<DrivingNotificationDTO> createNotifications( ///driving dto
                        final double lonStarted,
                        final double latStarted,
                        final double lonEnd,
                        final double latEnd,
                        final String senderEmail,
                        final double price,
                        final List<String> passengers,
                        final LocalDateTime started,
                        final int duration
    ) throws EntityNotFoundException;

    DrivingNotificationDTO setDrivingNotificationAnswered(final Long id) throws EntityNotFoundException;

    List<DrivingNotificationDTO> createNotifications(
        final Location startLocation,
        final Location destination,
        final double price,
        final User sender,
        final Set<RegularUser> receivers,
        final DrivingNotificationType rejectDriving,
        final String reason
    );
}
