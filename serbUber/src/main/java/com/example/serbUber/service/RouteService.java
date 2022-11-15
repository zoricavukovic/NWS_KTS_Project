package com.example.serbUber.service;

import com.example.serbUber.dto.PossibleRouteDTO;
import com.example.serbUber.dto.RouteDTO;
import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.model.Location;
import com.example.serbUber.model.Route;
import com.example.serbUber.repository.RouteRepository;
import com.example.serbUber.request.LocationsForRoutesRequest;
import com.graphhopper.ResponsePath;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.example.serbUber.SerbUberApplication.hopper;
import static com.example.serbUber.dto.RouteDTO.fromRoutes;
import static com.example.serbUber.util.GraphHopperUtil.routing;
import static com.example.serbUber.exception.EntityType.ROUTE;

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

    public Route get(Long id) throws EntityNotFoundException {
        Optional<Route> route = routeRepository.findById(id);
        if(route.isPresent()){
            return route.get();
        }
        throw new EntityNotFoundException(id, ROUTE);
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

    public List<PossibleRouteDTO> getPossibleRoutes(LocationsForRoutesRequest locationsForRouteRequest) {
        List<PossibleRouteDTO> possibleRouteDTOs = new LinkedList<>();
        List<ResponsePath> responsePaths = routing(hopper, locationsForRouteRequest);

        responsePaths.forEach( responsePath -> {
            possibleRouteDTOs.add(
                new PossibleRouteDTO(responsePath.getDistance(), responsePath.getTime(), getPointsDTO(responsePath))
            );
        });

        return possibleRouteDTOs;
    }

    private List<double[]> getPointsDTO(ResponsePath responsePath) {
        List<double[]> points = new LinkedList<>();
        responsePath.getPoints().size();
        for (int i = 0; i < responsePath.getPoints().size();i++) {
            points.add(new double[]{responsePath.getPoints().getLat(i), responsePath.getPoints().getLon(i)});
        }
        return points;
    }
}
