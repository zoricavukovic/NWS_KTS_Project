package com.example.serbUber.server.service;

import com.example.serbUber.dto.PossibleRoutesViaPointsDTO;
import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.exception.EntityType;
import com.example.serbUber.model.DrivingLocationIndex;
import com.example.serbUber.model.Location;
import com.example.serbUber.model.Route;
import com.example.serbUber.repository.RouteRepository;
import com.example.serbUber.request.DrivingLocationIndexRequest;
import com.example.serbUber.request.LocationsForRoutesRequest;
import com.example.serbUber.request.LongLatRequest;
import com.example.serbUber.service.*;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static com.example.serbUber.server.helper.LocationHelper.*;
import static com.example.serbUber.util.Constants.getBeforeLastIndexOfList;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static com.github.tomakehurst.wiremock.client.WireMock.get;


@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.DisplayName.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RouteServiceTest {
    @Mock
    private RouteRepository routeRepository;

    @Mock
    private LocationService locationService;

    @Mock
    private VehicleTypeInfoService vehicleTypeInfoService;

    @Mock
    private DrivingLocationIndexService drivingLocationIndexService;

    @InjectMocks
    private RouteService routeService;

    @Test
    @DisplayName("T1-Should create route")
    public void shouldCreateRoute() {
        List<DrivingLocationIndexRequest> drivingLocationIndexRequests = createDrivingLocationIndexRequests();
        SortedSet<DrivingLocationIndex> drivingLocationIndexList = createDrivingLocationIndex();

        double time = 5.2;
        double distance = 4.7;
        List<Integer> routePathIndex = Arrays.asList(1, 2);
        when(locationService.tryToFindLocation(LOCATION_LON_LAT_1[0], LOCATION_LON_LAT_1[1])).thenReturn(FIRST_LOCATION);
        when(locationService.tryToFindLocation(LOCATION_LON_LAT_2[0], LOCATION_LON_LAT_2[1])).thenReturn(null);

        doReturn(SECOND_LOCATION).when(locationService).create(CITY, STREET_2, NUMBER_2, ZIP_CODE, LOCATION_LON_LAT_2[0], LOCATION_LON_LAT_2[1]);

        when(drivingLocationIndexService.create(
            FIRST_LOCATION,
            routePathIndex.get(0),
            1
        )).thenReturn(drivingLocationIndexList.first());

        when(drivingLocationIndexService.create(
            SECOND_LOCATION,
            routePathIndex.get(1),
            -1
        )).thenReturn(drivingLocationIndexList.last());

        Route expectedRoute = createTestRoute(drivingLocationIndexList, time, distance);
        when(routeRepository.save(any(Route.class))).thenReturn(expectedRoute);

        Route createdRoute = routeService.createRoute(drivingLocationIndexRequests, time, distance, routePathIndex);

        assertNotNull(createdRoute);

        assertEquals(expectedRoute.getDistance(), createdRoute.getDistance(), 0);
        assertEquals(expectedRoute.getTimeInMin(), createdRoute.getTimeInMin(), 0);
        assertEquals(expectedRoute.getLocations(), createdRoute.getLocations());

        verify(locationService, times(1)).create(CITY, STREET_2, NUMBER_2, ZIP_CODE, LOCATION_LON_LAT_2[0], LOCATION_LON_LAT_2[1]);
        verify(drivingLocationIndexService, times(drivingLocationIndexRequests.size())).create(any(Location.class), anyInt(), anyInt());
    }

    @Test
    @DisplayName("T2-Should return before last index of list")
    public void shouldGetBeforeLastIndexOfList() {

        assertEquals(-1, getBeforeLastIndexOfList(new ArrayList<>()));

        List<LongLatRequest> oneElementList = new ArrayList<>();
        oneElementList.add(new LongLatRequest(1, 2));
        assertEquals(0, getBeforeLastIndexOfList(oneElementList));

        List<LongLatRequest> moreThanOneElementList = new ArrayList<>();
        moreThanOneElementList.add(new LongLatRequest(1, 2));
        moreThanOneElementList.add(new LongLatRequest(3, 4));
        moreThanOneElementList.add(new LongLatRequest(5, 6));
        assertEquals(2, getBeforeLastIndexOfList(moreThanOneElementList));
    }

    @Test
    @DisplayName("T3-Should throw entity not found when vehicle type not found")
    public void shouldReturnEmptyListOfPossibleRoutes() throws EntityNotFoundException {
        LocationsForRoutesRequest locationsForRoutesRequest = createLocationsForRoutesRequest(2);

        WireMockServer wireMockServer = new WireMockServer();
        wireMockServer.start();

        wireMockServer.stubFor(get(urlEqualTo("https://routing.openstreetmap.de/routed-car/routed-car/route/v1/driving/1,2;3,4?geometries=geojson&overview=false&alternatives=true&steps=true"))
            .willReturn(aResponse().withStatus(200).withBody("{ / JSON response / }")));

        when(vehicleTypeInfoService.getAveragePriceForChosenRoute(anyDouble())).thenThrow(new EntityNotFoundException("CAR", EntityType.VEHICLE_TYPE_INFO));

        List<PossibleRoutesViaPointsDTO> result = routeService.getPossibleRoutes(locationsForRoutesRequest);
        wireMockServer.stop();

        assertEquals(0, result.size());
    }

    @Test
    @DisplayName("T5-Should return possible routes list")
    public void shouldReturnListOfPossibleRoutes() throws EntityNotFoundException {
        LocationsForRoutesRequest locationsForRoutesRequest = createLocationsForRoutesRequest(2);

        WireMockServer wireMockServer = new WireMockServer();
        wireMockServer.start();

        wireMockServer.stubFor(get(urlEqualTo("https://routing.openstreetmap.de/routed-car/route/v1/driving/1,2;3,4?geometries=geojson&overview=false&alternatives=true&steps=true"))
            .willReturn(aResponse().withStatus(200).withBody("{ / JSON response / }")));

        when(vehicleTypeInfoService.getAveragePriceForChosenRoute(anyDouble())).thenReturn(4.0);

        List<PossibleRoutesViaPointsDTO> result = routeService.getPossibleRoutes(locationsForRoutesRequest);
        wireMockServer.stop();

        assertEquals(1, result.size());
        assertEquals(result.get(0).getPossibleRouteDTOList().get(0).getAveragePrice(), 4.0);
    }


//    @Test
//    @DisplayName("T3-Should throw entity not found when getting possible routes and cannot find average price for ride")
//    public void shouldThrowEntityNotFoundWhenCannotFindAveragePriceForRide() {
//        LocationsForRoutesRequest locationsForRoutesRequest = createLocationsForRoutesRequest(0);
//
//        WireMockServer wireMockServer = new WireMockServer();
//        wireMockServer.start();
//
//        // Configure the mock server to return a fixed response
//        wireMockServer.stubFor(get(urlEqualTo("/routed-car/route/v1/driving/firstPointLng,firstPointLat;secondPointLng,secondPointLat?geometries=geojson&overview=false&alternatives=true&steps=true"))
//            .willReturn(aResponse().withStatus(200).withBody("{ /* JSON response */ }")));
//
//        routeService.getPossibleRoutes(locationsForRoutesRequest);
//        wireMockServer.stop();
//
//
//        assertEquals(-1, getBeforeLastIndexOfList(new ArrayList<>()));
//
//        List<LongLatRequest> oneElementList = new ArrayList<>();
//        oneElementList.add(new LongLatRequest(1, 2));
//        assertEquals(0, getBeforeLastIndexOfList(oneElementList));
//
//        List<LongLatRequest> moreThanOneElementList = new ArrayList<>();
//        moreThanOneElementList.add(new LongLatRequest(1, 2));
//        moreThanOneElementList.add(new LongLatRequest(3, 4));
//        moreThanOneElementList.add(new LongLatRequest(5, 6));
//        assertEquals(2, getBeforeLastIndexOfList(moreThanOneElementList));
//    }


}
