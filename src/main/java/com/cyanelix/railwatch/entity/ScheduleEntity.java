package com.cyanelix.railwatch.entity;

import com.cyanelix.railwatch.domain.Schedule;
import org.springframework.data.annotation.Id;

import java.time.LocalTime;

public class ScheduleEntity {
    @Id
    private String id;

    private LocalTime startTime;
    private LocalTime endTime;
    private String fromStation;
    private String toStation;
    private String notificationTarget;

    public ScheduleEntity(LocalTime startTime, LocalTime endTime, String fromStation, String toStation, String notificationTarget) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.fromStation = fromStation;
        this.toStation = toStation;
        this.notificationTarget = notificationTarget;
    }

    public static ScheduleEntity of(Schedule schedule) {
        return new ScheduleEntity(
                schedule.getStartTime(),
                schedule.getEndTime(),
                schedule.getFromStation().getStationCode(),
                schedule.getToStation().getStationCode(),
                schedule.getNotificationTarget().getTargetAddress());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public String getFromStation() {
        return fromStation;
    }

    public void setFromStation(String fromStation) {
        this.fromStation = fromStation;
    }

    public String getToStation() {
        return toStation;
    }

    public void setToStation(String toStation) {
        this.toStation = toStation;
    }

    public String getNotificationTarget() {
        return notificationTarget;
    }

    public void setNotificationTarget(String notificationTarget) {
        this.notificationTarget = notificationTarget;
    }
}
