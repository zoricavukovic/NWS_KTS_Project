package com.example.serbUber.service;

import com.example.serbUber.dto.PossibleRouteDTO;
import com.example.serbUber.dto.PossibleRoutesViaPointsDTO;
import com.example.serbUber.dto.RouteDTO;
import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.model.Location;
import com.example.serbUber.model.Route;
import com.example.serbUber.repository.RouteRepository;
import com.example.serbUber.request.LocationsForRoutesRequest;
import com.example.serbUber.request.LongLatRequest;
import com.graphhopper.ResponsePath;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.IntStream;

import static com.example.serbUber.SerbUberApplication.hopper;
import static com.example.serbUber.dto.RouteDTO.fromRoutes;
import static com.example.serbUber.util.Constants.START_LIST_INDEX;
import static com.example.serbUber.util.Constants.getBeforeLastIndexOfList;
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
            final Set<Location> locations,
            final double distance,
            final double time
    ) {

        Route route = routeRepository.save(new Route(
                locations,
                distance,
                time
        ));

        return new RouteDTO(route);
    }

    public List<PossibleRoutesViaPointsDTO> getPossibleRoutes(LocationsForRoutesRequest locationsForRouteRequest) {
        List<PossibleRoutesViaPointsDTO> possibleRoutesViaPointsDTOs = new LinkedList<>();

        IntStream.range(
            START_LIST_INDEX, getBeforeLastIndexOfList(locationsForRouteRequest.getLocationsForRouteRequest())
            )
            .forEach(index -> addPossibleRoutesViaPoints(locationsForRouteRequest.getLocationsForRouteRequest(), possibleRoutesViaPointsDTOs, index));

        return possibleRoutesViaPointsDTOs;
    }

    private void addPossibleRoutesViaPoints(
        List<LongLatRequest> longLatRequestList,
        List<PossibleRoutesViaPointsDTO> possibleRoutesViaPointsDTOs,
        int index
    ) {

        possibleRoutesViaPointsDTOs.add(
            new PossibleRoutesViaPointsDTO(getPossibleRoutesDTO(longLatRequestList.get(index),
                longLatRequestList.get(index + 1))
            )
        );
    }

    private List<PossibleRouteDTO> getPossibleRoutesDTO(LongLatRequest firstPoint, LongLatRequest secondPoint) {
        List<PossibleRouteDTO> possibleRouteDTOs = new LinkedList<>();

        List<ResponsePath> responsePaths = routing(hopper, firstPoint, secondPoint);

        responsePaths.forEach( responsePath -> possibleRouteDTOs.add(
            new PossibleRouteDTO(responsePath.getDistance(), responsePath.getTime(), getPointsDTO(responsePath))
        ));

        return possibleRouteDTOs;
    }

    private List<double[]> getPointsDTO(ResponsePath responsePath) {
        List<double[]> points = new LinkedList<>();

        IntStream.range(START_LIST_INDEX, responsePath.getPoints().size())
            .forEach(index ->
                points.add(new double[]{responsePath.getPoints().getLat(index), responsePath.getPoints().getLon(index)})
            );

        return points;
    }
}
