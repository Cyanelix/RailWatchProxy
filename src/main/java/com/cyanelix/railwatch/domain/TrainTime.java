package com.cyanelix.railwatch.domain;

import java.time.LocalTime;
import java.util.Optional;

public class TrainTime {
    private final LocalTime scheduledDepartureTime;
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
}
