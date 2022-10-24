package com.example.serbUber.controller;

import com.example.serbUber.dto.RouteDTO;
import com.example.serbUber.request.RouteRequest;
import com.example.serbUber.service.RouteService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/routes")
public class RouteController {

    private RouteService routeService;


    public RouteController(RouteService routeService) {
        this.routeService = routeService;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@Valid @RequestBody RouteRequest routeRequest) {
        this.routeService.create(
                routeRequest.getStartPoint(),
                routeRequest.getDestinations(),
                routeRequest.getKilometers()
        );
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<RouteDTO> getAll() {

        return this.routeService.getAll();
    }
}
