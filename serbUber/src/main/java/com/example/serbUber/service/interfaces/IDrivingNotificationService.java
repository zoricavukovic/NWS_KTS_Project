package com.example.serbUber.service.interfaces;

import com.example.serbUber.exception.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public interface IDrivingNotificationService {

    void create( ///driving dto
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

    int setDrivingNotificationAnswered(Long id) throws EntityNotFoundException;


}
