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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

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
        Optional<Route> route = routeRepository.findById(id)
                ;
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

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
//        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<String> entity = new HttpEntity("parameters", headers);
        ResponseEntity<?> result =
                restTemplate.exchange("https://routing.openstreetmap.de/routed-car/route/v1/driving/" + firstPointLng + "," + firstPointLat + ";" + secondPointLng+ "," + secondPointLat+ " ?geometries=geojson&overview=false&alternatives=true&steps=true",
                        HttpMethod.GET, entity, Object.class);
        return fromOSMResponse(Objects.requireNonNull(result.getBody()).toString());

//        List<ResponsePath> responsePaths = routing(
//            hopper,
//            firstPointLat,
//            firstPointLng,
//            secondPointLat,
//            secondPointLng
//        );
//
//        responsePaths.forEach( responsePath -> possibleRouteDTOs.add(
//            new PossibleRouteDTO(responsePath.getDistance(), responsePath.getTime(), getPointsDTO(responsePath))
//        ));

//        return possibleRouteDTOs;
    }

    private List<PossibleRouteDTO> fromOSMResponse(String object) {

        return fromLegs(object);
    }

    private List<PossibleRouteDTO> fromLegs(String object) {
        List<PossibleRouteDTO> possibleRouteDTOs = new LinkedList<>();
        List<String> legs = Arrays.stream(object.split("legs=")).toList();
        fromLegs(possibleRouteDTOs, legs);

        return possibleRouteDTOs;
    }

    private void fromLegs(List<PossibleRouteDTO> possibleRouteDTOs, List<String> legs) {
        for (String leg: legs.subList(1, legs.size())){
            List<double[]> locations = new LinkedList<>();
            List<String> steps = Arrays.stream(leg.split("steps=")).toList();
            fromSteps(locations, steps);
            possibleRouteDTOs.add(new PossibleRouteDTO(1000, 13000, locations));
        }
    }

    private void fromSteps(List<double[]> locations, List<String> steps) {
        for (String step: steps.subList(1, steps.size())){
            List<String> geometries = Arrays.stream(step.split("geometry=")).toList();
            fromGeometries(locations, geometries);
        }
    }

    private void fromGeometries(List<double[]> locations, List<String> geometries) {
        for (String geometry: geometries.subList(1, geometries.size())){
            List<String> coordinates = Arrays.stream(geometry.split("coordinates=")).toList();
            fromCoordinates(locations, coordinates);
        }
    }

    private void fromCoordinates(List<double[]> locations, List<String> coordinates) {
        for (String coordinate: coordinates.subList(1, coordinates.size())){
            List<String> points = Arrays.stream(coordinate.split(",")).toList();
            fromPoints(locations, points);
        }
    }

    private void fromPoints(List<double[]> locations, List<String> points) {
        for (int i = 0; i< points.size()-2; i+=2){
            String firstPoint = points.get(i).trim();
            String secondPoint = points.get(i + 1).trim();
            if (firstPoint.toLowerCase().startsWith("type") || secondPoint.toLowerCase().startsWith("type")){
                break;
            }

            String lng = firstPoint.replace("[","");
            lng = lng.replace("]","");
            String lat = secondPoint.replace("[","");
            lat = lat.replace("]","");
            locations.add(new double[]{Double.parseDouble(lat), Double.parseDouble(lng)});

        }
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