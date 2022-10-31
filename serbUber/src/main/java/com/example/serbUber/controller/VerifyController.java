package com.example.serbUber.controller;

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

@RestController
@RequestMapping("/verify")
public class VerifyController {

    public final DriverService driverService;

    public final RegularUserService regularUserService;

    public final VerifyService verifyService;

    public VerifyController(
            final DriverService driverService,
            final RegularUserService regularUserService,
            final VerifyService verifyService
        ) {
        this.driverService = driverService;
        this.regularUserService = regularUserService;
        this.verifyService = verifyService;
    }

    @PostMapping("/send-code-again")
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody String verifyId)
            throws EntityNotFoundException, MailCannotBeSentException {

        this.verifyService.generateNewSecurityCode(verifyId);
    }

    @PutMapping()
    @ResponseStatus(HttpStatus.OK)
    public void update(@Valid @RequestBody VerifyRequest verifyRequest)
            throws EntityNotFoundException, WrongVerifyTryException {

        if (verifyRequest.getUserRole().equalsIgnoreCase("ROLE_DRIVER")){
            driverService.activate(
                verifyRequest.getVerifyId(),
                verifyRequest.getSecurityCode()
            );
        } else if (verifyRequest.getUserRole().equalsIgnoreCase("ROLE_REGULAR_USER")) {
            regularUserService.activate(
                verifyRequest.getVerifyId(),
                verifyRequest.getSecurityCode()
            );
        }
    }

}
