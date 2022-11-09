package com.example.serbUber.controller;

import com.example.serbUber.dto.user.UserDTO;
import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.exception.MailCannotBeSentException;
import com.example.serbUber.exception.WrongVerifyTryException;
import com.example.serbUber.request.VerifyRequest;
import com.example.serbUber.service.VerifyService;
import com.example.serbUber.service.user.DriverService;
import com.example.serbUber.service.user.RegularUserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

import static com.example.serbUber.exception.ErrorMessagesConstants.WRONG_VERIFY_ID;
import static com.example.serbUber.util.Constants.POSITIVE_INT_NUM_REG;
import static com.example.serbUber.util.Constants.ROLE_DRIVER;

@RestController
@RequestMapping("/verify")
public class VerifyController {

    private final VerifyService verifyService;

    private final RegularUserService regularUserService;

    private final DriverService driverService;

    public VerifyController(
            final VerifyService verifyService,
            final RegularUserService regularUserService,
            final DriverService driverService
        ) {
        this.verifyService = verifyService;
        this.regularUserService = regularUserService;
        this.driverService = driverService;
    }

    @PostMapping("/send-code-again")
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@Valid @NotNull(message = WRONG_VERIFY_ID) @Positive(message = WRONG_VERIFY_ID)
                           @RequestBody Long verifyId)
            throws EntityNotFoundException, MailCannotBeSentException {

        this.verifyService.generateNewSecurityCode(verifyId);
    }

    @PutMapping()
    @ResponseStatus(HttpStatus.OK)
    public UserDTO update(@Valid @RequestBody VerifyRequest verifyRequest)
            throws EntityNotFoundException, WrongVerifyTryException {

        return (verifyRequest.getUserRole().equalsIgnoreCase(ROLE_DRIVER)) ? driverService.activate(
                verifyRequest.getVerifyId(),
                verifyRequest.getSecurityCode()) : regularUserService.activate(
                verifyRequest.getVerifyId(),
                verifyRequest.getSecurityCode()
        );
    }

}
