package com.cyanelix.railwatch.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.time.LocalTime;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@JsonTest
public class TrainTimeJsonTest {

    @Autowired
    private JacksonTester<TrainTime> json;

    @Test
    public void scheduledSetAM_noExpected_noMessage() throws IOException {
        // Given...
        TrainTime trainTime = TrainTime.of(LocalTime.of(10, 0), Optional.empty(), "");

        // When...
        JsonContent<TrainTime> out = json.write(trainTime);

        // Then...
        assertThat(out).isEqualToJson("scheduledAM_noExpected_noMessage.json");
    }

    @Test
    public void scheduledSetPM_noExpected_noMessage() throws IOException {
        // Given...
        TrainTime trainTime = TrainTime.of(LocalTime.of(20, 0), Optional.empty(), "");

        // When...
        JsonContent<TrainTime> out = json.write(trainTime);

        // Then...
        assertThat(out).isEqualToJson("scheduledPM_noExpected_noMessage.json");
    }

    @Test
    public void scheduledSet_expectedSet_messageSet() throws IOException {
        // Given...
        TrainTime trainTime = TrainTime.of(LocalTime.of(20, 0), Optional.of(LocalTime.of(20, 59)), "Delayed");

        // When...
        JsonContent<TrainTime> out = json.write(trainTime);

        // Then...
        assertThat(out).isEqualToJson("scheduledSet_expectedSet_messageSet.json");
    }
}
