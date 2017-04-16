package com.cyanelix.railwatch.domain;

import com.cyanelix.railwatch.service.NotificationService;
import com.cyanelix.railwatch.service.TrainTimesService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ScheduleTest {
    @Mock
    private TrainTimesService mockTrainTimesService;

    @Mock
    private NotificationService mockNotificationService;

    @Test
    public void timeWithinSchedule_isActive_returnsTrue() {
        // Given...
        Schedule schedule = new Schedule();
        schedule.setStartTime(LocalTime.MIN);
        schedule.setEndTime(LocalTime.MAX);

        // When...
        boolean isActive = schedule.isActive(LocalTime.NOON);

        // Then...
        assertThat(isActive, is(true));
    }

    @Test
    public void timeBeforeSchedule_isActive_returnsFalse() {
        // Given...
        Schedule schedule = new Schedule();
        schedule.setStartTime(LocalTime.of(19, 0));
        schedule.setEndTime(LocalTime.of(20, 0));

        // When...
        boolean isActive = schedule.isActive(LocalTime.NOON);

        // Then...
        assertThat(isActive, is(false));
    }

    @Test
    public void timeAfterSchedule_isActive_returnsFalse() {
        // Given...
        Schedule schedule = new Schedule();
        schedule.setStartTime(LocalTime.of(9, 0));
        schedule.setEndTime(LocalTime.of(10, 0));

        // When...
        boolean isActive = schedule.isActive(LocalTime.NOON);

        // Then...
        assertThat(isActive, is(false));
    }

    @Test
    public void schedule_lookupAndNotify() {
        // Given...
        Station from = Station.of("FOO");
        Station to = Station.of("BAR");

        Schedule schedule = new Schedule();
        schedule.setFromStation(from);
        schedule.setToStation(to);

        List<TrainTime> trainTimes = Collections.singletonList(TrainTime.of(LocalTime.NOON, Optional.empty(), ""));
        given(mockTrainTimesService.lookupTrainTimes(from, to)).willReturn(trainTimes);

        // When...
        schedule.lookupAndNotifyTrainTimes(mockTrainTimesService, mockNotificationService);

        // Then...
        verify(mockTrainTimesService).lookupTrainTimes(from, to);
        verify(mockNotificationService).sendNotification(schedule, trainTimes);
    }
}
