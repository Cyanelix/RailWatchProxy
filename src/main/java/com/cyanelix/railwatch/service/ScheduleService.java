package com.cyanelix.railwatch.service;

import com.cyanelix.railwatch.domain.*;
import com.cyanelix.railwatch.entity.Schedule;
import com.cyanelix.railwatch.entity.User;
import com.cyanelix.railwatch.repository.ScheduleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ScheduleService {
    private static final Logger LOG = LoggerFactory.getLogger(ScheduleService.class);

    private final TrainTimesService trainTimesService;

    private final NotificationService notificationService;

    private final ScheduleRepository scheduleRepository;

    private final Clock clock;

    @Autowired
    public ScheduleService(TrainTimesService trainTimesService, NotificationService notificationService, ScheduleRepository scheduleRepository, Clock clock) {
        this.trainTimesService = trainTimesService;
        this.notificationService = notificationService;
        this.scheduleRepository = scheduleRepository;
        this.clock = clock;
    }

    public void createSchedule(Schedule schedule) {
        scheduleRepository.save(schedule);
    }

    @Scheduled(fixedDelay = 30000)
    public void checkTimes() {
        LOG.debug("Checking times.");
        getActiveSchedules()
                .forEach(this::lookupAndNotifyTrainTimes);
    }

    public Set<Schedule> getSchedules() {
        return scheduleRepository.findAll().parallelStream()
                .collect(Collectors.toSet());
    }

    public List<Schedule> getSchedulesForUser(User user) {
        return scheduleRepository.findByUser(user);
    }

    private void lookupAndNotifyTrainTimes(Schedule schedule) {
        List<TrainTime> trainTimes = trainTimesService.lookupTrainTimes(schedule.getFromStation(), schedule.getToStation());
        notificationService.sendNotification(schedule, trainTimes);
    }

    private Stream<Schedule> getActiveSchedules() {
        return scheduleRepository.findByStateIs(ScheduleState.ENABLED).parallelStream()
                .filter(schedule -> schedule.isActive(LocalDateTime.now(clock)));
    }
}
