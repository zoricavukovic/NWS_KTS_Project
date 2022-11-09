package com.example.serbUber.controller;

import com.example.serbUber.dto.ReviewDTO;
import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.request.ReviewRequest;
import com.example.serbUber.service.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import java.util.List;

import static com.example.serbUber.exception.ErrorMessagesConstants.WRONG_EMAIL;


@RestController
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(final ReviewService reviewService) {
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
            reviewRequest.getUserEmail()

        );
    }

    @GetMapping("/reviewedDrivings/{email}")
    @ResponseStatus(HttpStatus.OK)
    public List<Long> getReviewDrivingsForUser(@Valid @Email(message = WRONG_EMAIL) @PathVariable String email){
        return reviewService.getAllReviewedDrivingIdForUser(email);
    }
}
