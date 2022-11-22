package com.example.serbUber.service.interfaces;

import com.example.serbUber.dto.ReviewDTO;
import com.example.serbUber.exception.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IReviewService {

   ReviewDTO create(
            final double vehicleRate,
            final double driverRate,
            final String message,
            final Long drivingId,
            final Long userId
    ) throws EntityNotFoundException;

    List<ReviewDTO> getAllForDriver(Long id);

    List<Long> getAllReviewedDrivingIdForUser(Long id);

    List<ReviewDTO> getAll();
    void delete(Long id);

}
