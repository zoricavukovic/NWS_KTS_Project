package com.example.serbUber.controller.user;

import com.example.serbUber.dto.user.DriverDTO;
import com.example.serbUber.exception.*;
import com.example.serbUber.request.user.DriverActivityStatusRequest;
import com.example.serbUber.service.user.DriverService;
import org.springframework.beans.factory.annotation.Qualifier;
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

    public DriverController(@Qualifier("driverServiceConfiguration") final DriverService driverService) {
        this.driverService = driverService;
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<DriverDTO> getAll() {

        return driverService.getAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DRIVER', 'ROLE_REGULAR_USER')")
    @ResponseStatus(HttpStatus.OK)
    public DriverDTO get(
            @Valid @NotNull(message = NOT_NULL_MESSAGE) @PathVariable Long id
    ) throws EntityNotFoundException {

        return driverService.get(id);
    }

    @GetMapping("/blocked-data/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public boolean getBlocked(
            @Valid @NotNull(message = NOT_NULL_MESSAGE) @PathVariable Long id
    ) throws EntityNotFoundException {

        return driverService.getIsBlocked(id);
    }

    @GetMapping("/rating/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DRIVER', 'ROLE_REGULAR_USER')")
    public double getRating(@Valid @NotNull(message = NOT_NULL_MESSAGE) @PathVariable Long id) throws EntityNotFoundException {
        return driverService.getDriverRating(id);
    }

    @PutMapping("/activity")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ROLE_DRIVER')")
    public DriverDTO updateActivityStatus(@Valid @RequestBody DriverActivityStatusRequest driverActivityStatusRequest)
            throws ActivityStatusCannotBeChangedException, EntityNotFoundException {

        return driverService.updateActivityStatus(
                driverActivityStatusRequest.getId(),
                driverActivityStatusRequest.isActive()
        );
    }

    @PutMapping("/unblock/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public boolean unblock(
            @Valid @NotNull(message = NOT_NULL_MESSAGE) @PathVariable Long id
    ) throws EntityNotFoundException, EntityUpdateException {

        return driverService.unblock(id);
    }
}

