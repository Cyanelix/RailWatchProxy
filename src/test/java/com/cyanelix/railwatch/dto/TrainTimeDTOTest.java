package com.cyanelix.railwatch.dto;

import com.cyanelix.railwatch.domain.TrainTime;
import org.junit.Test;

import java.time.LocalTime;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class TrainTimeDTOTest {
    @Test
    public void trainTimeWithExpectedTime_dtoConstructor() {
        // Given...
        TrainTime trainTime = new TrainTime.Builder(LocalTime.NOON)
                .withExpectedDepartureTime(LocalTime.MIDNIGHT)
                .withMessage("foo")
                .build();

        // When...
        TrainTimeDTO trainTimeDTO = new TrainTimeDTO(trainTime);

        // Then...
        assertThat(trainTimeDTO.getScheduledDepartureTime(), is("12:00"));
        assertThat(trainTimeDTO.getExpectedDepartureTime(), is("00:00"));
        assertThat(trainTimeDTO.getMessage(), is("foo"));
    }

    @Test
    public void trainTimeWithNoExpectedTime_dtoConstructor() {
        // Given...
        TrainTime trainTime = new TrainTime.Builder(LocalTime.NOON)
                .withMessage("foo")
                .build();

        // When...
        TrainTimeDTO trainTimeDTO = new TrainTimeDTO(trainTime);

        // Then...
        assertThat(trainTimeDTO.getScheduledDepartureTime(), is("12:00"));
        assertThat(trainTimeDTO.getExpectedDepartureTime(), is(nullValue()));
        assertThat(trainTimeDTO.getMessage(), is("foo"));
    }

    @Test
    public void dtoWithExpectedTime_toTrainTime() {
        // Given...
        TrainTimeDTO trainTimeDTO = new TrainTimeDTO();
        trainTimeDTO.setScheduledDepartureTime("12:00");
        trainTimeDTO.setExpectedDepartureTime("00:00");
        trainTimeDTO.setMessage("foo");

        // When...
        TrainTime trainTime = trainTimeDTO.toTrainTime();

        // Then...
        assertThat(trainTime.getScheduledDepartureTime(), is(LocalTime.NOON));
        assertThat(trainTime.getExpectedDepartureTime(), is(LocalTime.MIDNIGHT));
        assertThat(trainTime.getMessage(), is("foo"));
    }

    @Test
    public void dtoWithNoExpectedTime_toTrainTime() {
        // Given...
        TrainTimeDTO trainTimeDTO = new TrainTimeDTO();
        trainTimeDTO.setScheduledDepartureTime("12:00");
        trainTimeDTO.setMessage("foo");

        // When...
        TrainTime trainTime = trainTimeDTO.toTrainTime();

        // Then...
        assertThat(trainTime.getScheduledDepartureTime(), is(LocalTime.NOON));
        assertThat(trainTime.getExpectedDepartureTime(), is(nullValue()));
        assertThat(trainTime.getMessage(), is("foo"));
    }
}
