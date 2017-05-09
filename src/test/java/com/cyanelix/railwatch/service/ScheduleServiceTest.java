package com.cyanelix.railwatch.service;

import com.cyanelix.railwatch.domain.Journey;
import com.cyanelix.railwatch.domain.NotificationTarget;
import com.cyanelix.railwatch.domain.Schedule;
import com.cyanelix.railwatch.domain.Station;
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
import java.util.Set;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ScheduleServiceTest {
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
        Schedule schedule = Schedule.of(LocalTime.MIN, LocalTime.MAX, Journey.of(Station.of("FOO"), Station.of("BAR")), NotificationTarget.of("notification-target"));

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
        Schedule activeSchedule = Schedule.of(LocalTime.MIN, LocalTime.MAX, Journey.of(Station.of("FOO"), Station.of("BAR")), NotificationTarget.of("target"));
        given(scheduleRepository.findAll()).willReturn(Collections.singletonList(ScheduleEntity.of(activeSchedule)));

        // When...
        scheduleService.checkTimes();

        // Then...
        verify(trainTimesService).lookupTrainTimes(any(), any());
        verify(notificationService).sendNotification(eq(activeSchedule), any());
    }

    @Test
    public void oneActiveOneInactiveSchedule_checkTimes_onlyActiveRouteLookedUp() {
        // Given...
        Schedule activeSchedule = Schedule.of(LocalTime.MIN, LocalTime.MAX, Journey.of(Station.of("FOO"), Station.of("BAR")), NotificationTarget.of("target"));
        Schedule inactiveSchedule = Schedule.of(LocalTime.MAX, LocalTime.MIN, Journey.of(Station.of("XXX"), Station.of("ZZZ")), NotificationTarget.of("target"));
        given(scheduleRepository.findAll()).willReturn(Arrays.asList(ScheduleEntity.of(activeSchedule), ScheduleEntity.of(inactiveSchedule)));

        // When...
        scheduleService.checkTimes();

        // Then...
        verify(trainTimesService).lookupTrainTimes(Station.of("FOO"), Station.of("BAR"));
        verify(trainTimesService, never()).lookupTrainTimes(Station.of("XXX"), Station.of("ZZZ"));

        verify(notificationService).sendNotification(eq(activeSchedule), any());
        verify(notificationService, never()).sendNotification(eq(inactiveSchedule), any());
    }

    @Test
    public void singleSchedule_getSchedules() {
        // Given...
        Schedule schedule = Schedule.of(LocalTime.MIN, LocalTime.MAX, Journey.of(Station.of("FOO"), Station.of("BAR")), NotificationTarget.of("notification-to"));
        given(scheduleRepository.findAll()).willReturn(Collections.singletonList(ScheduleEntity.of(schedule)));

        // When...
        Set<Schedule> schedules = scheduleService.getSchedules();

        // Then...
        assertThat(schedules, hasSize(1));
        assertThat(schedules.contains(schedule), is(true));
    }
}
