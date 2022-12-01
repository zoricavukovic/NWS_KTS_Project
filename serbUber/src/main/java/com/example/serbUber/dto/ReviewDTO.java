package com.example.serbUber.dto;

import com.example.serbUber.model.Driving;
import com.example.serbUber.model.Notification;
import com.example.serbUber.model.Review;
import com.example.serbUber.model.user.RegularUser;

import java.util.LinkedList;
import java.util.List;

import static com.example.serbUber.util.PictureHandler.convertPictureToBase64ByName;

public class ReviewDTO {

    private Long id;
    private double vehicleRate;
    private double driverRate;
    private String message;
    private Driving driving;
    private RegularUser sender;

    public ReviewDTO(final Review review) {
        this.id = review.getId();
        this.vehicleRate = review.getVehicleRate();
        this.driverRate = review.getDriverRate();
        this.message = review.getMessage();
        this.driving = review.getDriving();
        this.sender = review.getSender();
    }

    public static List<ReviewDTO> fromReviews(List<Review> reviews) {
        List<ReviewDTO> dtos = new LinkedList<>();
        reviews.forEach(review -> {
                review.getSender().setProfilePicture(convertPictureToBase64ByName(review.getSender().getProfilePicture()));
                dtos.add(new ReviewDTO(review));
        });

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

    public RegularUser getSender() {
        return sender;
    }
}
