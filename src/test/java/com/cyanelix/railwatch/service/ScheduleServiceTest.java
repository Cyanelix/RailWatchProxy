package com.cyanelix.railwatch.service;

import com.cyanelix.railwatch.domain.*;
import com.cyanelix.railwatch.entity.ScheduleEntity;
import com.cyanelix.railwatch.repository.ScheduleRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
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
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        Schedule schedule = Schedule.of(
                LocalTime.MIN, LocalTime.MAX, DayRange.ALL, FOO_TO_BAR,
                NotificationTarget.of("notification-target"), ScheduleState.ENABLED);

        // When...
        scheduleService.createSchedule(schedule);

        // Then...
        ArgumentCaptor<ScheduleEntity> scheduleEntityCaptor = ArgumentCaptor.forClass(ScheduleEntity.class);
        verify(scheduleRepository).save(scheduleEntityCaptor.capture());

        ScheduleEntity scheduleEntity = scheduleEntityCaptor.getValue();
        assertThat(scheduleEntity.getStartTime(), is(schedule.getStartTime()));
        assertThat(scheduleEntity.getEndTime(), is(schedule.getEndTime()));
        assertThat(scheduleEntity.getFromStation(), is(schedule.getJourney().getFrom().getStationCode()));
        assertThat(scheduleEntity.getToStation(), is(schedule.getJourney().getTo().getStationCode()));
        assertThat(scheduleEntity.getNotificationTarget(), is(schedule.getNotificationTarget().getTargetAddress()));
    }

    @Test
    public void singleScheduleActiveNow_checkTimes_routeLookedUp() {
        // Given...
        Schedule activeSchedule = Schedule.of(
                LocalTime.MIN, LocalTime.MAX, DayRange.ALL, FOO_TO_BAR,
                NotificationTarget.of("target"), ScheduleState.ENABLED);
        given(scheduleRepository.findByStateIs(ScheduleState.ENABLED)).willReturn(
                Collections.singletonList(ScheduleEntity.of(activeSchedule)));

        // When...
        scheduleService.checkTimes();

        // Then...
        verify(trainTimesService).lookupTrainTimes(any(), any());
        verify(notificationService).sendNotification(eq(activeSchedule), any());
    }

    @Test
    public void oneActiveOneInactiveSchedule_checkTimes_onlyActiveRouteLookedUp() {
        // Given...
        Schedule activeSchedule = Schedule.of(
                LocalTime.MIN, LocalTime.MAX, DayRange.ALL, FOO_TO_BAR,
                NotificationTarget.of("target"), ScheduleState.ENABLED);
        Schedule inactiveSchedule = Schedule.of(
                LocalTime.MAX, LocalTime.MIN, DayRange.ALL, Journey.of(Station.of("XXX"), Station.of("ZZZ")),
                NotificationTarget.of("target"), ScheduleState.ENABLED);
        given(scheduleRepository.findByStateIs(ScheduleState.ENABLED)).willReturn(
                Arrays.asList(ScheduleEntity.of(activeSchedule), ScheduleEntity.of(inactiveSchedule)));

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
        verify(notificationService, never()).sendNotification(any(), any());
    }

    @Test
    public void singleSchedule_getSchedules() {
        // Given...
        Schedule schedule = Schedule.of(
                LocalTime.MIN, LocalTime.MAX, DayRange.ALL, FOO_TO_BAR,
                NotificationTarget.of("notification-to"), ScheduleState.ENABLED);
        given(scheduleRepository.findAll()).willReturn(
                Collections.singletonList(ScheduleEntity.of(schedule)));

        // When...
        Set<Schedule> schedules = scheduleService.getSchedules();

        // Then...
        assertThat(schedules, hasSize(1));
        assertThat(schedules.contains(schedule), is(true));
    }

    @Test
    public void oneEnabledSchedule_getEnabledSchedules_enabledScheduleReturned() {
        // Given...
        ScheduleEntity scheduleEntity =
                new ScheduleEntity(LocalTime.MIN, LocalTime.MAX, DayRange.ALL, "FOO", "BAR", "notification-to", ScheduleState.ENABLED);
        given(scheduleRepository.findByStateIs(ScheduleState.ENABLED)).willReturn(Collections.singletonList(scheduleEntity));

        // When...
        Stream<Schedule> scheduleStream = scheduleService.getEnabledSchedules();

        // Then...
        List<Schedule> schedules = scheduleStream.collect(Collectors.toList());
        assertThat(schedules.size(), is(1));
        assertThat(schedules.get(0), is(Schedule.of(scheduleEntity)));
    }

    @Test
    public void disableSchedulesForNotificationTarget_scheduleStatesUpdated() {
        // Given...
        NotificationTarget notificationTarget = NotificationTarget.of("target");

        ScheduleEntity scheduleEntity =
                new ScheduleEntity(LocalTime.MIN, LocalTime.MAX, DayRange.ALL, "FOO", "BAR", "notification-to", ScheduleState.ENABLED);
        List<ScheduleEntity> scheduleEntities = Collections.singletonList(scheduleEntity);

        given(scheduleRepository.findByNotificationTarget(notificationTarget.getTargetAddress()))
                .willReturn(scheduleEntities);

        // When...
        scheduleService.disableSchedulesForNotificationTarget(notificationTarget);

        // Then...
        verify(scheduleRepository).save(scheduleEntities);
        assertThat(scheduleEntity.getState(), is(ScheduleState.DISABLED));
    }
}
