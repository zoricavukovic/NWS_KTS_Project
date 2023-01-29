package com.example.serbUber.controller;

import com.example.serbUber.dto.DrivingDTO;
import com.example.serbUber.exception.EntityNotFoundException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.Objects;

import static com.example.serbUber.model.DrivingStatus.REJECTED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class DrivingControllerTest {

    private static final String DRIVING_URL_PREFIX = "/drivings";

    private MockMvc mockMvc;
    private final MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
        MediaType.APPLICATION_JSON.getSubtype());

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }


    @Test
    @DisplayName("Should successfully reject driving when making PUT request to endpoint - /drivings/reject/{drivingId}")
    @WithMockUser(roles="DRIVER")
    @Rollback(true)
    public void shouldSuccessfullyRejectDriving() throws Exception {

        String reason = "reason";
        Long drivingId = 1L;
        this.mockMvc.perform(MockMvcRequestBuilders.put(String.format("%s/reject/%d", DRIVING_URL_PREFIX, drivingId))
            .contentType(contentType).content(reason)).andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(drivingId))
            .andExpect(jsonPath("$.drivingStatus").value(REJECTED.toString()));
    }

    @Test
    @DisplayName("Should throw entity not found (not found driving) when making PUT request to endpoint - /drivings/reject/{drivingId}")
    @WithMockUser(roles="DRIVER")
    @Rollback(true)
    public void shouldThrowEntityNotFoundRejectDriving() throws Exception {

        String reason = "reason";
        Long drivingId = 100L;
        String errorMessage = String.format("Driving: %d is not found", drivingId);
        this.mockMvc.perform(MockMvcRequestBuilders.put(String.format("%s/reject/%d", DRIVING_URL_PREFIX, drivingId))
                .contentType(contentType).content(reason)).andExpect(status().isNotFound())
            .andExpect(result ->
                assertTrue(result.getResolvedException() instanceof EntityNotFoundException)
            )
            .andExpect(result -> assertEquals(errorMessage, Objects.requireNonNull(result.getResolvedException()).getMessage()));

    }

    @Test
    @DisplayName("Should throw validation error when reason is too long when making PUT request to endpoint - /drivings/reject/{drivingId}")
    @WithMockUser(roles="DRIVER")
    @Rollback(true)
    public void shouldThrowValidationErrorWhenMissingReasonRejectDriving() throws Exception {
        Long drivingId = 1L;
        String tooLongReason = "AKAKKSAKSDAJDJWDHUJASDASKJDKASLJDKJAKDJKSAJDKASJDKA";
        String errorMessage = "rejectDriving.reason: Entered reason must contain less than 50 letters.";
        this.mockMvc.perform(MockMvcRequestBuilders.put(String.format("%s/reject/%d", DRIVING_URL_PREFIX, drivingId))
            .contentType(contentType).content(tooLongReason)).andExpect(status().isBadRequest())
            .andExpect(result -> assertEquals(errorMessage, Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }
}
