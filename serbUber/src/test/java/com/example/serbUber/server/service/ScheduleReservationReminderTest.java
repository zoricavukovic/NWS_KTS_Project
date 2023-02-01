package com.example.serbUber.server.service;

import com.example.serbUber.model.Driving;
import com.example.serbUber.service.DrivingService;
import com.example.serbUber.service.ScheduleReservationReminder;
import com.example.serbUber.service.WebSocketService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static com.example.serbUber.server.service.helper.DrivingConstants.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.DisplayName.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ScheduleReservationReminderTest {

    @Mock
    private DrivingService drivingService;

    @Mock
    private WebSocketService webSocketService;

    @InjectMocks
    private ScheduleReservationReminder scheduleReservationReminder;

    @BeforeAll
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("T1-Should send reminder for driving when driving is 15 minutes before start and there is no previous reminders")
    public void shouldSendReminderWhenDrivingIsStartingSoonAndThereIsNoPreviousReminder() {

        when(drivingService.getAllReservations()).thenReturn(
            Arrays.asList(DRIVING_SHOULD_RECEIVE_FIRST_NOTIFICATION_1, DRIVING_SHOULD_RECEIVE_FIRST_NOTIFICATION_2)
        );

        doNothing().when(webSocketService).sendReservationReminder(anySet(), anyInt());
        when(drivingService.save(DRIVING_SHOULD_RECEIVE_FIRST_NOTIFICATION_1)).thenReturn(new Driving());
        when(drivingService.save(DRIVING_SHOULD_RECEIVE_FIRST_NOTIFICATION_2)).thenReturn(new Driving());
        scheduleReservationReminder.remindPassengerForReservation();

        verify(webSocketService, times(2)).sendReservationReminder(anySet(), anyInt());
        verify(drivingService, times(1)).save(DRIVING_SHOULD_RECEIVE_FIRST_NOTIFICATION_1);
        verify(drivingService, times(1)).save(DRIVING_SHOULD_RECEIVE_FIRST_NOTIFICATION_2);
    }

    @Test
    @DisplayName("T2-Should send reminder for driving when there is previous reminder")
    public void shouldSendReminderWhenDrivingIsStartingSoonAndThereIsPreviousReminder() {

        when(drivingService.getAllReservations()).thenReturn(
                List.of(DRIVING_SHOULD_RECEIVE_FIRST_NOTIFICATION_3)
        );

        doNothing().when(webSocketService).sendReservationReminder(anySet(), anyInt());
        when(drivingService.save(DRIVING_SHOULD_RECEIVE_FIRST_NOTIFICATION_3)).thenReturn(new Driving());
        when(drivingService.save(DRIVING_SHOULD_RECEIVE_FIRST_NOTIFICATION_3)).thenReturn(new Driving());
        scheduleReservationReminder.remindPassengerForReservation();

        verify(webSocketService, times(1)).sendReservationReminder(anySet(), anyInt());
        verify(drivingService, times(1)).save(DRIVING_SHOULD_RECEIVE_FIRST_NOTIFICATION_3);
    }

    @Test
    @DisplayName("T3-Should not send reminder, time difference is bigger than 15 minutes")
    public void shouldNotSendReminderTooBigTimeDifference() {

        when(drivingService.getAllReservations()).thenReturn(List.of(DRIVING_SHOULD_NOT_RECEIVE_REMINDER));

        scheduleReservationReminder.remindPassengerForReservation();

        verify(webSocketService, never()).sendReservationReminder(anySet(), anyInt());
        verify(drivingService, never()).save(DRIVING_SHOULD_NOT_RECEIVE_REMINDER);
    }
}
