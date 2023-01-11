package com.example.serbUber.controller;

import com.example.serbUber.dto.DrivingDTO;
import com.example.serbUber.dto.DrivingPageDTO;
import com.example.serbUber.dto.SimpleDrivingInfoDTO;
import com.example.serbUber.dto.VehicleCurrentLocationDTO;
import com.example.serbUber.exception.DriverAlreadyHasStartedDrivingException;
import com.example.serbUber.exception.DrivingShouldNotStartYetException;
import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.request.DrivingRequest;
import com.example.serbUber.service.DrivingService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;

import static com.example.serbUber.exception.ErrorMessagesConstants.*;
import static com.example.serbUber.util.Constants.NUM_OF_LETTERS_REASON_TOO_LONG;

@RestController
@RequestMapping("/drivings")
public class DrivingController {

    private final DrivingService drivingService;

    public DrivingController(@Qualifier("drivingServiceConfiguration") final DrivingService drivingService) {
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
    public List<DrivingPageDTO> getAllDrivingsForUserWithPaginationAndSort(
            @Valid @NotNull(message = NOT_NULL_MESSAGE) @PathVariable Long id,
            @Valid @NotNull(message = NOT_NULL_MESSAGE) @PositiveOrZero(message = POSITIVE_OR_ZERO_MESSAGE) @PathVariable int pageNumber,
            @Valid @NotNull(message = NOT_NULL_MESSAGE) @Positive(message = POSITIVE_MESSAGE) @PathVariable int pageSize,
            @Valid @NotNull(message = NOT_NULL_MESSAGE) @PathVariable String parameter,
            @Valid @NotNull(message = NOT_NULL_MESSAGE) @PathVariable String sortOrder
        ) throws EntityNotFoundException
    {

        return drivingService.getDrivingsForUser(id, pageNumber, pageSize, parameter, sortOrder);
    }

//    @GetMapping("/number/{id}")
//    @ResponseStatus(HttpStatus.OK)
//    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DRIVER', 'ROLE_REGULAR_USER')")
//    public int getNumberOfAllDrivingsForUser(@PathVariable Long id) throws EntityNotFoundException {
//        return drivingService.getNumberOfAllDrivingsForUser(id);
//    }

    @GetMapping("/now-and-future/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_DRIVER')")
    public List<DrivingDTO> getAllNowAndFutureDrivings(
        @Valid @NotNull(message = NOT_NULL_MESSAGE)@PathVariable Long id
    ){

        return drivingService.getAllNowAndFutureDrivings(id);
    }



    @GetMapping("/{id}")
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

    @PutMapping("/start/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_DRIVER')")
    public DrivingDTO startDriving(@PathVariable Long id) throws EntityNotFoundException, DriverAlreadyHasStartedDrivingException, DrivingShouldNotStartYetException {

        return drivingService.startDriving(id);
    }

    @PutMapping("/reject/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_DRIVER')")
    public DrivingDTO rejectDriving(
        @PathVariable Long id,
        @Valid @NotBlank(message="Reason must be added")
        @Size(max=NUM_OF_LETTERS_REASON_TOO_LONG, message=REASON_TOO_LONG) @RequestBody String reason
    ) throws EntityNotFoundException {

        return drivingService.rejectDriving(id, reason);
    }

    @GetMapping("/has-active/user/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_REGULAR_USER')")
    public SimpleDrivingInfoDTO checkUserHasActiveDriving(@Valid @NotNull(message = NOT_NULL_MESSAGE)@PathVariable final Long id){

        return drivingService.checkUserHasActiveDriving(id);
    }

    @GetMapping("/vehicle-current-location/{drivingId}")
    @ResponseStatus(HttpStatus.OK)
    public VehicleCurrentLocationDTO getVehicleCurrentLocation(@Valid @NotNull(message = MISSING_ID) @PathVariable Long drivingId) throws EntityNotFoundException {

        return drivingService.getVehicleCurrentLocation(drivingId);
    }
}
