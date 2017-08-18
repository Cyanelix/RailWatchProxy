package com.cyanelix.railwatch.darwin.client;

import com.cyanelix.railwatch.domain.Station;
import com.thalesgroup.rtti._2007_10_10.ldb.commontypes.FilterType;
import com.thalesgroup.rtti._2016_02_16.ldb.GetBoardRequestParams;
import org.junit.Test;

import javax.xml.bind.JAXBElement;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class DeparturesBoardRequestTest {
    @Test
    public void createRequest_correctlyPopulated() {
        // Given...
        Station fromStation = Station.of("FOO");
        Station toStation = Station.of("BAR");

        DeparturesBoardRequest departuresBoardRequest = new DeparturesBoardRequest(fromStation, toStation);

        // When...
        JAXBElement<GetBoardRequestParams> soapRequest = departuresBoardRequest.getSoapRequest();

        // Then...
        GetBoardRequestParams getBoardRequestParams = soapRequest.getValue();
        assertThat(getBoardRequestParams.getCrs(), is(fromStation.getStationCode()));
        assertThat(getBoardRequestParams.getFilterCrs(), is(toStation.getStationCode()));
        assertThat(getBoardRequestParams.getFilterType(), is(FilterType.TO));
    }
}
