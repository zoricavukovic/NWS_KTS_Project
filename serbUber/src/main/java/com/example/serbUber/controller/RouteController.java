package com.example.serbUber.controller;

import com.example.serbUber.dto.LngLatLiteralDTO;
import com.example.serbUber.dto.PossibleRouteDTO;
import com.example.serbUber.dto.PossibleRoutesViaPointsDTO;
import com.example.serbUber.dto.RouteDTO;
import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.request.LocationsForRoutesRequest;
import com.example.serbUber.request.LongLatRequest;
import com.example.serbUber.request.RouteRequest;
import com.example.serbUber.service.RouteService;
import com.example.serbUber.util.MaxSizeConstraint;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

import static com.example.serbUber.exception.ErrorMessagesConstants.MISSING_ID;

@RestController
@RequestMapping("/routes")
public class RouteController {
    private final RouteService routeService;

    public RouteController(@Qualifier("routeServiceConfiguration") final RouteService routeService) {
        this.routeService = routeService;
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<RouteDTO> getAll() {

        return this.routeService.getAll();
    }

    @GetMapping("/path/{id}")
    @ResponseStatus(HttpStatus.OK)
    public List<double[]> getRoutePath(@PathVariable Long id) throws EntityNotFoundException {

        return this.routeService.getRoutePath(id);
    }

    @PostMapping(path = "/possible")
    @ResponseStatus(HttpStatus.OK)
    public List<PossibleRoutesViaPointsDTO> getPossibleRoutes(
        @Valid @RequestBody LocationsForRoutesRequest locationsForRouteRequest
    ) {

        return this.routeService.getPossibleRoutes(locationsForRouteRequest);
    }
}
