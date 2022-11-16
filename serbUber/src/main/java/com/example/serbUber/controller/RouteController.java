package com.example.serbUber.controller;

import com.example.serbUber.dto.PossibleRouteDTO;
import com.example.serbUber.dto.PossibleRoutesViaPointsDTO;
import com.example.serbUber.dto.RouteDTO;
import com.example.serbUber.request.LocationsForRoutesRequest;
import com.example.serbUber.request.LongLatRequest;
import com.example.serbUber.request.RouteRequest;
import com.example.serbUber.service.RouteService;
import com.example.serbUber.util.MaxSizeConstraint;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@RestController
@RequestMapping("/routes")
public class RouteController {
    private final RouteService routeService;

    public RouteController(final RouteService routeService) {
        this.routeService = routeService;
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<RouteDTO> getAll() {

        return this.routeService.getAll();
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public RouteDTO create(@Valid @RequestBody RouteRequest routeRequest) {
        /* TODO postojalo od ranije, a izmenjeno da se ne dobija location vec locationRequest
        return this.routeService.create(
                routeRequest.getStartPoint(),
                routeRequest.getDestinations(),
                routeRequest.getKilometers()
        );*/
        return null;
    }

    @PostMapping(path = "/possible")
    @ResponseStatus(HttpStatus.OK)
    public List<PossibleRoutesViaPointsDTO> getPossibleRoutes(
        @Valid @RequestBody LocationsForRoutesRequest locationsForRouteRequest
    ) {

        return this.routeService.getPossibleRoutes(locationsForRouteRequest);
    }
}
