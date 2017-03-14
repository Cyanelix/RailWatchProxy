package com.cyanelix.railwatch.client.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cyanelix.railwatch.client.darwin.DarwinActionType;
import com.cyanelix.railwatch.client.darwin.DarwinClient;
import com.cyanelix.railwatch.client.darwin.DeparturesBoardRequest;
import com.cyanelix.railwatch.domain.Station;
import com.cyanelix.railwatch.domain.TrainTime;

@Service
public class TrainTimesService {
    @Autowired
    private DarwinClient darwinClient;

    public List<TrainTime> lookupTrainTimes(Station fromStation, Station toStation) {
        DeparturesBoardRequest departuresBoardRequest = new DeparturesBoardRequest(fromStation, toStation);
        return darwinClient.sendAndReceive(departuresBoardRequest, DarwinActionType.GET_DEPARTURE_BOARD);
    }
}
