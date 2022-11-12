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
import com.example.serbUber.request.user.UserEmailRequest;
import com.example.serbUber.request.user.UsersProfileUpdateRequest;
import com.example.serbUber.service.user.RegularUserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import java.util.List;

import static com.example.serbUber.exception.ErrorMessagesConstants.WRONG_EMAIL;

@RestController
@RequestMapping("/regular-users")
public class RegularUserController {

    private final RegularUserService regularUserService;

    public RegularUserController(RegularUserService regularUserService) {
        this.regularUserService = regularUserService;
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<RegularUserDTO> getAll() {

        return regularUserService.getAll();
    }

    @GetMapping("/byEmail")
    @ResponseStatus(HttpStatus.OK)
    public RegularUserDTO get(
        @Valid @RequestBody UserEmailRequest emailRequest
    ) throws EntityNotFoundException {

        return regularUserService.get(emailRequest.getEmail());
    }


    @PostMapping("/favourite")
    @ResponseStatus(HttpStatus.OK)
    public boolean addToFavouriteRoutes(@RequestBody FavouriteRouteRequest request) throws EntityNotFoundException {

        return regularUserService.addToFavouriteRoutes(request.getUserEmail(), request.getRouteId());
    }

    @PostMapping("/removeFavourite")
    @ResponseStatus(HttpStatus.OK)
    public boolean removeFromFavouriteRoutes(@RequestBody FavouriteRouteRequest request) throws EntityNotFoundException {

        return regularUserService.removeFromFavouriteRoutes(request.getUserEmail(), request.getRouteId());
    }

    @GetMapping("/favouriteRoute/{id}/{email}")
    @ResponseStatus(HttpStatus.OK)
    public boolean isFavouriteRoute(@PathVariable Long id, @PathVariable String email){
        return regularUserService.isFavouriteRoute(id, email);
    }

    @GetMapping("/favourite-routes/{email}")
    @ResponseStatus(HttpStatus.OK)
    public List<RouteDTO> getFavouriteRoutes(@Valid @Email(message=WRONG_EMAIL) @PathVariable String email){
        return regularUserService.getFavouriteRoutes(email);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO create(@Valid @RequestBody RegularUserRequest regularUserRequest)
            throws EntityNotFoundException, PasswordsDoNotMatchException, EntityAlreadyExistsException, MailCannotBeSentException {

        return regularUserService.create(
                regularUserRequest.getEmail(),
                regularUserRequest.getPassword(),
                regularUserRequest.getConfirmationPassword(),
                regularUserRequest.getName(),
                regularUserRequest.getSurname(),
                regularUserRequest.getPhoneNumber(),
                regularUserRequest.getCity(),
                regularUserRequest.getProfilePicture()
        );
    }
}
