package com.cyanelix.railwatch.controller;

import com.cyanelix.railwatch.domain.Station;
import com.cyanelix.railwatch.domain.TrainTime;
import com.cyanelix.railwatch.dto.TrainTimeDTO;
import com.cyanelix.railwatch.service.TrainTimesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @RequestMapping(method = RequestMethod.GET)
    public List<TrainTimeDTO> get(@RequestParam("from") String fromStation, @RequestParam("to") String toStation) {
        List<TrainTime> trainTimes = trainTimesService.lookupTrainTimes(Station.of(fromStation), Station.of(toStation));
        return trainTimes.parallelStream()
                .map(TrainTimeDTO::new)
                .collect(Collectors.toList());
    }
}
