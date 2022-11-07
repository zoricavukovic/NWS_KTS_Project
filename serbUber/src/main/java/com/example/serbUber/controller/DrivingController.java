package com.example.serbUber.controller;

import com.example.serbUber.dto.DrivingDTO;
import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.model.Driving;
import com.example.serbUber.request.DrivingRequest;
import com.example.serbUber.service.DrivingService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import java.util.LinkedList;
import java.util.List;

import static com.example.serbUber.exception.ErrorMessagesConstants.WRONG_EMAIL;

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

    @GetMapping("/{email}")
    @ResponseStatus(HttpStatus.OK)
    public List<DrivingDTO> getAllDrivingsForUser(@Valid @Email(message = WRONG_EMAIL) @PathVariable String email)
            throws EntityNotFoundException
    {

        return drivingService.getDrivingsForUser(email);
    }

    @GetMapping("/details/{id}")
    @ResponseStatus(HttpStatus.OK)
    public DrivingDTO getDriving(@PathVariable Long id) throws EntityNotFoundException {
        return drivingService.getDrivingDto(id);
    }
}
