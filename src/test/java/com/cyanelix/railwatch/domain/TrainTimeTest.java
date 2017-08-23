package com.cyanelix.railwatch.domain;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.time.LocalTime;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class TrainTimeTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void onTime_toString_justShowScheduledTime() {
        // Given...
        TrainTime trainTime = new TrainTime.Builder(LocalTime.NOON).withExpectedDepartureTime(LocalTime.NOON).build();

        // When...
        String string = trainTime.toString();

        // Then...
        assertThat(string, is("12:00"));
    }

    @Test
    public void delayed_toString_showScheduledAndExpectedTimes() {
        // Given...
        TrainTime trainTime = new TrainTime.Builder(LocalTime.NOON)
                .withExpectedDepartureTime(LocalTime.of(12, 1))
                .build();

        // When...
        String string = trainTime.toString();

        // Then...
        assertThat(string, is("12:00 -> 12:01"));
    }

    @Test
    public void cancelled_toString_showScheduledAndMessage() {
        // Given...
        TrainTime trainTime = new TrainTime.Builder(LocalTime.NOON)
                .withMessage("Cancelled")
                .build();

        // When...
        String string = trainTime.toString();

        // Then...
        assertThat(string, is("12:00 (Cancelled)"));
    }

    @Test
    public void reverseFormation_toString_includeR() {
        // Given...
        TrainTime trainTime = new TrainTime.Builder(LocalTime.NOON)
                .withExpectedDepartureTime(LocalTime.NOON)
                .withFormation(Formation.REVERSE)
                .build();

        // When...
        String string = trainTime.toString();

        // Then...
        assertThat(string, is("12:00 R"));
    }

    @Test
    public void noMessageOrExpectedTime_build_throwException() {
        // Given...
        expectedException.expect(IllegalStateException.class);

        TrainTime.Builder builder = new TrainTime.Builder(LocalTime.NOON);

        // When...
        builder.build();

        // Then...
        // (exception expected)
    }

    @Test
    public void nullMessage_build_emptyString() {
        // Given...
        TrainTime.Builder builder = new TrainTime.Builder(LocalTime.NOON).withExpectedDepartureTime(LocalTime.NOON);

        // When...
        TrainTime trainTime = builder.build();

        // Then...
        assertThat(trainTime.getMessage(), is(""));
    }
}
