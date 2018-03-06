package com.cyanelix.railwatch.service;

import com.cyanelix.railwatch.domain.*;
import com.cyanelix.railwatch.entity.ScheduleEntity;
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

    public void createSchedule(ScheduleEntity schedule) {
        scheduleRepository.save(schedule);
    }

    @Scheduled(fixedDelay = 30000)
    public void checkTimes() {
        LOG.debug("Checking times.");
        getActiveSchedules()
                .forEach(this::lookupAndNotifyTrainTimes);
    }

    private void lookupAndNotifyTrainTimes(ScheduleEntity schedule) {
        List<TrainTime> trainTimes = trainTimesService.lookupTrainTimes(schedule.getFromStation(), schedule.getToStation());
        notificationService.sendNotification(schedule, trainTimes);
    }

    private Stream<ScheduleEntity> getActiveSchedules() {
        return getEnabledSchedules().filter(schedule -> schedule.isActive(LocalDateTime.now(clock)));
    }

    public Set<ScheduleEntity> getSchedules() {
        return scheduleRepository.findAll().parallelStream()
                .collect(Collectors.toSet());
    }

    Stream<ScheduleEntity> getEnabledSchedules() {
        return scheduleRepository.findByStateIs(ScheduleState.ENABLED).parallelStream();
    }
}
