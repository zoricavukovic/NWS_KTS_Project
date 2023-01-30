package com.example.serbUber.server.controller;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static com.example.serbUber.server.controller.helper.ControllerConstants.NUM_OF_SEATS_SUV;
import static com.example.serbUber.server.controller.helper.ControllerConstants.VEHICLE_TYPE_SUV;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.DisplayName.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
public class VehicleTypeInfoControllerTest {

    private static final String VEHICLE_TYPE_INFO_URL_PREFIX = "/vehicle-type-infos";

    private MockMvc mockMvc;
    private final MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype());

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeAll
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }


    @Test
    @DisplayName("T1-Should return price for vehicle type and route when making GET request to endpoint - /price/{vehicleType}/{kilometers}")
    @WithMockUser(roles = {"DRIVER", "REGULAR_USER", "ADMIN"})
    @Rollback(true)
    public void getPriceForSelectedRouteAndVehicle_returnPriceForVehicleTypeAndRoute() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get(
                                String.format("%s/price/%s/%d", VEHICLE_TYPE_INFO_URL_PREFIX, VEHICLE_TYPE_SUV, 3000))
                        .contentType(contentType).content("")).andExpect(status().isOk())
                .andExpect(jsonPath("$").value(5.0));
    }

    @Test
    @DisplayName("T2-Should return vehicle type info and route when making GET request to endpoint - /{vehicleType}")
    @WithMockUser(roles = {"DRIVER", "REGULAR_USER", "ADMIN"})
    @Rollback(true)
    public void getVehicleByVehicleType_returnVehicleTypeInfo() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get(
                                String.format("%s/%s", VEHICLE_TYPE_INFO_URL_PREFIX, VEHICLE_TYPE_SUV))
                        .contentType(contentType).content("")).andExpect(status().isOk())
                .andExpect(jsonPath("$.numOfSeats").value(NUM_OF_SEATS_SUV))
                .andExpect(jsonPath("$.vehicleType").value(VEHICLE_TYPE_SUV));
    }


}
