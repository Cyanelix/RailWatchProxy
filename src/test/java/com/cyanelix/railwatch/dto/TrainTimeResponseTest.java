package com.cyanelix.railwatch.dto;

import com.cyanelix.railwatch.domain.TrainTime;
import org.junit.Test;

import java.time.LocalTime;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class TrainTimeResponseTest {
    @Test
    public void trainTimeWithExpectedTime_constructor() {
        // Given...
        TrainTime trainTime = new TrainTime.Builder(LocalTime.NOON)
                .withExpectedDepartureTime(LocalTime.MIDNIGHT)
                .withMessage("foo")
                .build();

        // When...
        TrainTimeResponse trainTimeResponse = new TrainTimeResponse(trainTime);

        // Then...
        assertThat(trainTimeResponse.getScheduledDepartureTime(), is("12:00"));
        assertThat(trainTimeResponse.getExpectedDepartureTime(), is("00:00"));
        assertThat(trainTimeResponse.getMessage(), is("foo"));
    }

    @Test
    public void trainTimeWithNoExpectedTime_constructor() {
        // Given...
        TrainTime trainTime = new TrainTime.Builder(LocalTime.NOON)
                .withMessage("foo")
                .build();

        // When...
        TrainTimeResponse trainTimeResponse = new TrainTimeResponse(trainTime);

        // Then...
        assertThat(trainTimeResponse.getScheduledDepartureTime(), is("12:00"));
        assertThat(trainTimeResponse.getExpectedDepartureTime(), is(nullValue()));
        assertThat(trainTimeResponse.getMessage(), is("foo"));
    }
}
