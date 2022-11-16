package com.example.serbUber.controller.user;

import com.example.serbUber.dto.user.DriverDTO;
import com.example.serbUber.dto.user.UserDTO;
import com.example.serbUber.exception.EntityAlreadyExistsException;
import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.exception.MailCannotBeSentException;
import com.example.serbUber.exception.PasswordsDoNotMatchException;
import com.example.serbUber.request.user.DriverRegistrationRequest;
import com.example.serbUber.service.user.DriverService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

import static com.example.serbUber.exception.ErrorMessagesConstants.NOT_NULL_MESSAGE;

@RestController
@RequestMapping("/drivers")
public class DriverController {

    private final DriverService driverService;

    public DriverController(DriverService driverService) {
        this.driverService = driverService;
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<DriverDTO> getAll() {

        return driverService.getAll();
    }

    @GetMapping("/{id}")
//    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DRIVER', 'ROLE_REGULAR_USER')")
    @ResponseStatus(HttpStatus.OK)
    public DriverDTO get(
        @Valid @NotNull(message = NOT_NULL_MESSAGE) @PathVariable Long id
    ) throws EntityNotFoundException {

        return driverService.get(id);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
//    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public UserDTO create(@Valid @RequestBody DriverRegistrationRequest driverRegistrationRequest)
            throws EntityNotFoundException, PasswordsDoNotMatchException, EntityAlreadyExistsException, MailCannotBeSentException {

        return driverService.create(
            driverRegistrationRequest.getEmail(),
            driverRegistrationRequest.getPassword(),
            driverRegistrationRequest.getConfirmPassword(),
            driverRegistrationRequest.getName(),
            driverRegistrationRequest.getSurname(),
            driverRegistrationRequest.getPhoneNumber(),
            driverRegistrationRequest.getCity(),
            driverRegistrationRequest.getProfilePicture(),
            driverRegistrationRequest.getVehicleRequest().isPetFriendly(),
            driverRegistrationRequest.getVehicleRequest().isBabySeat(),
            driverRegistrationRequest.getVehicleRequest().getVehicleType()
        );
    }

    @GetMapping("/rating/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DRIVER', 'ROLE_REGULAR_USER')")
    public double getRating(@Valid @NotNull(message=NOT_NULL_MESSAGE) @PathVariable Long id) throws EntityNotFoundException{
        return driverService.getDriverRating(id);
    }
}

