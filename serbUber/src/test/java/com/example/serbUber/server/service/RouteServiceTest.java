package com.example.serbUber.server.service;

import com.example.serbUber.model.DrivingLocationIndex;
import com.example.serbUber.model.Location;
import com.example.serbUber.model.Route;
import com.example.serbUber.repository.RouteRepository;
import com.example.serbUber.request.DrivingLocationIndexRequest;
import com.example.serbUber.service.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static com.example.serbUber.server.helper.LocationHelper.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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


}
