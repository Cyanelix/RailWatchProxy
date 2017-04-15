package com.cyanelix.railwatch.domain;

import java.time.LocalTime;

public class Schedule {
    private LocalTime startTime;
    private LocalTime endTime;
    private Station fromStation;
    private Station toStation;
    private NotificationTarget notificationTarget;

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

    public Station getToStation() {
        return toStation;
    }

    public void setToStation(Station toStation) {
        this.toStation = toStation;
    }

    public Station getFromStation() {
        return fromStation;
    }

    public void setFromStation(Station fromStation) {
        this.fromStation = fromStation;
    }

    public boolean isActive(LocalTime testTime) {
        return startTime.isBefore(testTime) && endTime.isAfter(testTime);
    }

    public NotificationTarget getNotificationTarget() {
        return notificationTarget;
    }

    public void setNotificationTarget(NotificationTarget notificationTarget) {
        this.notificationTarget = notificationTarget;
    }
}
