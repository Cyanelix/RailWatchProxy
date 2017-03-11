package com.cyanelix.railwatch.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;

import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.cyanelix.railwatch.client.service.TrainTimesService;
import com.cyanelix.railwatch.domain.Station;
import com.cyanelix.railwatch.domain.TrainTime;

@RunWith(MockitoJUnitRunner.class)
public class DeparturesControllerTest {
	@Mock
	private TrainTimesService mockTrainTimesService;

	@InjectMocks
	private DeparturesController departuresController;

	@Test
	public void givenTwoStationsWithETD_whenRequested_getTimes() {
		// Given...
		String fromStation = "FOO";
		String toStation = "BAR";

		ArgumentCaptor<Station> fromCaptor = ArgumentCaptor.forClass(Station.class);
		ArgumentCaptor<Station> toCaptor = ArgumentCaptor.forClass(Station.class);

		given(mockTrainTimesService.lookupTrainTimes(fromCaptor.capture(), toCaptor.capture()))
				.willReturn(Collections.singletonList(TrainTime.of(LocalTime.MIDNIGHT, Optional.of(LocalTime.NOON), "")));

		// When...
		List<TrainTime> trainTimes = departuresController.get(fromStation, toStation);

		// Then...
		assertThat(fromCaptor.getValue().getStationCode(), is(fromStation));
		assertThat(toCaptor.getValue().getStationCode(), is(toStation));
		assertThat(trainTimes.size(), is(1));
		assertThat(trainTimes.get(0).getScheduledDepartureTime(), is(LocalTime.MIDNIGHT));
		assertThat(trainTimes.get(0).getExpectedDepartureTime().get(), is(LocalTime.NOON));
	}
}
