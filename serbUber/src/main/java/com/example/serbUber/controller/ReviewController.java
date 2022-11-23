package com.example.serbUber.controller;

import com.example.serbUber.dto.ReviewDTO;
import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.request.ReviewRequest;
import com.example.serbUber.service.ReviewService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;



@RestController
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(@Qualifier("reviewServiceConfiguration") final ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<ReviewDTO> getAll() {

        return this.reviewService.getAll();
    }


    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable Long id) {

        this.reviewService.delete(id);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ReviewDTO create(@Valid @RequestBody ReviewRequest reviewRequest) throws EntityNotFoundException {
        return this.reviewService.create(
            reviewRequest.getVehicleRate(),
            reviewRequest.getDriverRate(),
            reviewRequest.getMessage(),
            reviewRequest.getDriving(),
            reviewRequest.getUserId()

        );
    }

    @GetMapping("/reviewedDrivings/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DRIVER', 'ROLE_REGULAR_USER')")
    public List<Long> getReviewDrivingsForUser(@PathVariable Long id){
        return reviewService.getAllReviewedDrivingIdForUser(id);
    }
}
