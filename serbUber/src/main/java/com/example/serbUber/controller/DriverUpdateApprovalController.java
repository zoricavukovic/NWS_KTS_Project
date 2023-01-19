package com.example.serbUber.controller;

import com.example.serbUber.dto.user.DriverUpdateApprovalDTO;

import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.exception.EntityUpdateException;
import com.example.serbUber.service.DriverUpdateApprovalService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

import static com.example.serbUber.exception.ErrorMessagesConstants.NOT_NULL_MESSAGE;

@RestController
@RequestMapping("/driver-update-approval")
public class DriverUpdateApprovalController {

    private final DriverUpdateApprovalService driverUpdateApprovalService;

    public DriverUpdateApprovalController(@Qualifier("driverUpdateApprovalServiceConfiguration") final DriverUpdateApprovalService driverUpdateApprovalService) {
        this.driverUpdateApprovalService = driverUpdateApprovalService;
    }

    @GetMapping()
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public List<DriverUpdateApprovalDTO> getAllNotApproved() {

        return driverUpdateApprovalService.getAllNotApproved();
    }

    @PutMapping("/reject/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public boolean reject(
            @Valid @NotNull(message = NOT_NULL_MESSAGE) @PathVariable Long id
    ) throws EntityNotFoundException, EntityUpdateException {

        return driverUpdateApprovalService.reject(id);
    }

    @PutMapping("/approve/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public boolean approve(
            @Valid @NotNull(message = NOT_NULL_MESSAGE) @PathVariable Long id
    ) throws EntityNotFoundException {

        return driverUpdateApprovalService.approve(id);
    }

}
