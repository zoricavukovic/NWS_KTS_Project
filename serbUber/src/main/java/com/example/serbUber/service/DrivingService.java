package com.example.serbUber.service;

import com.example.serbUber.dto.DrivingDTO;
import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.exception.EntityType;
import com.example.serbUber.model.Driving;
import com.example.serbUber.model.DrivingStatus;
import com.example.serbUber.model.Route;
import com.example.serbUber.model.user.User;
import com.example.serbUber.repository.DrivingRepository;
import com.example.serbUber.repository.user.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static com.example.serbUber.dto.DrivingDTO.fromDrivings;

@Service
public class DrivingService {

    private final DrivingRepository drivingRepository;
    private final UserRepository userRepository;

    public DrivingService(final DrivingRepository drivingRepository, final UserRepository userRepository) {
        this.drivingRepository = drivingRepository;
        this.userRepository = userRepository;
    }

    public DrivingDTO create(
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

        Driving driving = drivingRepository.save(new Driving(
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

        return new DrivingDTO(driving);
    }

    public List<DrivingDTO> getAll() {
        List<Driving> drivings = drivingRepository.findAll();

        return fromDrivings(drivings);
    }

    public List<DrivingDTO> getDrivingsForUser(String email) throws EntityNotFoundException {

            List<Driving> drivings = drivingRepository.findByUserEmail(email);
            return fromDrivings(drivings);
    }
}
