package com.cyanelix.railwatch.controller;

import com.cyanelix.railwatch.domain.Station;
import com.cyanelix.railwatch.domain.TrainTime;
import com.cyanelix.railwatch.dto.ScheduleDTO;
import com.cyanelix.railwatch.dto.TrainTimeDTO;
import com.cyanelix.railwatch.service.ScheduleService;
import com.cyanelix.railwatch.service.TrainTimesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("departures")
public class DeparturesController {
    private final TrainTimesService trainTimesService;

    private final ScheduleService scheduleService;

    @Autowired
    public DeparturesController(TrainTimesService trainTimesService, ScheduleService scheduleService) {
        this.trainTimesService = trainTimesService;
        this.scheduleService = scheduleService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<TrainTimeDTO> get(@RequestParam("from") String fromStation, @RequestParam("to") String toStation) {
        List<TrainTime> trainTimes = trainTimesService.lookupTrainTimes(Station.of(fromStation), Station.of(toStation));
        return trainTimes.parallelStream()
                .map(TrainTimeDTO::new)
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "schedule", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.CREATED)
    public void put(@RequestBody ScheduleDTO scheduleDTO) {
        scheduleService.createSchedule(scheduleDTO.toSchedule());
        scheduleService.checkTimes();
    }
}
