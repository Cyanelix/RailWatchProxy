package com.cyanelix.railwatch.dto;

import com.cyanelix.railwatch.domain.Formation;
import com.cyanelix.railwatch.domain.TrainTime;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class TrainTimeDTO {
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    private String scheduledDepartureTime;
    private String expectedDepartureTime;
    private String message;

    public TrainTimeDTO(TrainTime trainTime) {
        this.scheduledDepartureTime = Objects.requireNonNull(trainTime.getScheduledDepartureTime()).format(TIME_FORMATTER);
        this.message = trainTime.getMessage();

        if (trainTime.getExpectedDepartureTime() != null) {
            this.expectedDepartureTime = trainTime.getExpectedDepartureTime().format(TIME_FORMATTER);
        }
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

        return new TrainTime.Builder(LocalTime.parse(scheduledDepartureTime, TIME_FORMATTER))
                .withExpectedDepartureTime(expected)
                .withMessage(message)
                .withFormation(Formation.NORMAL)
                .build();
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
