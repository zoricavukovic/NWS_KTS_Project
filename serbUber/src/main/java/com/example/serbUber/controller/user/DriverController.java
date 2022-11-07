package com.example.serbUber.controller.user;

import com.example.serbUber.dto.user.DriverDTO;
import com.example.serbUber.dto.user.UserDTO;
import com.example.serbUber.exception.EntityAlreadyExistsException;
import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.exception.MailCannotBeSentException;
import com.example.serbUber.exception.PasswordsDoNotMatchException;
import com.example.serbUber.request.user.DriverRegistrationRequest;
import com.example.serbUber.request.user.UserEmailRequest;
import com.example.serbUber.service.user.DriverService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import java.util.List;

import static com.example.serbUber.exception.ErrorMessagesConstants.WRONG_EMAIL;

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

    @GetMapping("/{email}")
    @ResponseStatus(HttpStatus.OK)
    public DriverDTO getUserByEmail(@PathVariable String email) throws EntityNotFoundException {
        return driverService.get(email);
    }

    @GetMapping("/byEmail")
    @ResponseStatus(HttpStatus.OK)
    public DriverDTO get(
        @Valid @RequestBody UserEmailRequest emailRequest
    ) throws EntityNotFoundException {

        return driverService.get(emailRequest.getEmail());
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public DriverDTO create(@Valid @RequestBody DriverRegistrationRequest driverRegistrationRequest)
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

    @GetMapping("/rating/{email}")
    @ResponseStatus(HttpStatus.OK)
    public double getRating(@Valid @Email(message=WRONG_EMAIL) @PathVariable String email) throws EntityNotFoundException{
        return driverService.getDriverRating(email);
    }
}

