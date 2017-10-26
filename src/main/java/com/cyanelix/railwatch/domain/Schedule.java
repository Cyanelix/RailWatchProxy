package com.cyanelix.railwatch.domain;

import com.cyanelix.railwatch.entity.ScheduleEntity;
import com.cyanelix.railwatch.service.NotificationService;
import com.cyanelix.railwatch.service.TrainTimesService;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Stream;

public final class Schedule {
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    private final LocalTime startTime;
    private final LocalTime endTime;
    private final DayRange dayRange;
    private final Journey journey;
    private final NotificationTarget notificationTarget;
    private final ScheduleState state;

    private Schedule(LocalTime startTime, LocalTime endTime, DayRange dayRange, Journey journey, NotificationTarget notificationTarget, ScheduleState state) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.dayRange = dayRange;
        this.journey = journey;
        this.notificationTarget = notificationTarget;
        this.state = state;
    }

    public static Schedule of(LocalTime startTime, LocalTime endTime, DayRange dayRange, Journey journey, NotificationTarget notificationTarget, ScheduleState state) {
        return new Schedule(startTime, endTime, dayRange, journey, notificationTarget, state);
    }

    public static Schedule of(ScheduleEntity scheduleEntity) {
        return new Schedule(
                scheduleEntity.getStartTime(),
                scheduleEntity.getEndTime(),
                scheduleEntity.getDayRange(),
                Journey.of(
                        Station.of(scheduleEntity.getFromStation()),
                        Station.of(scheduleEntity.getToStation())
                ),
                NotificationTarget.of(scheduleEntity.getNotificationTarget()),
                scheduleEntity.getState());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Schedule schedule = (Schedule) o;
        return Objects.equals(startTime, schedule.startTime) &&
                Objects.equals(endTime, schedule.endTime) &&
                Objects.equals(dayRange, schedule.dayRange) &&
                Objects.equals(journey, schedule.journey) &&
                Objects.equals(notificationTarget, schedule.notificationTarget) &&
                state == schedule.state;
    }

    @Override
    public int hashCode() {
        return Objects.hash(startTime, endTime, dayRange, journey, notificationTarget, state);
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public DayRange getDayRange() {
        return dayRange;
    }

    public Journey getJourney() {
        return journey;
    }

    public Stream<String> getDayNames() {
        return dayRange.getDays().stream()
                .map(day -> day.getDisplayName(TextStyle.FULL, Locale.UK));
    }

    public boolean isActive(LocalDateTime testDateTime) {
        DayOfWeek dayOfWeek = testDateTime.getDayOfWeek();
        LocalTime testTime = testDateTime.toLocalTime();

        return dayRange.contains(dayOfWeek) && timeWithinRange(testTime);
    }

    private boolean timeWithinRange(LocalTime testTime) {
        return startTime.isBefore(testTime) && endTime.isAfter(testTime);
    }

    public NotificationTarget getNotificationTarget() {
        return notificationTarget;
    }

    public void lookupAndNotifyTrainTimes(TrainTimesService trainTimesService, NotificationService notificationService) {
        List<TrainTime> trainTimes = trainTimesService.lookupTrainTimes(journey.getFrom(), journey.getTo());
        notificationService.sendNotification(this, trainTimes);
    }

    public ScheduleState getState() {
        return state;
    }

    @Override
    public String toString() {
        return journey.toString() + "; "
                + dayRange.toString() + " @ "
                + startTime.format(TIME_FORMATTER) + " -> " + endTime.format(TIME_FORMATTER);
    }
}
