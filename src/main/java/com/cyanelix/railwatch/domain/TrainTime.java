package com.cyanelix.railwatch.domain;

import java.time.LocalTime;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonFormat;

public class TrainTime {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private final LocalTime scheduledDepartureTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private final Optional<LocalTime> expectedDepartureTime;

    private final String message;

    private TrainTime(LocalTime scheduledDepartureTime, Optional<LocalTime> expectedDepartureTime, String message) {
        this.scheduledDepartureTime = scheduledDepartureTime;
        this.expectedDepartureTime = expectedDepartureTime;
        this.message = message;
    }

    public static TrainTime of(LocalTime scheduledDepartureTime, Optional<LocalTime> expectedDepartureTime,
            String message) {
        return new TrainTime(scheduledDepartureTime, expectedDepartureTime, message);
    }

    public LocalTime getScheduledDepartureTime() {
        return scheduledDepartureTime;
    }

    public Optional<LocalTime> getExpectedDepartureTime() {
        return expectedDepartureTime;
    }

    public String getMessage() {
        return message;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(scheduledDepartureTime.toString());

        if (expectedDepartureTime.isPresent()) {
            stringBuilder.append(" -> " + expectedDepartureTime);
        } else {
            stringBuilder.append(message);
        }

        return stringBuilder.toString();
    }
}
