package com.cyanelix.railwatch.domain;

import org.apache.commons.lang3.StringUtils;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class TrainTime {
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

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

    public boolean isOnTime() {
        return expectedDepartureTime.isPresent() && scheduledDepartureTime.equals(expectedDepartureTime.get());
    }

    public String toString() {
        String scheduledTime = scheduledDepartureTime.format(TIME_FORMATTER);

        if (isOnTime()) {
            return scheduledTime;
        }

        if (expectedDepartureTime.isPresent()) {
            return scheduledTime + " -> " + expectedDepartureTime.get().format(TIME_FORMATTER);
        }

        if (StringUtils.isNotBlank(message)) {
            return scheduledTime + " (" + message + ")";
        }

        return scheduledTime + " [ERROR]";
    }
}
