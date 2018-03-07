package com.cyanelix.railwatch.dto;

import com.cyanelix.railwatch.domain.*;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class ScheduleDTO {
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    private String startTime;
    private String endTime;
    private String[] days;
    private String fromStation;
    private String toStation;
    private String notificationTarget;
    private String state;

    public ScheduleDTO() {
        // Default constructor required for Jackson.
    }

    public ScheduleDTO(Schedule schedule) {
        startTime = schedule.getStartTime().format(TIME_FORMATTER);
        endTime = schedule.getEndTime().format(TIME_FORMATTER);
        days = convertToDayNames(schedule.getDayRange());
        fromStation = schedule.getJourney().getFrom().getStationCode();
        toStation = schedule.getJourney().getTo().getStationCode();
        notificationTarget = schedule.getNotificationTarget().getTargetAddress();

        ScheduleState state = schedule.getState();
        if (state == null) {
            this.state = ScheduleState.ENABLED.name();
        } else {
            this.state = state.name();
        }
    }

    private String[] convertToDayNames(DayRange dayRange) {
        List<String> names = dayRange.getDays().stream()
                .map(dayOfWeek -> dayOfWeek.getDisplayName(TextStyle.FULL, Locale.UK))
                .collect(Collectors.toList());

        return names.toArray(new String[dayRange.getDays().size()]);
    }

    public Schedule toSchedule() {
        return Schedule.of(
                LocalTime.parse(startTime, TIME_FORMATTER),
                LocalTime.parse(endTime, TIME_FORMATTER),
                DayRange.of(days),
                Journey.of(Station.of(fromStation), Station.of(toStation)),
                NotificationTarget.of(notificationTarget),
                ScheduleState.parse(state));
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
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

    public String[] getDays() {
        return days;
    }

    public void setDays(String[] days) {
        this.days = days;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
