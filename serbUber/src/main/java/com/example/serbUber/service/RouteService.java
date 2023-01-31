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
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
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
import static com.example.serbUber.dto.RouteDTO.fromRoutes;
import static com.example.serbUber.exception.EntityType.ROUTE;
import static com.example.serbUber.util.Constants.*;

@Component
@Qualifier("routeServiceConfiguration")
public class RouteService implements IRouteService {

    private RouteRepository routeRepository;
    private LocationService locationService;
    private VehicleTypeInfoService vehicleTypeInfoService;
    private DrivingLocationIndexService drivingLocationIndexService;

    @Autowired
    public RouteService(
            final RouteRepository routeRepository,
            final LocationService locationService,
            final DrivingLocationIndexService drivingLocationIndexService,
            final VehicleTypeInfoService vehicleTypeInfoService
    ) {
        this.routeRepository = routeRepository;
        this.locationService = locationService;
        this.drivingLocationIndexService = drivingLocationIndexService;
        this.vehicleTypeInfoService = vehicleTypeInfoService;
    }

    public List<RouteDTO> getAll() {
        List<Route> routes = routeRepository.findAll();

        return fromRoutes(routes);
    }

    public Route get(Long id) throws EntityNotFoundException {

        return routeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id, EntityType.ROUTE));
    }


    public Route create(final SortedSet<DrivingLocationIndex> locations, final double distance, final double time) {

        return routeRepository.save(new Route(
                locations,
                distance,
                time
        ));
    }

    private Optional<PossibleRoutesViaPointsDTO> addRouteViaPointSafely(
        LocationsForRoutesRequest locationsForRouteRequest,
        int index
    ) {
        try {
            PossibleRoutesViaPointsDTO routeViaPoint = addPossibleRoutesViaPoints(
                locationsForRouteRequest.getLocationsForRouteRequest(), index);

            return Optional.of(routeViaPoint);
        } catch (EntityNotFoundException e) {

            return Optional.empty();
        }
    }

    public List<PossibleRoutesViaPointsDTO> getPossibleRoutes(LocationsForRoutesRequest locationsForRouteRequest) {
        List<PossibleRoutesViaPointsDTO> possibleRoutesViaPointsDTOs = new LinkedList<>();

        IntStream.range(START_LIST_INDEX, getBeforeLastIndexOfList(locationsForRouteRequest.getLocationsForRouteRequest()))
            .mapToObj(index -> addRouteViaPointSafely(locationsForRouteRequest, index))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .forEach(possibleRoutesViaPointsDTOs::add);

        return possibleRoutesViaPointsDTOs;
    }

    public List<double[]> getRoutePath(final Long id) throws EntityNotFoundException {
        Route route = get(id);
        List<DrivingLocationIndex> locations = route.getLocations().stream().toList();
        List<double[]> points = new LinkedList<>();
        int index = 0;
        while (index < locations.size() - 1) {
            PossibleRouteDTO path = formPath(locations, index);
            if (path != null) {
                points.addAll(path.getPointList());
            }
            index += 1;
        }

        return points;
    }

    private PossibleRouteDTO formPath(List<DrivingLocationIndex> locations, int index) throws EntityNotFoundException {
        DrivingLocationIndex firstLocation = locations.get(index);
        DrivingLocationIndex secondLocation = locations.get(index + 1);

        return getPossibleRoutesDTO(
                firstLocation.getLocation().getLat(),
                firstLocation.getLocation().getLon(),
                secondLocation.getLocation().getLat(),
                secondLocation.getLocation().getLon()
        ).get(firstLocation.getRouteIndex());
    }

    public SortedSet<DrivingLocationIndex> getLocationsForRoute(Long id) {

        return routeRepository.getLocationsForRoute(id);
    }

    public Route createRoute(
            final List<DrivingLocationIndexRequest> locations,
            final double time,
            final double distance,
            final List<Integer> routePathIndex
    ){
        SortedSet<DrivingLocationIndex> drivingLocations = new TreeSet<>();
        locations.forEach(locationIndex -> {
            Location location = createLocationIfDoesNotExist(locationIndex);
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

    private Location createLocationIfDoesNotExist(final DrivingLocationIndexRequest locationIndex) {
        Location location = locationService.tryToFindLocation(locationIndex.getLocation().getLon(), locationIndex.getLocation().getLat());
        if (location == null){
            location = locationService.create(
                locationIndex.getLocation().getCity(),
                locationIndex.getLocation().getStreet(),
                locationIndex.getLocation().getNumber(),
                locationIndex.getLocation().getZipCode(),
                locationIndex.getLocation().getLon(),
                locationIndex.getLocation().getLat()
            );
        }
        return location;
    }

    public double getTimeFromDistance(final double distance) {

        return distance == ZERO_DISTANCE ? ZERO_MINUTES :  Math.ceil((distance/AVERAGE_CAR_SPEED_IN_M_H)*MINUTES_FOR_ONE_HOUR + 0.5);
    }

    public double getDistanceInKmFromTime(final double minutes) {

        return minutes == ZERO_MINUTES ? ZERO_DISTANCE : AVERAGE_CAR_SPEED_IN_KM_H*(minutes/MINUTES_FOR_ONE_HOUR);
    }

    private boolean isDestination(int indexOfLocation, int numOfLocations){

        return indexOfLocation == numOfLocations - 1;
    }

    private PossibleRoutesViaPointsDTO addPossibleRoutesViaPoints(
            List<LongLatRequest> longLatRequestList,
            int index
    ) throws EntityNotFoundException {

        return new PossibleRoutesViaPointsDTO(
            getPossibleRoutesDTO(longLatRequestList.get(index).getLat(), longLatRequestList.get(index).getLon(),
                longLatRequestList.get(index + 1).getLat(), longLatRequestList.get(index + 1).getLon())
        );
    }

    private List<PossibleRouteDTO> getPossibleRoutesDTO(
            final double firstPointLat,
            final double firstPointLng,
            final double secondPointLat,
            final double secondPointLng
    ) throws EntityNotFoundException {

        return fromOSMResponse(getOSMResult(firstPointLat, firstPointLng, secondPointLat, secondPointLng));
    }

    private String getOSMResult(
            final double firstPointLat,
            final double firstPointLng,
            final double secondPointLat,
            final double secondPointLng
    ){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        ResponseEntity<?> result =
                restTemplate.exchange("https://routing.openstreetmap.de/routed-car/route/v1/driving/" + firstPointLng + "," + firstPointLat + ";" + secondPointLng+ "," + secondPointLat+ " ?geometries=geojson&overview=false&alternatives=true&steps=true",
                        HttpMethod.GET, entity, Object.class);

        return Objects.requireNonNull(result.getBody()).toString();
    }

    public double calculateMinutesForDistance(
            final double firstPointLat,
            final double firstPointLng,
            final double secondPointLat,
            final double secondPointLng
    ){
        double minutes = 0;
        String result = getOSMResult(firstPointLat, firstPointLng, secondPointLat, secondPointLng);
        List<String> legs = Arrays.stream(result.split("legs=")).toList();
        if(legs.size() > 1){
            double distance = getDistance(Arrays.stream(legs.get(1).split("distance=")).toList());
            minutes = getTimeFromDistance(distance);
        }

        return minutes;
    }

    private List<PossibleRouteDTO> fromOSMResponse(String object) throws EntityNotFoundException {

        return fromLegs(object);
    }

    private List<PossibleRouteDTO> fromLegs(String object) throws EntityNotFoundException {
        List<PossibleRouteDTO> possibleRouteDTOs = new LinkedList<>();
        List<String> legs = Arrays.stream(object.split("legs=")).toList();
        fromLegs(possibleRouteDTOs, legs);

        return possibleRouteDTOs;
    }

    private void fromLegs(List<PossibleRouteDTO> possibleRouteDTOs, List<String> legs) throws EntityNotFoundException {
        for (String leg: legs.subList(1, legs.size())){
            List<double[]> locations = new LinkedList<>();
            List<String> steps = Arrays.stream(leg.split("steps=")).toList();
            fromSteps(locations, steps);
            double distance = getDistance(Arrays.stream(leg.split("distance=")).toList());
            double minutes = getTimeFromDistance(distance);
            double averagePrice = vehicleTypeInfoService.getAveragePriceForChosenRoute(distance/ONE_KILOMETER_TO_METER);
            possibleRouteDTOs.add(new PossibleRouteDTO(distance, locations, minutes, averagePrice));
        }
    }

    private double getDistance(List<String> distances) {

        return distances.stream()
                .mapToDouble(distance -> Double.parseDouble(distance.split(",")[0].replaceAll("[\\[\\]{} ]", "")))
                .max()
                .orElse(0);
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
}