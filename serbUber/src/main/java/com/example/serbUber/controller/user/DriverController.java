package com.example.serbUber.controller.user;

import com.example.serbUber.dto.user.DriverDTO;
import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.request.user.DriverRequest;
import com.example.serbUber.request.user.UserEmailRequest;
import com.example.serbUber.service.user.DriverService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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

    @GetMapping("/byEmail")
    @ResponseStatus(HttpStatus.OK)
    public DriverDTO get(
        @Valid @RequestBody UserEmailRequest emailRequest
    ) throws EntityNotFoundException {

        return driverService.get(emailRequest.getEmail());
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public DriverDTO create(@Valid @RequestBody DriverRequest driverRequest) {

        return driverService.create(
            driverRequest.getEmail(),
            driverRequest.getPassword(),
            driverRequest.getName(),
            driverRequest.getSurname(),
            driverRequest.getPhoneNumber(),
            driverRequest.getCity(),
            driverRequest.getProfilePicture()
        );
    }
}

