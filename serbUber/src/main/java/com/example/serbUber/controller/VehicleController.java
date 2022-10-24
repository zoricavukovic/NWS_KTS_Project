package com.example.serbUber.controller;


import com.example.serbUber.dto.VehicleDTO;
import com.example.serbUber.request.VehicleRequest;
import com.example.serbUber.service.VehicleService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/vehicles")
public class VehicleController {

    private VehicleService vehicleService;

    public VehicleController(VehicleService vehicleServie) {
        this.vehicleService = vehicleServie;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@Valid @RequestBody VehicleRequest vehicleRequest) {

        this.vehicleService.create(
          vehicleRequest.isPetFriendly(),
          vehicleRequest.isBabySeat(),
          vehicleRequest.getVehicleType()
        );
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<VehicleDTO> getAll() {

        return this.vehicleService.getAll();
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable String id) {

        this.vehicleService.delete(id);
    }


}
