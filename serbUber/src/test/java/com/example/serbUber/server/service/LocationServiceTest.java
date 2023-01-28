package com.example.serbUber.server.service;

import com.example.serbUber.model.Location;
import com.example.serbUber.repository.LocationRepository;
import com.example.serbUber.service.LocationService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.LinkedList;
import java.util.List;
import static com.example.serbUber.server.helper.LocationHelper.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.DisplayName.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LocationServiceTest {
    @Mock
    private LocationRepository locationRepository;

    @InjectMocks
    private LocationService locationService;

    @Test
    @DisplayName("T1-Should return null when call tryToFindLocation")
    public void shouldReturnNullWhenCallsTyToFindLocation() {
        when(locationRepository.findByLonAndLat(NOT_EXIST_LOCATION[0], NOT_EXIST_LOCATION[1]))
            .thenReturn(new LinkedList<>());

        Location location = locationService.tryToFindLocation(NOT_EXIST_LOCATION[0], NOT_EXIST_LOCATION[1]);

        Assertions.assertNull(location);
    }

    @Test
    @DisplayName("T2-Should return location when call tryToFindLocation")
    public void shouldReturnLocationWhenCallsTyToFindLocation() {
        Location firstLocation = new Location(LOCATION_LON_LAT_1[1], LOCATION_LON_LAT_1[0]);
        List<Location> locations = new LinkedList<>();
        locations.add(firstLocation);
        when(locationRepository.findByLonAndLat(LOCATION_LON_LAT_1[0], LOCATION_LON_LAT_1[1]))
            .thenReturn(locations);

        Location location = locationService.tryToFindLocation(LOCATION_LON_LAT_1[0], LOCATION_LON_LAT_1[1]);

        Assertions.assertNotNull(location);
        Assertions.assertEquals(LOCATION_LON_LAT_1[0], location.getLon());
        Assertions.assertEquals(LOCATION_LON_LAT_1[1], location.getLat());
    }

    @Test
    @DisplayName("T3-Should successfully create new location")
    public void shouldSuccessfullyCreateLocation() {

        when(locationRepository.save(any(Location.class))).thenReturn(FIRST_LOCATION);

        Location createdLocation = locationService.create(CITY, STREET_1, NUMBER_1, ZIP_CODE,  LOCATION_LON_LAT_1[0],  LOCATION_LON_LAT_1[1]);

        Assertions.assertNotNull(createdLocation);
        Assertions.assertEquals(CITY, createdLocation.getCity());
        Assertions.assertEquals(STREET_1, createdLocation.getStreet());
        Assertions.assertEquals(NUMBER_1, createdLocation.getNumber());
        Assertions.assertEquals(ZIP_CODE, createdLocation.getZipCode());
        Assertions.assertEquals(LOCATION_LON_LAT_1[0], createdLocation.getLon(), 0);
        Assertions.assertEquals(LOCATION_LON_LAT_1[1], createdLocation.getLat(), 0);
    }
}
