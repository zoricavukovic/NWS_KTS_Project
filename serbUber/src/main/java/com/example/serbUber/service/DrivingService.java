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

    public DrivingService(final DrivingRepository drivingRepository) {
        this.drivingRepository = drivingRepository;
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

    public List<DrivingDTO> getDrivingsForUser(String email) {

            List<Driving> drivings = drivingRepository.findByUserEmail(email);
            return fromDrivings(drivings);
    }

    public DrivingDTO getDriving(Long id) throws EntityNotFoundException {
//        Optional<Driving> driving = drivingRepository.getDrivingById(id);
//        if (driving.isPresent()) {
//            return new DrivingDTO(driving.get());
//        }
//        else {
//            throw new EntityNotFoundException(id, EntityType.USER);
//        }
        Driving driving = drivingRepository.getDrivingById(id);
        return new DrivingDTO(driving);
    }
}
