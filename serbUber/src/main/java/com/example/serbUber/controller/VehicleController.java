package com.example.serbUber.controller;


import com.example.serbUber.dto.VehicleCurrentLocationDTO;
import com.example.serbUber.dto.VehicleCurrentLocationForLocustDTO;
import com.example.serbUber.dto.VehicleDTO;
import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.request.LocationsForRoutesRequest;
import com.example.serbUber.request.LongLatRequest;
import com.example.serbUber.request.VehicleRequest;
import com.example.serbUber.service.VehicleService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

import static com.example.serbUber.exception.ErrorMessagesConstants.MISSING_ID;

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

    @GetMapping("/active/nesto")
    @ResponseStatus(HttpStatus.OK)
    public List<VehicleCurrentLocationDTO> getAllActiveVehicles() throws EntityNotFoundException {

        return vehicleService.getAllVehiclesForActiveDriver();
    }

    @GetMapping("/active")
    @ResponseStatus(HttpStatus.OK)
    public List<VehicleCurrentLocationForLocustDTO> getAllActiveVehiclesForLocust() throws EntityNotFoundException {

        return vehicleService.getAllVehicleCurrentLocationForLocustDTOForActiveDriver();
    }


    @PutMapping("/update-current-location")
    @ResponseStatus(HttpStatus.OK)
    public List<VehicleCurrentLocationDTO> updateCurrentVehiclesLocation() throws EntityNotFoundException {

        return vehicleService.updateCurrentVehiclesLocation();
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public VehicleCurrentLocationForLocustDTO updateCurrentPosition(
        @Valid @NotNull(message = MISSING_ID) @PathVariable final Long id,
        @Valid @RequestBody LongLatRequest longLatRequest
        ) throws EntityNotFoundException {

        return this.vehicleService.updateCurrentPosition(id, longLatRequest.getLon(), longLatRequest.getLat());
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@Valid @NotNull(message = MISSING_ID) @PathVariable final Long id) {

        this.vehicleService.delete(id);
    }

    @GetMapping("/rating/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DRIVER', 'ROLE_REGULAR_USER')")
    public double getRatingForVehicle(@Valid @NotNull(message = MISSING_ID) @PathVariable  Long id) {
        return vehicleService.getRatingForVehicle(id);
    }

    @GetMapping("/{vehicleType}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DRIVER', 'ROLE_REGULAR_USER')")
    public VehicleDTO getVehicleByVehicleType(@PathVariable String vehicleType){
        return vehicleService.getVehicleDTOByVehicleType(vehicleType);
    }

    @GetMapping("/vehicle-by-driver/{driverId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DRIVER', 'ROLE_REGULAR_USER')")
    public VehicleDTO getVehicleByDriverId(
            @PathVariable @Valid @NotNull(message = MISSING_ID) Long driverId
    ) throws EntityNotFoundException {

        return vehicleService.getVehicleOfDriver(driverId);
    }
}
