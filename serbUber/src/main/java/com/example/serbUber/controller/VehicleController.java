package com.example.serbUber.controller;


import com.example.serbUber.dto.VehicleDTO;
import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.request.VehicleRequest;
import com.example.serbUber.service.VehicleService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/vehicles")
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(final VehicleService vehicleServie) {
        this.vehicleService = vehicleServie;
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<VehicleDTO> getAll() {

        return this.vehicleService.getAll();
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public VehicleDTO create(@Valid @RequestBody VehicleRequest vehicleRequest) throws EntityNotFoundException {

        return this.vehicleService.create(
          vehicleRequest.isPetFriendly(),
          vehicleRequest.isBabySeat(),
          vehicleRequest.getVehicleType()
        );
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {

        this.vehicleService.delete(id);
    }

    @GetMapping("/rating/{id}")
    @ResponseStatus(HttpStatus.OK)
    public double getRatingForVehicle(@PathVariable  Long id) {
        return vehicleService.getRatingForVehicle(id);
    }


}
