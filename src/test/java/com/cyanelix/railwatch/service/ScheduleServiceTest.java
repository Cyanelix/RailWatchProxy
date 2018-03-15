package com.cyanelix.railwatch.service;

import com.cyanelix.railwatch.domain.*;
import com.cyanelix.railwatch.entity.Schedule;
import com.cyanelix.railwatch.entity.User;
import com.cyanelix.railwatch.repository.ScheduleRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ScheduleServiceTest {
    private static final Journey FOO_TO_BAR = Journey.of(Station.of("FOO"), Station.of("BAR"));

    @Mock
    private TrainTimesService trainTimesService;

    @Mock
    private NotificationService notificationService;

    @Mock
    private ScheduleRepository scheduleRepository;

    private ScheduleService scheduleService;

    @Before
    public void setup() {
        scheduleService = new ScheduleService(trainTimesService, notificationService, scheduleRepository, Clock.fixed(Instant.parse("2017-01-01T10:30:00Z"), ZoneId.systemDefault()));
    }

    @Test
    public void createSchedule_savedInRepo() {
        // Given...
        User user = createUser();

        Schedule schedule = new Schedule(LocalTime.MIN, LocalTime.MAX, DayRange.ALL,
                FOO_TO_BAR.getFrom(), FOO_TO_BAR.getTo(), ScheduleState.ENABLED, user);

        // When...
        scheduleService.createSchedule(schedule);

        // Then...
        verify(scheduleRepository).save(schedule);
    }

    @Test
    public void singleScheduleActiveNow_checkTimes_routeLookedUp() {
        // Given...
        User user = createUser();

        Schedule activeSchedule = new Schedule(
                LocalTime.MIN, LocalTime.MAX, DayRange.ALL, FOO_TO_BAR.getFrom(), FOO_TO_BAR.getTo(),
                ScheduleState.ENABLED, user);
        given(scheduleRepository.findByStateIs(ScheduleState.ENABLED)).willReturn(
                Collections.singletonList(activeSchedule));

        // When...
        scheduleService.checkTimes();

        // Then...
        verify(trainTimesService).lookupTrainTimes(any(), any());
        verify(notificationService).sendNotification(eq(activeSchedule), any());
    }

    @Test
    public void oneActiveOneInactiveSchedule_checkTimes_onlyActiveRouteLookedUp() {
        // Given...
        User user = createUser();

        Schedule activeSchedule = new Schedule(
                LocalTime.MIN, LocalTime.MAX, DayRange.ALL, FOO_TO_BAR.getFrom(), FOO_TO_BAR.getTo(),
                ScheduleState.ENABLED, user);
        Schedule inactiveSchedule = new Schedule(
                LocalTime.MAX, LocalTime.MIN, DayRange.ALL, Station.of("XXX"), Station.of("ZZZ"),
                ScheduleState.ENABLED, user);
        given(scheduleRepository.findByStateIs(ScheduleState.ENABLED)).willReturn(
                Arrays.asList(activeSchedule, inactiveSchedule));

        // When...
        scheduleService.checkTimes();

        // Then...
        verify(trainTimesService).lookupTrainTimes(Station.of("FOO"), Station.of("BAR"));
        verify(trainTimesService, never()).lookupTrainTimes(Station.of("XXX"), Station.of("ZZZ"));

        verify(notificationService).sendNotification(eq(activeSchedule), any());
        verify(notificationService, never()).sendNotification(eq(inactiveSchedule), any());
    }

    @Test
    public void noEnabledSchedules_checkTimes_notLookedUp() {
        // Given...
        given(scheduleRepository.findByStateIs(ScheduleState.ENABLED)).willReturn(Collections.emptyList());

        // When...
        scheduleService.checkTimes();

        // Then...
        verify(trainTimesService, never()).lookupTrainTimes(any(), any());
        verify(notificationService, never()).sendNotification(any(NotificationTarget.class), any());
    }

    @Test
    public void singleSchedule_getSchedules() {
        // Given...
        User user = createUser();

        Schedule schedule = new Schedule(
                LocalTime.MIN, LocalTime.MAX, DayRange.ALL, FOO_TO_BAR.getFrom(), FOO_TO_BAR.getTo(),
                ScheduleState.ENABLED, user);
        given(scheduleRepository.findAll()).willReturn(
                Collections.singletonList(schedule));

        // When...
        Set<Schedule> schedules = scheduleService.getSchedules();

        // Then...
        assertThat(schedules, hasSize(1));
        assertThat(schedules.contains(schedule), is(true));
    }

    @Test
    public void scheduleExistsForUser_getSchedulesForUser_success() {
        // Given...
        User user = createUser();

        Schedule schedule = new Schedule(LocalTime.MIN, LocalTime.MAX, DayRange.ALL,
                FOO_TO_BAR.getFrom(), FOO_TO_BAR.getTo(), ScheduleState.ENABLED, user);

        given(scheduleRepository.findByUser(user)).willReturn(Collections.singletonList(schedule));

        // When...
        List<Schedule> userSchedules = scheduleService.getSchedulesForUser(user);

        // Then...
        assertThat(userSchedules, hasSize(1));
    }

    private User createUser() {
        return new User(UserId.generate(), NotificationTarget.of("notification-target").getTargetAddress(), UserState.ENABLED);
    }
}
