package com.example.serbUber.controller;

import com.example.serbUber.dto.VehicleTypeInfoDTO;
import com.example.serbUber.request.VehicleTypeInfoRequest;
import com.example.serbUber.service.VehicleTypeInfoService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/vehicle-type-infos")
public class VehicleTypeInfoController {

    private final VehicleTypeInfoService vehicleTypeInfoService;

    public VehicleTypeInfoController(@Qualifier("vehicleTypeInfoServiceConfiguration") final VehicleTypeInfoService vehicleTypeInfoService) {
        this.vehicleTypeInfoService = vehicleTypeInfoService;
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<VehicleTypeInfoDTO> getAll() {

        return this.vehicleTypeInfoService.getAll();
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@Valid @RequestBody VehicleTypeInfoRequest vehicleTypeInfoRequest) {

        this.vehicleTypeInfoService.create(
            vehicleTypeInfoRequest.getVehicleType(),
            vehicleTypeInfoRequest.getStartPrice(),
            vehicleTypeInfoRequest.getNumOfSeats()
        );
    }


}
