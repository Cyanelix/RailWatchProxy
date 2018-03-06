package com.cyanelix.railwatch.service;

import com.cyanelix.railwatch.domain.*;
import com.cyanelix.railwatch.entity.ScheduleEntity;
import com.cyanelix.railwatch.entity.UserEntity;
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
        UserEntity user = createUser();

        ScheduleEntity schedule = new ScheduleEntity(LocalTime.MIN, LocalTime.MAX, DayRange.ALL,
                FOO_TO_BAR.getFrom(), FOO_TO_BAR.getTo(), ScheduleState.ENABLED, user);

        // When...
        scheduleService.createSchedule(schedule);

        // Then...
        verify(scheduleRepository).save(schedule);
    }

    @Test
    public void singleScheduleActiveNow_checkTimes_routeLookedUp() {
        // Given...
        UserEntity user = createUser();

        ScheduleEntity activeSchedule = new ScheduleEntity(
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
        UserEntity user = createUser();

        ScheduleEntity activeSchedule = new ScheduleEntity(
                LocalTime.MIN, LocalTime.MAX, DayRange.ALL, FOO_TO_BAR.getFrom(), FOO_TO_BAR.getTo(),
                ScheduleState.ENABLED, user);
        ScheduleEntity inactiveSchedule = new ScheduleEntity(
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
        UserEntity user = createUser();

        ScheduleEntity schedule = new ScheduleEntity(
                LocalTime.MIN, LocalTime.MAX, DayRange.ALL, FOO_TO_BAR.getFrom(), FOO_TO_BAR.getTo(),
                ScheduleState.ENABLED, user);
        given(scheduleRepository.findAll()).willReturn(
                Collections.singletonList(schedule));

        // When...
        Set<ScheduleEntity> schedules = scheduleService.getSchedules();

        // Then...
        assertThat(schedules, hasSize(1));
        assertThat(schedules.contains(schedule), is(true));
    }

    @Test
    public void oneEnabledSchedule_getEnabledSchedules_enabledScheduleReturned() {
        // Given...
        UserEntity user = createUser();
        ScheduleEntity scheduleEntity =
                new ScheduleEntity(LocalTime.MIN, LocalTime.MAX, DayRange.ALL, Station.of("FOO"), Station.of("BAR"),
                        ScheduleState.ENABLED, user);
        given(scheduleRepository.findByStateIs(ScheduleState.ENABLED)).willReturn(Collections.singletonList(scheduleEntity));

        // When...
        Stream<ScheduleEntity> scheduleStream = scheduleService.getEnabledSchedules();

        // Then...
        List<ScheduleEntity> schedules = scheduleStream.collect(Collectors.toList());
        assertThat(schedules.size(), is(1));
        assertThat(schedules.get(0), is(scheduleEntity));
    }

    private UserEntity createUser() {
        return new UserEntity(UserId.generate().get(), NotificationTarget.of("notification-target").getTargetAddress(), UserState.ENABLED);
    }
}
