package com.cyanelix.railwatch.dto;

import com.cyanelix.railwatch.domain.Schedule;
import com.cyanelix.railwatch.domain.Station;
import org.junit.Test;

import java.time.LocalTime;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

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

        // When...
        Schedule schedule = scheduleDTO.toSchedule();

        // Then...
        assertThat(schedule.getStartTime(), is(LocalTime.NOON));
        assertThat(schedule.getEndTime(), is(LocalTime.MIDNIGHT));
        assertThat(schedule.getFromStation(), is(Station.of("FOO")));
        assertThat(schedule.getToStation(), is(Station.of("BAR")));
        assertThat(schedule.getNotificationTarget(), is("notification-to"));
    }

    @Test
    public void dto_ScheduleConstructor() {
        // Given...
        Schedule schedule = new Schedule();
        schedule.setStartTime(LocalTime.NOON);
        schedule.setEndTime(LocalTime.MIDNIGHT);
        schedule.setFromStation(Station.of("FOO"));
        schedule.setToStation(Station.of("BAR"));
        schedule.setNotificationTarget("notification-to");

        // When...
        ScheduleDTO dto = new ScheduleDTO(schedule);

        // Then...
        assertThat(dto.getStartTime(), is("12:00"));
        assertThat(dto.getEndTime(), is("00:00"));
        assertThat(dto.getFromStation(), is("FOO"));
        assertThat(dto.getToStation(), is("BAR"));
        assertThat(dto.getNotificationTarget(), is("notification-to"));
    }
}
