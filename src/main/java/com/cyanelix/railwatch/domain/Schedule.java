package com.cyanelix.railwatch.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalTime;

public class Schedule {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime startTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime endTime;

    private String fromStation;
    private String toStation;

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public void setFromStation(String fromStation) {
        this.fromStation = fromStation;
    }

    public void setToStation(String toStation) {
        this.toStation = toStation;
    }

    public String getToStation() {
        return toStation;
    }

    public String getFromStation() {
        return fromStation;
    }

    public boolean isActive(LocalTime testTime) {
        return startTime.isBefore(testTime) && endTime.isAfter(testTime);
    }
}
