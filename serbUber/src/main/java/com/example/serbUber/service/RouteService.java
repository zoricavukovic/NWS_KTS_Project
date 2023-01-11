package com.example.serbUber.service;

import com.example.serbUber.dto.PossibleRouteDTO;
import com.example.serbUber.dto.PossibleRoutesViaPointsDTO;
import com.example.serbUber.dto.RouteDTO;
import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.exception.EntityType;
import com.example.serbUber.model.DrivingLocationIndex;
import com.example.serbUber.model.Location;
import com.example.serbUber.model.Route;
import com.example.serbUber.repository.RouteRepository;
import com.example.serbUber.request.DrivingLocationIndexRequest;
import com.example.serbUber.request.LocationsForRoutesRequest;
import com.example.serbUber.request.LongLatRequest;
import com.example.serbUber.service.interfaces.IRouteService;
import com.graphhopper.ResponsePath;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
import java.util.stream.IntStream;

import static com.example.serbUber.SerbUberApplication.hopper;
import static com.example.serbUber.dto.RouteDTO.fromRoutes;
import static com.example.serbUber.exception.EntityType.ROUTE;
import static com.example.serbUber.util.Constants.START_LIST_INDEX;
import static com.example.serbUber.util.Constants.getBeforeLastIndexOfList;
import static com.example.serbUber.util.GraphHopperUtil.routing;

@Component
@Qualifier("routeServiceConfiguration")
public class RouteService implements IRouteService {

    private final RouteRepository routeRepository;
    private final LocationService locationService;

    private final DrivingLocationIndexService drivingLocationIndexService;

