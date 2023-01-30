package com.example.serbUber.server.controller;

import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.exception.EntityType;
import com.example.serbUber.server.controller.helper.TestUtil;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.Objects;

import static com.example.serbUber.exception.EntityType.getEntityErrorMessage;
import static com.example.serbUber.server.controller.helper.ControllerConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.DisplayName.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
public class DrivingNotificationControllerTest {

    private static final String DRIVING_NOTIFICATION_URL_PREFIX = "/driving-notifications";

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeAll
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @DisplayName("T1-Should successfully get driving notification when making GET request to endpoint - /driving-notifications/{id}")
    @WithMockUser(roles="REGULAR_USER")
    public void shouldSuccessfullyGetDrivingNotification() throws Exception {
        Long drivingNotificationId = 1L;
        this.mockMvc.perform(MockMvcRequestBuilders.get(String.format("%s/%d", DRIVING_NOTIFICATION_URL_PREFIX, drivingNotificationId))
                .contentType(contentType)).andExpect(status().isOk())
            .andExpect(jsonPath("$.price").value(10))
            .andExpect(jsonPath("$.duration").value(5))
            .andExpect(jsonPath("$.passengers.size()").value(1));
    }

    @Test
    @DisplayName("T2-Should throw entity not found (not found driving notification) when making GET request to endpoint - /driving-notifications/{id}")
    @WithMockUser(roles="REGULAR_USER")
    public void shouldThrowEntityNotFoundGetDrivingNotification() throws Exception {

        String errorMessage = getEntityErrorMessage(NOT_EXIST_ENTITY.toString(), EntityType.DRIVING_NOTIFICATION);
        this.mockMvc.perform(MockMvcRequestBuilders.get(String.format("%s/%d", DRIVING_NOTIFICATION_URL_PREFIX, NOT_EXIST_ENTITY))
                .contentType(contentType)).andExpect(status().isNotFound())
            .andExpect(result ->
                assertTrue(result.getResolvedException() instanceof EntityNotFoundException)
            )
            .andExpect(result -> assertEquals(errorMessage, Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }
}
