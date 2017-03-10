package com.cyanelix.railwatch.domain;

import java.time.LocalTime;

public class TrainTime {
	private final LocalTime scheduledDepartureTime;
	private final LocalTime expectedDepartureTime;
	private final String message;

	private TrainTime(LocalTime scheduledDepartureTime, LocalTime expectedDepartureTime, String message) {
		this.scheduledDepartureTime = scheduledDepartureTime;
		this.expectedDepartureTime = expectedDepartureTime;
		this.message = message;
	}

	public static TrainTime of(LocalTime scheduledDepartureTime, LocalTime expectedDepartureTime) {
		return new TrainTime(scheduledDepartureTime, expectedDepartureTime, "");
	}

	public static TrainTime of(LocalTime scheduledDepartureTime, String message) {
		return new TrainTime(scheduledDepartureTime, scheduledDepartureTime, message);
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
}
