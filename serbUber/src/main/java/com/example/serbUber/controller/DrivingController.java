package com.example.serbUber.controller;

import com.example.serbUber.dto.DrivingDTO;
import com.example.serbUber.request.DrivingRequest;
import com.example.serbUber.service.DrivingService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/drivings")
public class DrivingController {


    private DrivingService drivingService;


    public DrivingController(DrivingService drivingService) {
        this.drivingService = drivingService;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@Valid @RequestBody DrivingRequest drivingRequest) {
        System.out.println(drivingRequest.getDriverEmail());
        this.drivingService.create(
              drivingRequest.isActive(),
                drivingRequest.getDuration(),
                drivingRequest.getStarted(),
                drivingRequest.getPayingLimit(),
                drivingRequest.getRoute(),
                drivingRequest.getDrivingStatus(),
                drivingRequest.getDriverEmail(),
                drivingRequest.getUsersPaid(),
                drivingRequest.getPrice()
        );
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<DrivingDTO> getAll() {

        return this.drivingService.getAll();
    }
}
