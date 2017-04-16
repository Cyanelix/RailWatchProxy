package com.cyanelix.railwatch.domain;

import com.cyanelix.railwatch.service.NotificationService;
import com.cyanelix.railwatch.service.TrainTimesService;

import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

public final class Schedule {
    private final LocalTime startTime;
    private final LocalTime endTime;
    private final Station fromStation;
    private final Station toStation;
    private final NotificationTarget notificationTarget;

    private Schedule(LocalTime startTime, LocalTime endTime, Station fromStation, Station toStation, NotificationTarget notificationTarget) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.fromStation = fromStation;
        this.toStation = toStation;
        this.notificationTarget = notificationTarget;
    }

    public static Schedule of(LocalTime startTime, LocalTime endTime, Station fromStation, Station toStation, NotificationTarget notificationTarget) {
        return new Schedule(startTime, endTime, fromStation, toStation, notificationTarget);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Schedule schedule = (Schedule) o;
        return Objects.equals(startTime, schedule.startTime) &&
                Objects.equals(endTime, schedule.endTime) &&
                Objects.equals(fromStation, schedule.fromStation) &&
                Objects.equals(toStation, schedule.toStation) &&
                Objects.equals(notificationTarget, schedule.notificationTarget);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startTime, endTime, fromStation, toStation, notificationTarget);
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public Station getToStation() {
        return toStation;
    }

    public Station getFromStation() {
        return fromStation;
    }

    public boolean isActive(LocalTime testTime) {
        return startTime.isBefore(testTime) && endTime.isAfter(testTime);
    }

    public NotificationTarget getNotificationTarget() {
        return notificationTarget;
    }

    public void lookupAndNotifyTrainTimes(TrainTimesService trainTimesService, NotificationService notificationService) {
        List<TrainTime> trainTimes = trainTimesService.lookupTrainTimes(fromStation, toStation);
        notificationService.sendNotification(this, trainTimes);
    }
}
