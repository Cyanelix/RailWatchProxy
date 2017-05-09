package com.cyanelix.railwatch.domain;

import com.cyanelix.railwatch.entity.ScheduleEntity;
import com.cyanelix.railwatch.service.NotificationService;
import com.cyanelix.railwatch.service.TrainTimesService;

import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

public final class Schedule {
    private final LocalTime startTime;
    private final LocalTime endTime;
    private final Journey journey;
    private final NotificationTarget notificationTarget;

    private Schedule(LocalTime startTime, LocalTime endTime, Journey journey, NotificationTarget notificationTarget) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.journey = journey;
        this.notificationTarget = notificationTarget;
    }

    public static Schedule of(LocalTime startTime, LocalTime endTime, Journey journey, NotificationTarget notificationTarget) {
        return new Schedule(startTime, endTime, journey, notificationTarget);
    }

    public static Schedule of(ScheduleEntity scheduleEntity) {
        return new Schedule(
                scheduleEntity.getStartTime(),
                scheduleEntity.getEndTime(),
                Journey.of(
                        Station.of(scheduleEntity.getFromStation()),
                        Station.of(scheduleEntity.getToStation())
                ),
                NotificationTarget.of(scheduleEntity.getNotificationTarget()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Schedule schedule = (Schedule) o;
        return Objects.equals(startTime, schedule.startTime) &&
                Objects.equals(endTime, schedule.endTime) &&
                Objects.equals(journey, schedule.journey) &&
                Objects.equals(notificationTarget, schedule.notificationTarget);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startTime, endTime, journey, notificationTarget);
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public Journey getJourney() {
        return journey;
    }

    public boolean isActive(LocalTime testTime) {
        return startTime.isBefore(testTime) && endTime.isAfter(testTime);
    }

    public NotificationTarget getNotificationTarget() {
        return notificationTarget;
    }

    public void lookupAndNotifyTrainTimes(TrainTimesService trainTimesService, NotificationService notificationService) {
        List<TrainTime> trainTimes = trainTimesService.lookupTrainTimes(journey.getFrom(), journey.getTo());
        notificationService.sendNotification(this, trainTimes);
    }
}
