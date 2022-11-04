package com.example.serbUber.service;

import com.example.serbUber.dto.RouteDTO;
import com.example.serbUber.model.Location;
import com.example.serbUber.model.Route;
import com.example.serbUber.repository.RouteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

import static com.example.serbUber.dto.RouteDTO.fromRoutes;

@Service
public class RouteService {

    private final RouteRepository routeRepository;

    public RouteService(final RouteRepository routeRepository) {
        this.routeRepository = routeRepository;
    }

    public List<RouteDTO> getAll() {
        List<Route> routes = routeRepository.findAll();

        return fromRoutes(routes);
    }

    public RouteDTO create(
            final Location startPoint,
            final Set<Location> destinations,
            final double kilometers
    ) {

        Route route = routeRepository.save(new Route(
                startPoint,
                destinations,
                kilometers
        ));

        return new RouteDTO(route);
    }
}
