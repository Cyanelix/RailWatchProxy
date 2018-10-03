package com.cyanelix.railwatch.service;

import com.cyanelix.railwatch.darwin.client.DarwinActionType;
import com.cyanelix.railwatch.darwin.client.DarwinClient;
import com.cyanelix.railwatch.darwin.client.DeparturesBoardRequest;
import com.cyanelix.railwatch.domain.Station;
import com.cyanelix.railwatch.domain.TrainTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainTimesService {
    private final DarwinClient darwinClient;

    @Autowired
    public TrainTimesService(DarwinClient darwinClient) {
        this.darwinClient = darwinClient;
    }

    public List<TrainTime> lookupTrainTimes(Station fromStation, Station toStation) {
        DeparturesBoardRequest departuresBoardRequest = new DeparturesBoardRequest(fromStation, toStation);
        return darwinClient.sendAndReceive(departuresBoardRequest, DarwinActionType.GET_DEPARTURE_BOARD);
    }
}
