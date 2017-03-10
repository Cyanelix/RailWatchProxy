package com.cyanelix.railwatch.domain;

import java.time.LocalTime;
import java.util.Optional;

public class TrainTime {
	private final LocalTime scheduledDepartureTime;
	private final LocalTime expectedDepartureTime;

	private TrainTime(LocalTime scheduledDepartureTime, LocalTime expectedDepartureTime) {
		this.scheduledDepartureTime = scheduledDepartureTime;
		this.expectedDepartureTime = expectedDepartureTime;
	}

	public static TrainTime of(LocalTime scheduledDepartureTime, Optional<LocalTime> expectedDepartureTime) {
		return new TrainTime(scheduledDepartureTime, expectedDepartureTime.orElse(scheduledDepartureTime));
	}

	public LocalTime getScheduledDepartureTime() {
		return scheduledDepartureTime;
	}

	public LocalTime getExpectedDepartureTime() {
		return expectedDepartureTime;
	}
}
