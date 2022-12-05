package com.example.serbUber.controller;


import com.example.serbUber.dto.VehicleCurrentLocationDTO;
import com.example.serbUber.dto.VehicleDTO;
import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.request.VehicleRequest;
import com.example.serbUber.service.VehicleService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/vehicles")
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(@Qualifier("vehicleServiceConfiguration") final VehicleService vehicleServie) {
        this.vehicleService = vehicleServie;
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<VehicleDTO> getAll() {

        return this.vehicleService.getAll();
    }

    @GetMapping("/active")
    @ResponseStatus(HttpStatus.OK)
    public List<VehicleCurrentLocationDTO> getAllActiveVehicles() {

        return vehicleService.getAllVehiclesForActiveDriver();
    }

    @PutMapping("/update-current-location")
    @ResponseStatus(HttpStatus.OK)
    public List<VehicleCurrentLocationDTO> updateCurrentVehiclesLocation() {

        return vehicleService.updateCurrentVehiclesLocation();
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {

        this.vehicleService.delete(id);
    }

    @GetMapping("/rating/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DRIVER', 'ROLE_REGULAR_USER')")
    public double getRatingForVehicle(@PathVariable  Long id) {
        return vehicleService.getRatingForVehicle(id);
    }

    @GetMapping("/{vehicleType}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DRIVER', 'ROLE_REGULAR_USER')")
    public VehicleDTO getVehicleByVehicleType(@PathVariable String vehicleType){
        return vehicleService.getVehicleDTOByVehicleType(vehicleType);
    }
}
