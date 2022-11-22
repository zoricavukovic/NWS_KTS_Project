package com.example.serbUber.controller;

import com.example.serbUber.dto.DrivingDTO;
import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.model.Driving;
import com.example.serbUber.request.DrivingRequest;
import com.example.serbUber.service.DrivingService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.LinkedList;
import java.util.List;

import static com.example.serbUber.exception.ErrorMessagesConstants.*;

@RestController
@RequestMapping("/drivings")
public class DrivingController {

    private final DrivingService drivingService;

    public DrivingController(final DrivingService drivingService) {
        this.drivingService = drivingService;
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<DrivingDTO> getAll() {

        return this.drivingService.getAll();
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public DrivingDTO create(@Valid @RequestBody DrivingRequest drivingRequest) {
        /*
        return this.drivingService.create(
              drivingRequest.isActive(),
                drivingRequest.getDuration(),
                drivingRequest.getStarted(),
                drivingRequest.getPayingLimit(),
                drivingRequest.getRoute(),
                drivingRequest.getDrivingStatus(),
                drivingRequest.getDriverEmail(),
                drivingRequest.getUsersPaid(),
                drivingRequest.getPrice()
        ); */
        return null;
    }

    @GetMapping("/{id}/{pageNumber}/{pageSize}/{parameter}/{sortOrder}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DRIVER', 'ROLE_REGULAR_USER')")
    public List<DrivingDTO> getAllDrivingsForUserWithPaginationAndSort(
            @Valid @NotNull(message = NOT_NULL_MESSAGE) @PathVariable Long id,
            @Valid @NotNull(message = NOT_NULL_MESSAGE) @PositiveOrZero(message = POSITIVE_OR_ZERO_MESSAGE) @PathVariable int pageNumber,
            @Valid @NotNull(message = NOT_NULL_MESSAGE) @Positive(message = POSITIVE_MESSAGE) @PathVariable int pageSize,
            @Valid @NotNull(message = NOT_NULL_MESSAGE) @PathVariable String parameter,
            @Valid @NotNull(message = NOT_NULL_MESSAGE) @PathVariable String sortOrder
        ) throws EntityNotFoundException
    {

        return drivingService.getDrivingsForUser(id, pageNumber, pageSize, parameter, sortOrder);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_DRIVER')")
    public List<DrivingDTO> getAllNowAndFutureDrivings(
        @Valid @NotNull(message = NOT_NULL_MESSAGE)@PathVariable Long id
    ){

        return drivingService.getAllNowAndFutureDrivings(id);
    }

    @GetMapping("/details/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DRIVER', 'ROLE_REGULAR_USER')")
    public DrivingDTO getDriving(@PathVariable Long id) throws EntityNotFoundException {

        return drivingService.getDrivingDto(id);
    }

    @PutMapping("/finish-driving/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_DRIVER')")
    public DrivingDTO finishDriving(@PathVariable Long id) throws EntityNotFoundException {
        
        return drivingService.finishDriving(id);
    }
}
