package com.example.serbUber.service;

import com.example.serbUber.dto.DrivingDTO;
import com.example.serbUber.dto.ReviewDTO;
import com.example.serbUber.dto.user.DriverDTO;
import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.model.Driving;
import com.example.serbUber.model.Review;
import com.example.serbUber.model.user.Driver;
import com.example.serbUber.repository.ReviewRepository;
import com.example.serbUber.service.user.DriverService;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.serbUber.dto.ReviewDTO.fromReviews;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final DrivingService drivingService;

    private final DriverService driverService;

    private final VehicleService vehicleService;

    public ReviewService(final ReviewRepository reviewRepository, final DrivingService drivingService, final DriverService driverService, final VehicleService vehicleService) {
        this.reviewRepository = reviewRepository;
        this.drivingService = drivingService;
        this.driverService = driverService;
        this.vehicleService = vehicleService;
    }


    public Review create(
            final double vehicleRate,
            final double driverRate,
            final String message,
            final Long drivingId) throws EntityNotFoundException {

        Driving driving = drivingService.getDriving(drivingId);

        Review review = reviewRepository.save((new Review(
            vehicleRate,
            driverRate,
            message,
            driving
        )));
        updateRate(driving);

        return review;
    }

    private void updateRate(Driving driving) throws EntityNotFoundException {
        DriverDTO driverDTO = driverService.get(driving.getDriverEmail());
        List<ReviewDTO> reviews = getAllForDriver(driving.getDriverEmail());
        double rateVehicle = 0;
        double rateDriver = 0;
        int numberReview = reviews.size();
        for(ReviewDTO review : reviews){
            rateVehicle += review.getVehicleRate();
            rateDriver += review.getDriverRate();
        }
        rateVehicle = rateVehicle / numberReview;
        rateDriver = rateDriver / numberReview;

        driverService.updateRate(driverDTO.getId(), rateDriver);
        vehicleService.updateRate(driverDTO.getVehicle().getId(), rateVehicle);

    }

    public List<ReviewDTO> getAllForDriver(String email) {
        List<Review> reviews = reviewRepository.findAllByDriverEmail(email);

       return fromReviews(reviews);
    }

    public List<ReviewDTO> getAll() {
        List<Review> reviews = reviewRepository.findAll();

        return fromReviews(reviews);
    }

    public void delete(Long id) {

        reviewRepository.deleteById(id);
    }

    public boolean haveDrivingRate(Long id){
        Review review = reviewRepository.findByDrivingId(id);
        //da li sa onim notfoundexception??
        if(reviewRepository.findByDrivingId(id) != null){
            return true;
        }

        return false;
    }
}
