package com.cyanelix.railwatch.domain;

import com.cyanelix.railwatch.service.NotificationService;
import com.cyanelix.railwatch.service.TrainTimesService;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.*;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        Schedule schedule = Schedule.of(LocalTime.MIN, LocalTime.MAX, DayRange.ALL, null, null);

        // When...
        boolean isActive = schedule.isActive(LocalDateTime.of(2017, 1, 1, 12, 0));

        // Then...
        assertThat(isActive, is(true));
    }

    @Test
    public void timeBeforeSchedule_isActive_returnsFalse() {
        // Given...
        Schedule schedule = Schedule.of(LocalTime.of(19, 0), LocalTime.of(20, 0), DayRange.ALL, null, null);

        // When...
        boolean isActive = schedule.isActive(LocalDateTime.of(2017, 1, 1, 12, 0));

        // Then...
        assertThat(isActive, is(false));
    }

    @Test
    public void timeAfterSchedule_isActive_returnsFalse() {
        // Given...
        Schedule schedule = Schedule.of(LocalTime.of(9, 0), LocalTime.of(10, 0), DayRange.ALL, null, null);

        // When...
        boolean isActive = schedule.isActive(LocalDateTime.of(2017, 1, 1, 12, 0));

        // Then...
        assertThat(isActive, is(false));
    }

    @Test
    public void timeWithinScheduleButOutsideDays_isActive_returnsFalse() {
        // Given...
        Schedule schedule = Schedule.of(LocalTime.of(9, 0), LocalTime.of(10, 0), DayRange.of(DayOfWeek.MONDAY), null, null);

        // When...
        // 2017-01-01 was a Sunday.
        boolean active = schedule.isActive(LocalDateTime.of(2017, Month.JANUARY, 1, 9, 30));

        // Then...
        assertThat(active, is(false));
    }

    @Test
    public void schedule_lookupAndNotify() {
        // Given...
        Station from = Station.of("FOO");
        Station to = Station.of("BAR");

        Schedule schedule = Schedule.of(null, null, DayRange.ALL, Journey.of(from, to), null);

        List<TrainTime> trainTimes = Collections.singletonList(TrainTime.of(LocalTime.NOON, Optional.empty(), ""));
        given(mockTrainTimesService.lookupTrainTimes(from, to)).willReturn(trainTimes);

        // When...
        schedule.lookupAndNotifyTrainTimes(mockTrainTimesService, mockNotificationService);

        // Then...
        verify(mockTrainTimesService).lookupTrainTimes(from, to);
        verify(mockNotificationService).sendNotification(schedule, trainTimes);
    }

    @Test
    public void scheduleForAllDays_getDayNames() {
        // Given...
        Schedule schedule = Schedule.of(null, null, DayRange.ALL, null, null);

        // When...
        Stream<String> dayNames = schedule.getDayNames();

        // Then...
        Set<String> names = dayNames.collect(Collectors.toSet());
        assertThat(names.size(), is(7));
        assertThat(names.contains("Monday"), is(true));
        assertThat(names.contains("Tuesday"), is(true));
        assertThat(names.contains("Wednesday"), is(true));
        assertThat(names.contains("Thursday"), is(true));
        assertThat(names.contains("Friday"), is(true));
        assertThat(names.contains("Saturday"), is(true));
        assertThat(names.contains("Sunday"), is(true));
    }

    @Test
    public void schedulePopulatedWithValues_toString_containsExpectedValues() {
        // Given...
        Schedule schedule = Schedule.of(LocalTime.of(1, 1), LocalTime.of(2, 2),
                DayRange.ALL, Journey.of(Station.of("ABC"), Station.of("DEF")), NotificationTarget.of("123"));

        // When...
        String string = schedule.toString();

        // Then...
        assertThat(string, is("ABC -> DEF; Monday, Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday @ 01:01 -> 02:02"));
    }

    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(Schedule.class).verify();
    }
}
