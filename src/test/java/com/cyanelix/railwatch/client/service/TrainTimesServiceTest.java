package com.cyanelix.railwatch.client.service;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

import java.util.Collections;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.cyanelix.railwatch.client.darwin.DarwinClient;
import com.cyanelix.railwatch.client.darwin.DeparturesBoardRequest;
import com.cyanelix.railwatch.domain.Station;
import com.thalesgroup.rtti._2016_02_16.ldb.GetBoardRequestParams;

@RunWith(MockitoJUnitRunner.class)
public class TrainTimesServiceTest {
    @Mock
    private DarwinClient mockDarwinClient;

    @InjectMocks
    private TrainTimesService trainTimesService;

    @Test
    public void testLookupTrainTimes() {
        // Given...
        Station fromStation = Station.of("FOO");
        Station toStation = Station.of("BAR");

        ArgumentCaptor<DeparturesBoardRequest> departuresRequestCaptor = ArgumentCaptor
                .forClass(DeparturesBoardRequest.class);

        given(mockDarwinClient.sendAndReceive(departuresRequestCaptor.capture(), any()))
                .willReturn(Collections.emptyList());

        // When...
        trainTimesService.lookupTrainTimes(fromStation, toStation);

        // Then...
        DeparturesBoardRequest departuresBoardRequest = departuresRequestCaptor.getValue();
        GetBoardRequestParams requestParams = departuresBoardRequest.getSoapRequest().getValue();
        assertThat(requestParams.getCrs(), is(fromStation.getStationCode()));
        assertThat(requestParams.getFilterCrs(), is(toStation.getStationCode()));
    }
}
