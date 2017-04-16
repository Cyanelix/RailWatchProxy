package com.cyanelix.railwatch.domain;

import org.junit.Test;

import java.time.LocalTime;
import java.util.Optional;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class TrainTimeTest {
    @Test
    public void onTime_toString_justShowScheduledTime() {
        // Given...
        TrainTime trainTime = TrainTime.of(LocalTime.NOON, Optional.of(LocalTime.NOON), "On time");

        // When...
        String string = trainTime.toString();

        // Then...
        assertThat(string, is("12:00"));
    }

    @Test
    public void delayed_toString_showScheduledAndExpectedTimes() {
        // Given...
        TrainTime trainTime = TrainTime.of(LocalTime.NOON, Optional.of(LocalTime.of(12, 1)), "");

        // When...
        String string = trainTime.toString();

        // Then...
        assertThat(string, is("12:00 -> 12:01"));
    }

    @Test
    public void cancelled_toString_showScheduledAndMessage() {
        // Given...
        TrainTime trainTime = TrainTime.of(LocalTime.NOON, Optional.empty(), "Cancelled");

        // When...
        String string = trainTime.toString();

        // Then...
        assertThat(string, is("12:00 (Cancelled)"));
    }

    @Test
    public void unexpectedCombination_toString_showScheduledAndError() {
        // Given...
        TrainTime trainTime = TrainTime.of(LocalTime.NOON, Optional.empty(), "");

        // When...
        String string = trainTime.toString();

        // Then...
        assertThat(string, is("12:00 [ERROR]"));
    }
}
