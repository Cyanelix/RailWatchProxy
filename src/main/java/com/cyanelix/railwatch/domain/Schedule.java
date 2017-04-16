package com.cyanelix.railwatch.domain;

import com.cyanelix.railwatch.service.NotificationService;
import com.cyanelix.railwatch.service.TrainTimesService;

import java.time.LocalTime;
import java.util.List;

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

    public void lookupAndNotifyTrainTimes(TrainTimesService trainTimesService, NotificationService notificationService) {
        List<TrainTime> trainTimes = trainTimesService.lookupTrainTimes(fromStation, toStation);
        notificationService.sendNotification(this, trainTimes);
    }
}