    public RouteService(
            final RouteRepository routeRepository,
            final LocationService locationService,
            final DrivingLocationIndexService drivingLocationIndexService
    ) {
        this.routeRepository = routeRepository;
        this.locationService = locationService;
        this.drivingLocationIndexService = drivingLocationIndexService;

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

    public RouteDTO createDTO(
            final SortedSet<DrivingLocationIndex> locations,
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

    public Route create(
            final SortedSet<DrivingLocationIndex> locations,
            final double distance,
            final double time
    ) {

        return routeRepository.save(new Route(
                locations,
                distance,
                time
        ));
    }

    public List<PossibleRoutesViaPointsDTO> getPossibleRoutes(LocationsForRoutesRequest locationsForRouteRequest) {
        List<PossibleRoutesViaPointsDTO> possibleRoutesViaPointsDTOs = new LinkedList<>();

        IntStream.range(
            START_LIST_INDEX, getBeforeLastIndexOfList(locationsForRouteRequest.getLocationsForRouteRequest())
            )
            .forEach(index -> {
                try {
                    addPossibleRoutesViaPoints(locationsForRouteRequest.getLocationsForRouteRequest(), possibleRoutesViaPointsDTOs, index);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

        return possibleRoutesViaPointsDTOs;
    }

    public List<double[]> getRoutePath(final Long id) throws EntityNotFoundException {
        Route route = this.routeRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(id, EntityType.ROUTE));
        List<DrivingLocationIndex> locations = route.getLocations().stream().toList();

        List<double[]> points = new LinkedList<>();

        IntStream.range(START_LIST_INDEX, locations.size()-1)
            .forEach(index ->
            {
                DrivingLocationIndex firstLocation = locations.get(index);
                DrivingLocationIndex secondLocation = locations.get(index + 1);
                PossibleRouteDTO path = null;
                try {
                    path = getPossibleRoutesDTO(
                        firstLocation.getLocation().getLat(),
                        firstLocation.getLocation().getLon(),
                        secondLocation.getLocation().getLat(),
                        secondLocation.getLocation().getLon()
                    ).get(firstLocation.getRouteIndex());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                points.addAll(path.getPointList());
            });

        return points;
    }

    public Route createRoute(
        final List<DrivingLocationIndexRequest> locations,
        final double time,
        final double distance,
        final List<Integer> routePathIndex
    ){
        SortedSet<DrivingLocationIndex> drivingLocations = new TreeSet<>();
        locations.forEach(locationIndex -> {
            Location location = locationService.tryToFindLocation(locationIndex.getLocation().getLon(), locationIndex.getLocation().getLat());
            if (location == null){
                location = locationService.create( locationIndex.getLocation().getCity(),
                        locationIndex.getLocation().getStreet(),
                        locationIndex.getLocation().getNumber(),
                        locationIndex.getLocation().getZipCode(),
                        locationIndex.getLocation().getLon(),
                        locationIndex.getLocation().getLat()
                );
            }
            DrivingLocationIndex drivingLocationIndex = drivingLocationIndexService.create(
                location,
                locationIndex.getIndex(),
                isDestination(locations.indexOf(locationIndex), locations.size()) ?
                    -1 : routePathIndex.get(locations.indexOf(locationIndex))
            );
            drivingLocations.add(drivingLocationIndex);
        });

        return create(drivingLocations, distance, time);
    }
    private boolean isDestination(int indexOfLocation, int numOfLocations){

        return indexOfLocation == numOfLocations - 1;
    }
    private void addPossibleRoutesViaPoints(
        List<LongLatRequest> longLatRequestList,
        List<PossibleRoutesViaPointsDTO> possibleRoutesViaPointsDTOs,
        int index
    ) throws IOException {

        possibleRoutesViaPointsDTOs.add(
            new PossibleRoutesViaPointsDTO(
                getPossibleRoutesDTO(longLatRequestList.get(index).getLat(), longLatRequestList.get(index).getLon(),
                longLatRequestList.get(index + 1).getLat(), longLatRequestList.get(index + 1).getLon())
            )
        );
    }

    private List<PossibleRouteDTO> getPossibleRoutesDTO(
        final double firstPointLat,
        final double firstPointLng,
        final double secondPointLat,
        final double secondPointLng
    ) throws IOException {
        List<PossibleRouteDTO> possibleRouteDTOs = new LinkedList<>();

//        OkHttpClient client = new OkHttpClient().newBuilder()
//            .build();
//        MediaType mediaType = MediaType.parse("text/plain");
//        RequestBody body = RequestBody.create(mediaType, "");
//        Request request = new Request.Builder()
//            .url("https://maps.googleapis.com/maps/api/directions/json?origin=45.12,20.32&destination=45.17,20.45&key=AIzaSyDMqZ8APiUZLmLb5WyzogfK3l36Z4pG4H4")
//            .method("GET", null)
//            .build();
//        Response response = client.newCall(request).execute();
//        OkHttpClient client = new OkHttpClient().newBuilder()
//            .build();
//        MediaType mediaType = MediaType.parse("text/plain");
//        RequestBody body = RequestBody.create("", mediaType);
//        Request request = new Request.Builder()
//            .url("https://maps.googleapis.com/maps/api/directions/json?origin=Toronto&destination=Montreal&key=AIzaSyDMqZ8APiUZLmLb5WyzogfK3l36Z4pG4H4")
//            .method("GET", body)
//            .build();
//        Response response = client.newCall(request).execute()

        List<ResponsePath> responsePaths = routing(
            hopper,
            firstPointLat,
            firstPointLng,
            secondPointLat,
            secondPointLng
        );

        responsePaths.forEach( responsePath -> possibleRouteDTOs.add(
            new PossibleRouteDTO(responsePath.getDistance(), responsePath.getTime(), getPointsDTO(responsePath))
        ));

        return possibleRouteDTOs;
    }

    private List<double[]> getPointsDTO(ResponsePath responsePath) {
        List<double[]> points = new LinkedList<>();
//        for (int i=0; i< responsePath.getPoints().getLat())
        IntStream.range(START_LIST_INDEX, responsePath.getPoints().size())
            .forEach(index ->
                points.add(new double[]{responsePath.getPoints().getLat(index), responsePath.getPoints().getLon(index)})
            );

        return points;
    }
}
