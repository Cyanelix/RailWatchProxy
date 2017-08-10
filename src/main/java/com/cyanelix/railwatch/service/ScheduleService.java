package com.cyanelix.railwatch.service;

import com.cyanelix.railwatch.domain.Schedule;
import com.cyanelix.railwatch.entity.ScheduleEntity;
import com.cyanelix.railwatch.repository.ScheduleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;
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
        scheduleRepository.save(ScheduleEntity.of(schedule));
    }

    @Scheduled(fixedDelay = 30000)
    public void checkTimes() {
        LOG.debug("Checking times.");
        getActiveSchedules()
                .forEach(schedule -> schedule.lookupAndNotifyTrainTimes(trainTimesService, notificationService));
    }

    private Stream<Schedule> getActiveSchedules() {
        return scheduleRepository.findAll().parallelStream()
                .map(Schedule::of)
                .filter(schedule -> schedule.isActive(LocalDateTime.now(clock)));
    }

    public Set<Schedule> getSchedules() {
        return scheduleRepository.findAll().parallelStream()
                .map(Schedule::of)
                .collect(Collectors.toSet());
    }
}
