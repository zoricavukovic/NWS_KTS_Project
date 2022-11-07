package com.example.serbUber.dto;

import com.example.serbUber.model.Driving;
import com.example.serbUber.model.Notification;
import com.example.serbUber.model.Review;

import java.util.LinkedList;
import java.util.List;

public class ReviewDTO {

    private Long id;
    private double vehicleRate;
    private double driverRate;
    private String message;
    private Driving driving;

    public ReviewDTO(final Review review) {
        this.id = review.getId();
        this.vehicleRate = review.getVehicleRate();
        this.driverRate = review.getDriverRate();
        this.message = review.getMessage();
        this.driving = review.getDriving();
    }

    public static List<ReviewDTO> fromReviews(List<Review> reviews) {
        List<ReviewDTO> dtos = new LinkedList<>();
        reviews.forEach(review ->
                dtos.add(new ReviewDTO(review))
        );

        return dtos;
    }

    public double getVehicleRate() {
        return vehicleRate;
    }

    public double getDriverRate() {
        return driverRate;
    }

    public String getMessage() {
        return message;
    }

    public Driving getDriving() {
        return driving;
    }

    public Long getId() {
        return id;
    }

    public static int getNumberOfReviewsBeforeUpdate(List<ReviewDTO> reviews){
        return reviews.size() - 1;
    }

}
