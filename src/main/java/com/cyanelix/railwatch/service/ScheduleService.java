package com.cyanelix.railwatch.service;

import com.cyanelix.railwatch.domain.Schedule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

@Service
public class ScheduleService {
    private final TrainTimesService trainTimesService;

    private final NotificationService notificationService;

    private final Clock clock;

    protected static final Set<Schedule> createdSchedules = new HashSet<>();

    @Autowired
    public ScheduleService(TrainTimesService trainTimesService, NotificationService notificationService, Clock clock) {
        this.trainTimesService = trainTimesService;
        this.notificationService = notificationService;
        this.clock = clock;
    }

    public void createSchedule(Schedule schedule) {
        createdSchedules.add(schedule);
    }

    public void checkTimes() {
        getActiveSchedules()
                .forEach(schedule -> schedule.lookupAndNotifyTrainTimes(trainTimesService, notificationService));
    }

    private Stream<Schedule> getActiveSchedules() {
        return createdSchedules.parallelStream()
                .filter(schedule -> schedule.isActive(LocalTime.now(clock)));
    }
}
