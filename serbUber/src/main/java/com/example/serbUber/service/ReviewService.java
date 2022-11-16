package com.example.serbUber.service;

import com.example.serbUber.dto.ReviewDTO;
import com.example.serbUber.dto.user.DriverDTO;
import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.model.Driving;
import com.example.serbUber.model.Review;
import com.example.serbUber.model.user.RegularUser;
import com.example.serbUber.repository.ReviewRepository;
import com.example.serbUber.service.user.DriverService;
import com.example.serbUber.service.user.RegularUserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.example.serbUber.dto.ReviewDTO.fromReviews;
import static com.example.serbUber.dto.ReviewDTO.getNumberOfReviewsBeforeUpdate;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final DrivingService drivingService;

    private final DriverService driverService;

    private final VehicleService vehicleService;

    private final RegularUserService regularUserService;

    public ReviewService(final ReviewRepository reviewRepository, final DrivingService drivingService, final DriverService driverService, final VehicleService vehicleService, final RegularUserService regularUserService) {
        this.reviewRepository = reviewRepository;
        this.drivingService = drivingService;
        this.driverService = driverService;
        this.vehicleService = vehicleService;
        this.regularUserService = regularUserService;
    }


    public ReviewDTO create(
            final double vehicleRate,
            final double driverRate,
            final String message,
            final Long drivingId,
            final Long userId
    ) throws EntityNotFoundException {
        Driving driving = drivingService.getDriving(drivingId);
        RegularUser regularUser = regularUserService.getRegularById(userId);

        Review review = reviewRepository.save((new Review(
            vehicleRate,
            driverRate,
            message,
            driving,
            regularUser
        )));
        updateRate(driving, vehicleRate, driverRate);

        return new ReviewDTO(review);
    }

    private void updateRate(Driving driving, double vehicleRate, double driverRate)
            throws EntityNotFoundException
    {
        DriverDTO driverDTO = driverService.get(driving.getDriverId());
        List<ReviewDTO> reviews = getAllForDriver(driving.getDriverId());
        int numberOfReviewsBeforeUpdate = getNumberOfReviewsBeforeUpdate(reviews);
        calculateRateDriver(driverDTO, numberOfReviewsBeforeUpdate, driverRate);
        calculateRateVehicle(vehicleRate, driverDTO, numberOfReviewsBeforeUpdate);
    }

    private void calculateRateVehicle(double vehicleRate, DriverDTO driverDTO, int numberOfReviewsBeforeUpdate)
            throws EntityNotFoundException
    {
        double updatedRateVehicle = driverDTO.getVehicle().getRate() * numberOfReviewsBeforeUpdate + vehicleRate;
        vehicleService.updateRate(driverDTO.getVehicle().getId(), updatedRateVehicle);
    }

    public void calculateRateDriver(DriverDTO driverDTO, int numberOfReviews, double driverRate)
            throws EntityNotFoundException
    {
        double updatedRateDriver = driverDTO.getRate() * numberOfReviews + driverRate;
        driverService.updateRate(driverDTO.getId(), updatedRateDriver);
    }


    public List<ReviewDTO> getAllForDriver(Long id) {
        List<Review> reviews = reviewRepository.findAllByDriverId(id);

       return fromReviews(reviews);
    }

    public List<Long> getAllReviewedDrivingIdForUser(Long id){
        List<Long> reviewedDrivingsId = new ArrayList<>();
        List<Review> allReviewsForUser = reviewRepository.findAllReviewedDrivingIdForUser(id);
        for(Review review : allReviewsForUser){
            reviewedDrivingsId.add(review.getDriving().getId());
        }
        return reviewedDrivingsId;
    }

    public List<ReviewDTO> getAll() {
        List<Review> reviews = reviewRepository.findAll();

        return fromReviews(reviews);
    }

    public void delete(Long id) {

        reviewRepository.deleteById(id);
    }

}
