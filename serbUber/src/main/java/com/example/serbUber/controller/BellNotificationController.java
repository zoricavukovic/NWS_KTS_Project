package com.example.serbUber.controller;

import com.example.serbUber.dto.bell.BellNotificationDTO;
import com.example.serbUber.service.BellNotificationService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

import static com.example.serbUber.exception.ErrorMessagesConstants.NOT_NULL_MESSAGE;

@RestController
@RequestMapping("/bell-notifications")
public class BellNotificationController {

    private final BellNotificationService bellNotificationService;

    public BellNotificationController(@Qualifier("bellNotificationServiceConfiguration") final BellNotificationService bellNotificationService) {
        this.bellNotificationService = bellNotificationService;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_REGULAR_USER')")
    public List<BellNotificationDTO> getBellNotificationsForUser(
            @Valid @NotNull(message = NOT_NULL_MESSAGE) @PathVariable Long id
    ){

        return bellNotificationService.getBellNotificationsForUser(id);
    }

    @PutMapping("/seen/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_REGULAR_USER')")
    public boolean setAllAsSeen(
            @Valid @NotNull(message = NOT_NULL_MESSAGE) @PathVariable Long userId
    ){

        return bellNotificationService.setAllAsSeen(userId);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_REGULAR_USER')")
    public boolean deleteAllSeen(
            @Valid @NotNull(message = NOT_NULL_MESSAGE) @PathVariable Long userId
    ){

        return bellNotificationService.deleteAllSeen(userId);
    }

}
