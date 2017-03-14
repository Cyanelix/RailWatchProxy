package com.cyanelix.railwatch.client.darwin;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.cyanelix.railwatch.domain.Station;
import com.cyanelix.railwatch.domain.TrainTime;
import com.thalesgroup.rtti._2016_02_16.ldb.StationBoardResponseType;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DarwinClientIT {
    @Autowired
    private DarwinClient darwinClient;

    @Test
    public void getKYNtoBRIDepartureBoard() {
        // Given...
        Station fromStation = Station.of("KYN");
        Station toStation = Station.of("BRI");
        DeparturesBoardRequest request = new DeparturesBoardRequest(fromStation, toStation);

        DarwinActionType<StationBoardResponseType, List<TrainTime>> getDepartureBoard = DarwinActionType.GET_DEPARTURE_BOARD;

        // When...
        List<TrainTime> trainTimes = darwinClient.sendAndReceive(request, getDepartureBoard);

        // Then...
        assertThat(trainTimes, is(not(empty())));
    }
}
