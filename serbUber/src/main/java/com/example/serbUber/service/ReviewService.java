package com.example.serbUber.service;

import com.example.serbUber.dto.ReviewDTO;
import com.example.serbUber.model.Review;
import com.example.serbUber.repository.ReviewRepository;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.serbUber.dto.ReviewDTO.fromReviews;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewService(final ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }


    public void create(
            final double vehicleRate,
            final double driverRate,
            final String message,
            final String driving) {

        //drivingService da se doda i findBy id onda
        reviewRepository.save((new Review(
            vehicleRate,
            driverRate,
            message,
            null
        )));
    }

    public List<ReviewDTO> getAll() {
        List<Review> reviews = reviewRepository.findAll();

        return fromReviews(reviews);
    }

    public void delete(String id) {

        reviewRepository.deleteById(id);
    }
}
