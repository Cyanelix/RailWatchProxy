package com.cyanelix.railwatch.dto;

import com.cyanelix.railwatch.domain.TrainTime;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class TrainTimeDTO {
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    private String scheduledDepartureTime;
    private String expectedDepartureTime;
    private String message;

    public TrainTimeDTO(TrainTime trainTime) {
        this.scheduledDepartureTime = trainTime.getScheduledDepartureTime().format(TIME_FORMATTER);
        this.expectedDepartureTime = trainTime.getExpectedDepartureTime()
                .map(time -> time.format(TIME_FORMATTER))
                .orElse(null);
        this.message = trainTime.getMessage();
    }

    public TrainTimeDTO() {
        // Default constructor required for Jackson.
    }

    public String getScheduledDepartureTime() {
        return scheduledDepartureTime;
    }

    public void setScheduledDepartureTime(String scheduledDepartureTime) {
        this.scheduledDepartureTime = scheduledDepartureTime;
    }

    public TrainTime toTrainTime() {
        LocalTime expected = null;
        if (expectedDepartureTime != null) {
            expected = LocalTime.parse(expectedDepartureTime, TIME_FORMATTER);
        }

        return TrainTime.of(LocalTime.parse(scheduledDepartureTime, TIME_FORMATTER), Optional.ofNullable(expected), message);
    }

    public String getExpectedDepartureTime() {
        return expectedDepartureTime;
    }

    public void setExpectedDepartureTime(String expectedDepartureTime) {
        this.expectedDepartureTime = expectedDepartureTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
