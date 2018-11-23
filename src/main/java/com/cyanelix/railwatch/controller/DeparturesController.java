package com.cyanelix.railwatch.controller;

import com.cyanelix.railwatch.domain.Station;
import com.cyanelix.railwatch.domain.TrainTime;
import com.cyanelix.railwatch.dto.TrainTimeResponse;
import com.cyanelix.railwatch.service.TrainTimesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("departures")
public class DeparturesController {
    private final TrainTimesService trainTimesService;

    @Autowired
    public DeparturesController(TrainTimesService trainTimesService) {
        this.trainTimesService = trainTimesService;
    }

    @GetMapping
    public List<TrainTimeResponse> get(@RequestParam("from") String fromStation, @RequestParam("to") String toStation) {
        List<TrainTime> trainTimes = trainTimesService.lookupTrainTimes(Station.of(fromStation), Station.of(toStation));
        return trainTimes.parallelStream()
                .map(TrainTimeResponse::new)
                .collect(Collectors.toList());
    }
}
