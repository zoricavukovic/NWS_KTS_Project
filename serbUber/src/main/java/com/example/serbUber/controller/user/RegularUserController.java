package com.example.serbUber.controller.user;

import com.example.serbUber.dto.RouteDTO;
import com.example.serbUber.dto.user.RegularUserDTO;
import com.example.serbUber.dto.user.UserDTO;
import com.example.serbUber.exception.EntityAlreadyExistsException;
import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.exception.MailCannotBeSentException;
import com.example.serbUber.exception.PasswordsDoNotMatchException;
import com.example.serbUber.request.user.FavouriteRouteRequest;
import com.example.serbUber.request.user.RegularUserRequest;
import com.example.serbUber.service.user.RegularUserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

import static com.example.serbUber.exception.ErrorMessagesConstants.NOT_NULL_MESSAGE;

@RestController
@RequestMapping("/regular-users")
public class RegularUserController {

    private final RegularUserService regularUserService;

    public RegularUserController(@Qualifier("regularUserServiceConfiguration") final RegularUserService regularUserService) {
        this.regularUserService = regularUserService;
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<RegularUserDTO> getAll() {

        return regularUserService.getAll();
    }

    @PostMapping("/favourite")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DRIVER', 'ROLE_REGULAR_USER')")
    public boolean addToFavouriteRoutes(@RequestBody FavouriteRouteRequest request) throws EntityNotFoundException {

        return regularUserService.addToFavouriteRoutes(request.getUserId(), request.getRouteId());
    }

    @PostMapping("/removeFavourite")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ROLE_REGULAR_USER')")
    public boolean removeFromFavouriteRoutes(@RequestBody FavouriteRouteRequest request) throws EntityNotFoundException {

        return regularUserService.removeFromFavouriteRoutes(request.getUserId(), request.getRouteId());
    }

    @GetMapping("/favourite-route/{routeId}/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DRIVER', 'ROLE_REGULAR_USER')")
    public boolean isFavouriteRoute(@Valid @NotNull(message = NOT_NULL_MESSAGE) @PathVariable Long routeId, @Valid @NotNull(message = NOT_NULL_MESSAGE) @PathVariable Long userId){
        return regularUserService.isFavouriteRoute(routeId, userId);
    }

    @GetMapping("/favourite-routes/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_REGULAR_USER')")
    public List<RouteDTO> getFavouriteRoutes(@Valid @NotNull(message = NOT_NULL_MESSAGE) @PathVariable Long id) throws EntityNotFoundException {
        return regularUserService.getFavouriteRoutes(id);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO create(@Valid @RequestBody RegularUserRequest regularUserRequest)
            throws EntityNotFoundException, PasswordsDoNotMatchException, EntityAlreadyExistsException, MailCannotBeSentException {

        return regularUserService.create(
                regularUserRequest.getEmail(),
                regularUserRequest.getPassword(),
                regularUserRequest.getConfirmPassword(),
                regularUserRequest.getName(),
                regularUserRequest.getSurname(),
                regularUserRequest.getPhoneNumber(),
                regularUserRequest.getCity(),
                regularUserRequest.getProfilePicture()
        );
    }
}
