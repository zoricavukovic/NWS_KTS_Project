package com.example.serbUber.service.interfaces;

import com.example.serbUber.dto.DrivingDTO;
import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.model.Driving;
import com.example.serbUber.model.DrivingStatus;
import com.example.serbUber.model.Route;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

public interface IDrivingService {
    DrivingDTO create(
            final boolean active,
            final int duration,
            final LocalDateTime started,
            final LocalDateTime payingLimit,
            final Route route,
            final DrivingStatus drivingStatus,
            final Long driverId,
            final HashMap<Long, Boolean> usersPaid,
            final double price
    );

    List<DrivingDTO> getAll();

    List<DrivingDTO> getDrivingsForUser(Long id, int pageNumber, int pageSize, String parameter, String sortOrder) throws EntityNotFoundException;
    DrivingDTO getDrivingDto(Long id) throws EntityNotFoundException;
    Driving getDriving(Long id) throws EntityNotFoundException;
    List<DrivingDTO> getAllNowAndFutureDrivings(Long id);


}
