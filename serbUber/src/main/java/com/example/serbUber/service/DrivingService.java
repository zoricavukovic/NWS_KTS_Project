package com.example.serbUber.service;

import com.example.serbUber.dto.DrivingDTO;
import com.example.serbUber.model.Driving;
import com.example.serbUber.model.DrivingStatus;
import com.example.serbUber.model.Route;
import com.example.serbUber.repository.DrivingRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

import static com.example.serbUber.dto.DrivingDTO.fromDrivings;

@Service
public class DrivingService {

    private final DrivingRepository drivingRepository;


    public DrivingService(final DrivingRepository drivingRepository) {
        this.drivingRepository = drivingRepository;
    }

    public void create(
            final boolean active,
            final int duration,
            final LocalDateTime started,
            final LocalDateTime payingLimit,
            final Route route,
            final DrivingStatus drivingStatus,
            final String driverEmail,
            final HashMap<String, Boolean> usersPaid,
            final double price
    ) {

        drivingRepository.save(new Driving(
                active,
                duration,
                started,
                payingLimit,
                route,
                drivingStatus,
                driverEmail,
                usersPaid,
                price
        ));
    }

    public List<DrivingDTO> getAll() {
        List<Driving> drivings = drivingRepository.findAll();

        return fromDrivings(drivings);
    }
}
