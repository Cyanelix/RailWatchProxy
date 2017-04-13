package com.cyanelix.railwatch.service;

import com.cyanelix.railwatch.domain.Schedule;
import com.cyanelix.railwatch.domain.Station;
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

    private final Clock clock;

    private static final List<Schedule> createdSchedules = new ArrayList<>();

    @Autowired
    public ScheduleService(TrainTimesService trainTimesService, Clock clock) {
        this.trainTimesService = trainTimesService;
        this.clock = clock;
    }

    public void createSchedule(Schedule schedule) {
        createdSchedules.add(schedule);
    }

    public void checkTimes() {
        getActiveSchedules().forEach(this::lookupTrainTimes);
    }

    private Stream<Schedule> getActiveSchedules() {
        return createdSchedules.parallelStream()
                .filter(schedule -> schedule.isActive(LocalTime.now(clock)));
    }

    private void lookupTrainTimes(Schedule schedule) {
        trainTimesService.lookupTrainTimes(Station.of(schedule.getFromStation()), Station.of(schedule.getToStation()));
    }
}
