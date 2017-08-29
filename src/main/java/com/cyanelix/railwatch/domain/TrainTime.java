package com.cyanelix.railwatch.domain;

import org.apache.commons.lang3.StringUtils;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TrainTime {
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    private final LocalTime scheduledDepartureTime;
    private final LocalTime expectedDepartureTime;
    private final String message;
    private final Formation formation;
    private final int platformNumber;

    private TrainTime(Builder builder) {
        this.scheduledDepartureTime = builder.scheduledDepartureTime;
        this.expectedDepartureTime = builder.expectedDepartureTime;
        this.message = builder.message;
        this.formation = builder.formation;
        this.platformNumber = builder.platformNumber;
    }

    public LocalTime getScheduledDepartureTime() {
        return scheduledDepartureTime;
    }

    public LocalTime getExpectedDepartureTime() {
        return expectedDepartureTime;
    }

    public String getMessage() {
        return message;
    }

    public Formation getFormation() {
        return formation;
    }

    public boolean isOnTime() {
        return expectedDepartureTime != null && scheduledDepartureTime.equals(expectedDepartureTime);
    }

    public String toString() {
        return String.format("%s%s", getTimeRepresentation(), getFormationRepresentation());
    }

    private String getTimeRepresentation() {
        String scheduledTime = scheduledDepartureTime.format(TIME_FORMATTER);

        if (isOnTime()) {
            return scheduledTime;
        }

        if (expectedDepartureTime != null) {
            return scheduledTime + " -> " + expectedDepartureTime.format(TIME_FORMATTER);
        }

        if (StringUtils.isNotBlank(message)) {
            return scheduledTime + " (" + message + ")";
        }

        return scheduledTime + " [ERROR]";
    }

    private String getFormationRepresentation() {
        String formationSignifier = formation.getSignifier();

        if (formationSignifier.isEmpty()) {
            return formationSignifier;
        }

        return " " + formationSignifier;
    }

    public static class Builder {
        private final LocalTime scheduledDepartureTime;
        private LocalTime expectedDepartureTime;
        private String message;
        private Formation formation;
        private int platformNumber;

        public Builder(LocalTime scheduledDepartureTime) {
            this.scheduledDepartureTime = scheduledDepartureTime;
        }

        public Builder withExpectedDepartureTime(LocalTime expectedDepartureTime) {
            this.expectedDepartureTime = expectedDepartureTime;
            return this;
        }

        public Builder withMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder withFormation(Formation formation) {
            this.formation = formation;
            return this;
        }

        public Builder withPlatformNumber(int platformNumber) {
            this.platformNumber = platformNumber;
            return this;
        }

        public TrainTime build() {
            if (formation == null) {
                formation = Formation.UNSPECIFIED;
            }
            if (message == null) {
                message = "";
            }
            if (expectedDepartureTime == null && StringUtils.isBlank(message)) {
                throw new IllegalStateException("Either an expected departure time or a message is required.");
            }
            return new TrainTime(this);
        }
    }
}
