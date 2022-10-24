package com.example.serbUber.controller;

import com.example.serbUber.dto.ReviewDTO;
import com.example.serbUber.request.ReviewRequest;
import com.example.serbUber.service.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/reviews")
public class ReviewController {

    private ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@Valid @RequestBody ReviewRequest reviewRequest) {

        this.reviewService.create(
            reviewRequest.getVehicleRate(),
            reviewRequest.getDriverRate(),
            reviewRequest.getMessage(),
            reviewRequest.getDriving()
        );
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<ReviewDTO> getAll() {

        return this.reviewService.getAll();
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable String id) {

        this.reviewService.delete(id);
    }

}
