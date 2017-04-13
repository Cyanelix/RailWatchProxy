package com.cyanelix.railwatch.domain;

import org.junit.Test;

import java.time.LocalTime;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class ScheduleTest {
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
}