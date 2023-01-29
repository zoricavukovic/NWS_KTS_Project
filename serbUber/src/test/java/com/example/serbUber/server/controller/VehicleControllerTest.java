package com.example.serbUber.server.controller;

import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.exception.EntityType;
import com.example.serbUber.request.LongLatRequest;
import com.example.serbUber.request.VehicleCurrentPositionRequest;
import com.example.serbUber.server.controller.helper.TestUtil;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;


import java.util.Objects;

import static com.example.serbUber.exception.EntityType.getEntityErrorMessage;
import static com.example.serbUber.server.controller.helper.ControllerConstants.NOT_EXIST_ENTITY;
import static com.example.serbUber.server.controller.helper.VehicleConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.DisplayName.class)
@Transactional
public class VehicleControllerTest {

    private static final String VEHICLE_URL_PREFIX = "/vehicles";

    private MockMvc mockMvc;
    private final MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype());

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @DisplayName("T1-Should throw entity not found (vehicle found driving) when making PUT request to endpoint - /update-current-location/{id}")
    @Rollback(true)
    public void shouldThrowEntityNotFoundUpdateCurrentPosition() throws Exception {

        String errorMessage = String.format(getEntityErrorMessage(NOT_EXIST_ENTITY.toString(), EntityType.VEHICLE));
        String json = TestUtil.json(VEHICLE_CURRENT_POSITION_REQUEST);
        this.mockMvc.perform(MockMvcRequestBuilders.put(String.format("%s/update-current-location/%d", VEHICLE_URL_PREFIX, NOT_EXIST_ENTITY))
                        .contentType(contentType).content(json)).andExpect(status().isNotFound())
                .andExpect(result ->
                        assertTrue(result.getResolvedException() instanceof EntityNotFoundException)
                )
                .andExpect(result -> assertEquals(errorMessage, Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    @DisplayName("T2-Should throw entity not found when missing driver for vehicle making PUT request to endpoint - /update-current-location/{id}")
    @Rollback(true)
    public void shouldThrowEntityNotFoundWhenNotFoundDriverUpdateCurrentPosition() throws Exception {

        String errorMessage = String.format(getEntityErrorMessage(INVALID_VEHICLE_WITHOUT_DRIVER_ID.toString(), EntityType.VEHICLE));
        String json = TestUtil.json(VEHICLE_CURRENT_POSITION_REQUEST);
        this.mockMvc.perform(MockMvcRequestBuilders.put(String.format("%s/update-current-location/%d", VEHICLE_URL_PREFIX, INVALID_VEHICLE_WITHOUT_DRIVER_ID))
                        .contentType(contentType).content(json)).andExpect(status().isNotFound())
                .andExpect(result ->
                        assertTrue(result.getResolvedException() instanceof EntityNotFoundException)
                )
                .andExpect(result -> assertEquals(errorMessage, Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    @DisplayName("T3-Should throw bad request when invalid longitude request making PUT request to endpoint - /update-current-location/{id}")
    @Rollback(true)
    public void shouldThrowBadRequestInvalidLongitudeReqUpdateCurrentPosition() throws Exception {

        VehicleCurrentPositionRequest vehicleCurrentPositionRequest = new VehicleCurrentPositionRequest(new LongLatRequest(-3, 3), 0, 1);

        String json = TestUtil.json(vehicleCurrentPositionRequest);
        this.mockMvc.perform(MockMvcRequestBuilders.put(String.format("%s/update-current-location/%d", VEHICLE_URL_PREFIX, EXIST_VEHICLE_ID))
                        .contentType(contentType).content(json)).andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("T4-Should throw bad request when invalid latitude request making PUT request to endpoint - /update-current-location/{id}")
    @Rollback(true)
    public void shouldThrowBadRequestInvalidLatitudeReqUpdateCurrentPosition() throws Exception {

        VehicleCurrentPositionRequest vehicleCurrentPositionRequest = new VehicleCurrentPositionRequest(new LongLatRequest(5, -1), 0, 1);

        String json = TestUtil.json(vehicleCurrentPositionRequest);
        this.mockMvc.perform(MockMvcRequestBuilders.put(String.format("%s/update-current-location/%d", VEHICLE_URL_PREFIX, EXIST_VEHICLE_ID))
                .contentType(contentType).content(json)).andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("T5-Should throw bad request when missing crossed waypoints making PUT request to endpoint - /update-current-location/{id}")
    @Rollback(true)
    public void shouldThrowBadRequestMissingWaypointsUpdateCurrentPosition() throws Exception {

        VehicleCurrentPositionRequest vehicleCurrentPositionRequest = new VehicleCurrentPositionRequest(LONG_LAT_REQUEST, -2, 1);

        String json = TestUtil.json(vehicleCurrentPositionRequest);
        this.mockMvc.perform(MockMvcRequestBuilders.put(String.format("%s/update-current-location/%d", VEHICLE_URL_PREFIX, EXIST_VEHICLE_ID))
                .contentType(contentType).content(json)).andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("T6-Should throw bad request when missing chosen route index making PUT request to endpoint - /update-current-location/{id}")
    @Rollback(true)
    public void shouldThrowBadRequestChosenRouteIndexUpdateCurrentPosition() throws Exception {

        VehicleCurrentPositionRequest vehicleCurrentPositionRequest = new VehicleCurrentPositionRequest(LONG_LAT_REQUEST, -1, -4);

        String json = TestUtil.json(vehicleCurrentPositionRequest);
        this.mockMvc.perform(MockMvcRequestBuilders.put(String.format("%s/update-current-location/%d", VEHICLE_URL_PREFIX, EXIST_VEHICLE_ID))
                .contentType(contentType).content(json)).andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("T7-Should update current position, vehicle in drive making PUT request to endpoint - /update-current-location/{id}")
    @Rollback(true)
    public void shouldSuccessfullyUpdateCurrentPositionVehicleInDriver() throws Exception {

        String json = TestUtil.json(VEHICLE_CURRENT_POSITION_REQUEST);
        this.mockMvc.perform(MockMvcRequestBuilders.put(String.format("%s/update-current-location/%d", VEHICLE_URL_PREFIX, EXIST_VEHICLE_ID))
                .contentType(contentType).content(json)).andExpect(status().isOk())
                .andExpect(jsonPath("$.vehicleId").value(EXIST_VEHICLE_ID))
                .andExpect(jsonPath("$.crossedWaypoints").value(VEHICLE_CURRENT_POSITION_REQUEST.getCrossedWaypoints()))
                .andExpect(jsonPath("$.waypoints.size()").value(2))
                .andExpect(jsonPath("$.inDrive").value(true));
    }

    @Test
    @DisplayName("T7-Should update current position, vehicle not in drive making PUT request to endpoint - /update-current-location/{id}")
    @Rollback(true)
    public void shouldSuccessfullyUpdateCurrentPositionVehicleNotInDriver() throws Exception {

        String json = TestUtil.json(VEHICLE_CURRENT_POSITION_REQUEST);
        this.mockMvc.perform(MockMvcRequestBuilders.put(String.format("%s/update-current-location/%d", VEHICLE_URL_PREFIX, VEHICLE_WITHOUT_ACTIVE_ROUTE_ID))
                        .contentType(contentType).content(json)).andExpect(status().isOk())
                .andExpect(jsonPath("$.vehicleId").value(VEHICLE_WITHOUT_ACTIVE_ROUTE_ID))
                .andExpect(jsonPath("$.crossedWaypoints").value(VEHICLE_CURRENT_POSITION_REQUEST.getCrossedWaypoints()))
                .andExpect(jsonPath("$.waypoints.size()").value(0))
                .andExpect(jsonPath("$.inDrive").value(false));
    }

}
