package com.example.serbUber.controller;

import com.example.serbUber.dto.user.UserDTO;
import com.example.serbUber.service.DriverUpdateApprovalService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/driver-update-approval")
public class DriverUpdateApprovalController {

    private final DriverUpdateApprovalService driverUpdateApprovalService;

    public DriverUpdateApprovalController(@Qualifier("driverUpdateApprovalServiceConfiguration") final DriverUpdateApprovalService driverUpdateApprovalService) {
        this.driverUpdateApprovalService = driverUpdateApprovalService;
    }

    @GetMapping("all-not-approved")
    @ResponseStatus(HttpStatus.OK)
    public List<UserDTO> getAllNotApproved() {

        return driverUpdateApprovalService.getAllNotApproved();
    }
}
