package com.cyanelix.railwatch.entity;

import com.cyanelix.railwatch.domain.DayRange;
import com.cyanelix.railwatch.domain.Schedule;
import org.springframework.data.annotation.Id;

import java.time.LocalTime;

public class ScheduleEntity {
    @Id
    private String id;

    private LocalTime startTime;
    private LocalTime endTime;
    private DayRange dayRange;
    private String fromStation;
    private String toStation;
    private String notificationTarget;

    public ScheduleEntity(LocalTime startTime, LocalTime endTime, DayRange dayRange, String fromStation, String toStation, String notificationTarget) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.dayRange = dayRange;
        this.fromStation = fromStation;
        this.toStation = toStation;
        this.notificationTarget = notificationTarget;
    }

    public static ScheduleEntity of(Schedule schedule) {
        return new ScheduleEntity(
                schedule.getStartTime(),
                schedule.getEndTime(),
                schedule.getDayRange(),
                schedule.getJourney().getFrom().getStationCode(),
                schedule.getJourney().getTo().getStationCode(),
                schedule.getNotificationTarget().getTargetAddress());
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public String getFromStation() {
        return fromStation;
    }

    public String getToStation() {
        return toStation;
    }

    public String getNotificationTarget() {
        return notificationTarget;
    }

    public DayRange getDayRange() {
        return dayRange;
    }
}
