package com.cyanelix.railwatch.dto;

import com.cyanelix.railwatch.domain.*;
import org.junit.Test;

import java.time.LocalTime;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ScheduleDTOTest {
    @Test
    public void dto_toSchedule() {
        // Given...
        ScheduleDTO scheduleDTO = new ScheduleDTO();
        scheduleDTO.setStartTime("12:00");
        scheduleDTO.setEndTime("00:00");
        scheduleDTO.setFromStation("FOO");
        scheduleDTO.setToStation("BAR");
        scheduleDTO.setNotificationTarget("notification-to");
        scheduleDTO.setDays(allDaysStringArray());

        // When...
        Schedule schedule = scheduleDTO.toSchedule();

        // Then...
        assertThat(schedule.getStartTime(), is(LocalTime.NOON));
        assertThat(schedule.getEndTime(), is(LocalTime.MIDNIGHT));
        assertThat(schedule.getJourney().getFrom(), is(Station.of("FOO")));
        assertThat(schedule.getJourney().getTo(), is(Station.of("BAR")));
        assertThat(schedule.getNotificationTarget(), is(NotificationTarget.of("notification-to")));
        assertThat(schedule.getDayRange(), is(DayRange.ALL));
    }

    @Test
    public void dto_ScheduleConstructor() {
        // Given...
        Schedule schedule = Schedule.of(
                LocalTime.NOON, LocalTime.MIDNIGHT, DayRange.ALL, Journey.of(Station.of("FOO"), Station.of("BAR")),
                NotificationTarget.of("notification-to"), ScheduleState.ENABLED);

        // When...
        ScheduleDTO dto = new ScheduleDTO(schedule);

        // Then...
        assertThat(dto.getStartTime(), is("12:00"));
        assertThat(dto.getEndTime(), is("00:00"));
        assertThat(dto.getFromStation(), is("FOO"));
        assertThat(dto.getToStation(), is("BAR"));
        assertThat(dto.getNotificationTarget(), is("notification-to"));
        assertThat(dto.getDays(), is(allDaysStringArray()));
    }

    private String[] allDaysStringArray() {
        return new String[] {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
    }
}
