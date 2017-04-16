package com.cyanelix.railwatch.service;

import com.cyanelix.railwatch.domain.Schedule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

@Service
public class ScheduleService {
    private static final Logger LOG = LoggerFactory.getLogger(ScheduleService.class);

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

    @Scheduled(fixedDelay = 30000)
    public void checkTimes() {
        LOG.debug("Checking times.");
        getActiveSchedules()
                .forEach(schedule -> schedule.lookupAndNotifyTrainTimes(trainTimesService, notificationService));
    }

    private Stream<Schedule> getActiveSchedules() {
        return createdSchedules.parallelStream()
                .filter(schedule -> schedule.isActive(LocalTime.now(clock)));
    }
}
