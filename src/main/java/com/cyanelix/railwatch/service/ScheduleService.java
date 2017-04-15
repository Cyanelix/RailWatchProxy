package com.cyanelix.railwatch.service;

import com.cyanelix.railwatch.domain.Schedule;
import com.cyanelix.railwatch.domain.Station;
import com.cyanelix.railwatch.domain.TrainTime;
import com.cyanelix.railwatch.firebase.client.FirebaseClient;
import com.cyanelix.railwatch.firebase.client.entity.NotificationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Service
public class ScheduleService {
    private final TrainTimesService trainTimesService;

    private final NotificationService notificationService;

    private final Clock clock;

    private static final List<Schedule> createdSchedules = new ArrayList<>();

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
        getActiveSchedules().forEach(this::lookupAndNotifyTrainTimes);
    }

    private Stream<Schedule> getActiveSchedules() {
        return createdSchedules.parallelStream()
                .filter(schedule -> schedule.isActive(LocalTime.now(clock)));
    }

    private void lookupAndNotifyTrainTimes(Schedule schedule) {
        List<TrainTime> trainTimes = trainTimesService.lookupTrainTimes(Station.of(schedule.getFromStation()), Station.of(schedule.getToStation()));
        notificationService.sendNotification(schedule, trainTimes);
    }
}
