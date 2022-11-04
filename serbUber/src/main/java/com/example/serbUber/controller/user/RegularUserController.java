package com.example.serbUber.controller.user;

import com.example.serbUber.dto.user.RegularUserDTO;
import com.example.serbUber.exception.EntityAlreadyExistsException;
import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.exception.MailCannotBeSentException;
import com.example.serbUber.exception.PasswordsDoNotMatchException;
import com.example.serbUber.request.user.RegularUserRequest;
import com.example.serbUber.request.user.UserEmailRequest;
import com.example.serbUber.request.user.UsersProfileUpdateRequest;
import com.example.serbUber.service.user.RegularUserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/regularUsers")
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
}
