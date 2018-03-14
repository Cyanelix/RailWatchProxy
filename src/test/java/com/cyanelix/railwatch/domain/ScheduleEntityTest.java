package com.cyanelix.railwatch.domain;

import com.cyanelix.railwatch.entity.ScheduleEntity;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class ScheduleEntityTest {
    @Test
    public void timeWithinSchedule_isActive_returnsTrue() {
        // Given...
        ScheduleEntity schedule = new ScheduleEntity(LocalTime.MIN, LocalTime.MAX, DayRange.ALL, null, null, ScheduleState.ENABLED, null);

        // When...
        boolean isActive = schedule.isActive(LocalDateTime.of(2017, 1, 1, 12, 0));

        // Then...
        assertThat(isActive, is(true));
    }

    @Test
    public void timeBeforeSchedule_isActive_returnsFalse() {
        // Given...
        ScheduleEntity schedule = new ScheduleEntity(LocalTime.of(19, 0), LocalTime.of(20, 0),
                DayRange.ALL, null, null, ScheduleState.ENABLED, null);

        // When...
        boolean isActive = schedule.isActive(LocalDateTime.of(2017, 1, 1, 12, 0));

        // Then...
        assertThat(isActive, is(false));
    }

    @Test
    public void timeAfterSchedule_isActive_returnsFalse() {
        // Given...
        ScheduleEntity schedule = new ScheduleEntity(LocalTime.of(9, 0), LocalTime.of(10, 0),
                DayRange.ALL, null, null, ScheduleState.ENABLED, null);

        // When...
        boolean isActive = schedule.isActive(LocalDateTime.of(2017, 1, 1, 12, 0));

        // Then...
        assertThat(isActive, is(false));
    }

    @Test
    public void timeWithinScheduleButOutsideDays_isActive_returnsFalse() {
        // Given...
        ScheduleEntity schedule = new ScheduleEntity(LocalTime.of(9, 0), LocalTime.of(10, 0),
                DayRange.of(DayOfWeek.MONDAY), null, null, ScheduleState.ENABLED, null);

        // When...
        // 2017-01-01 was a Sunday.
        boolean active = schedule.isActive(LocalDateTime.of(2017, Month.JANUARY, 1, 9, 30));

        // Then...
        assertThat(active, is(false));
    }

    @Test
    public void schedulePopulatedWithValues_toString_containsExpectedValues() {
        // Given...
        ScheduleEntity schedule = new ScheduleEntity(
                LocalTime.of(1, 1), LocalTime.of(2, 2), DayRange.ALL,
                Station.of("ABC"), Station.of("DEF"), ScheduleState.ENABLED, null);

        // When...
        String string = schedule.toString();

        // Then...
        assertThat(string, is("ABC -> DEF; Monday, Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday @ 01:01 -> 02:02"));
    }

    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(ScheduleEntity.class)
                .suppress(Warning.NONFINAL_FIELDS)
                .withIgnoredFields("id")
                .verify();
    }
}
