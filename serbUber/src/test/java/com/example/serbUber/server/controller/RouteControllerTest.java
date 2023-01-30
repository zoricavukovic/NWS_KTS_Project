package com.example.serbUber.server.controller;

import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.exception.EntityType;
import com.example.serbUber.request.LocationsForRoutesRequest;
import com.example.serbUber.server.controller.helper.TestUtil;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.example.serbUber.exception.EntityType.getEntityErrorMessage;
import static com.example.serbUber.server.controller.helper.ControllerConstants.*;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.DisplayName.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
public class RouteControllerTest {
    private static final String ROUTE_URL_PREFIX = "/routes";

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeAll
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @DisplayName("T1-Should successfully get route path when making GET request to endpoint - /routes/path/{id}")
    public void shouldSuccessfullyGetRoutePath() throws Exception {
        Long routeId = 1L;
        this.mockMvc.perform(MockMvcRequestBuilders.get(String.format("%s/path/%d", ROUTE_URL_PREFIX, routeId))
                        .contentType(contentType)).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThan(0))));
    }

    @Test
    @DisplayName("T2-Should throw entity not found (not found route) when making GET request to endpoint - /routes/path/{id}")
    public void shouldThrowEntityNotFoundGetRoutePath() throws Exception {
        String errorMessage = getEntityErrorMessage(NOT_EXIST_ENTITY.toString(), EntityType.ROUTE);
        this.mockMvc.perform(MockMvcRequestBuilders.get(String.format("%s/path/%d", ROUTE_URL_PREFIX, NOT_EXIST_ENTITY))
                        .contentType(contentType)).andExpect(status().isNotFound())
                .andExpect(result ->
                        assertTrue(result.getResolvedException() instanceof EntityNotFoundException)
                )
                .andExpect(result -> assertEquals(errorMessage, Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @ParameterizedTest
    @MethodSource("getLocationsForRoutesRequestWithLessThanTwoElements")
    @DisplayName("T3-Should throw bad request when locations has less than two elements when making POST request to endpoint - /routes/possible")
    @Rollback(true)
    public void shouldThrowBadRequestWhenLocationsHasLessThanTwoElements(LocationsForRoutesRequest locationsForRoutesRequest) throws Exception {
        String json = TestUtil.json(locationsForRoutesRequest);

        this.mockMvc.perform(MockMvcRequestBuilders.post(String.format("%s/possible", ROUTE_URL_PREFIX))
                .contentType(contentType).content(json)).andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("T4-Should throw bad request when longitude or latitude is less than 0 when making POST request to endpoint - /routes/possible")
    @Rollback(true)
    public void shouldThrowBadRequestWhenLngOrLatIsLessThanZero() throws Exception {
        String json = TestUtil.json(LONG_LAT_REQUESTS_TWO_ELEMENTS_WRONG_LAT);

        this.mockMvc.perform(MockMvcRequestBuilders.post(String.format("%s/possible", ROUTE_URL_PREFIX))
                .contentType(contentType).content(json)).andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @MethodSource("getLocationsForRoutesRequestWithMoreOrEqualThanTwoElements")
    @DisplayName("T5-Should successfully return list of possible routes via points when making POST request to endpoint - /routes/possible")
    @Rollback(true)
    public void shouldSuccessfullyReturnListOfPossibleRoutesViaPoints(LocationsForRoutesRequest locationsForRoutesRequest) throws Exception {
        String json = TestUtil.json(locationsForRoutesRequest);

        this.mockMvc.perform(MockMvcRequestBuilders.post(String.format("%s/possible", ROUTE_URL_PREFIX))
                        .contentType(contentType).content(json)).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(locationsForRoutesRequest.getLocationsForRouteRequest().size() - 1)));
    }

    private List<Arguments> getLocationsForRoutesRequestWithLessThanTwoElements() {

        return Arrays.asList(
                Arguments.arguments(new LocationsForRoutesRequest(LONG_LAT_REQUESTS_EMPTY)),
                Arguments.arguments(new LocationsForRoutesRequest(LONG_LAT_REQUESTS_ONE_ELEMENT))
        );
    }

    private List<Arguments> getLocationsForRoutesRequestWithMoreOrEqualThanTwoElements() {

        return Arrays.asList(
                Arguments.arguments(new LocationsForRoutesRequest(LONG_LAT_REQUESTS_TWO_ELEMENTS)),
                Arguments.arguments(new LocationsForRoutesRequest(LONG_LAT_REQUESTS_THREE_ELEMENTS))
        );
    }
}
