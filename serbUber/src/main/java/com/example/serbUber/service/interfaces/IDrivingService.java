package com.example.serbUber.service.interfaces;

import com.example.serbUber.dto.DrivingDTO;
import com.example.serbUber.dto.DrivingPageDTO;
import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.model.Driving;
import com.example.serbUber.model.DrivingStatus;
import com.example.serbUber.model.Route;
import com.example.serbUber.model.user.RegularUser;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public interface IDrivingService {
    DrivingDTO create(
        final int duration,
        final LocalDateTime started,
        final LocalDateTime payingLimit,
        final Route route,
        final DrivingStatus drivingStatus,
        final Long driverId,
        final Set<RegularUser> users,
        final HashMap<Long, Boolean> usersPaid,
        final double price
    );

    List<DrivingDTO> getAll();

    List<DrivingPageDTO> getDrivingsForUser(
        final Long id,
        final int pageNumber,
        final int pageSize,
        final String parameter,
        final String sortOrder
    ) throws EntityNotFoundException;
    DrivingDTO getDrivingDto(final Long id) throws EntityNotFoundException;
    Driving getDriving(final Long id) throws EntityNotFoundException;
    List<DrivingDTO> getAllNowAndFutureDrivings(final Long id);


    DrivingDTO rejectDriving(final Long id, String reason) throws EntityNotFoundException;

    DrivingDTO startDriving(final Long id) throws EntityNotFoundException;

    DrivingDTO paidDriving(final Long id) throws EntityNotFoundException;

    DrivingDTO removeDriver(Long id) throws EntityNotFoundException;
}
