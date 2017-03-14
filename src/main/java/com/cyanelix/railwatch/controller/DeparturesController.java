package com.cyanelix.railwatch.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cyanelix.railwatch.client.service.TrainTimesService;
import com.cyanelix.railwatch.domain.Station;
import com.cyanelix.railwatch.domain.TrainTime;

@RestController
public class DeparturesController {
    @Autowired
    private TrainTimesService trainTimesService;

    @RequestMapping("/departures")
    public List<TrainTime> get(@RequestParam("from") String fromStation, @RequestParam("to") String toStation) {
        return trainTimesService.lookupTrainTimes(Station.of(fromStation), Station.of(toStation));
    }
}
