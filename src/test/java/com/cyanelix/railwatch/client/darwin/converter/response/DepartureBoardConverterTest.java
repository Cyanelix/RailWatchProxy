package com.cyanelix.railwatch.client.darwin.converter.response;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Stream;

import org.junit.Test;

import com.cyanelix.railwatch.domain.TrainTime;
import com.thalesgroup.rtti._2016_02_16.ldb.StationBoardResponseType;
import com.thalesgroup.rtti._2016_02_16.ldb.types.ArrayOfServiceItems;
import com.thalesgroup.rtti._2016_02_16.ldb.types.ServiceItem;
import com.thalesgroup.rtti._2016_02_16.ldb.types.StationBoard;

public class DepartureBoardConverterTest {
	@Test
	public void singleTrain_OnTime_convertsSuccessfully() {
		// Given...
		ServiceItem serviceItem = createServiceItemForTimes("15:00", "On time");
		StationBoardResponseType response = createStationBoardResponseType(serviceItem);

		DepartureBoardConverter converter = new DepartureBoardConverter();

		// When...
		List<TrainTime> trainTimes = converter.convert(response);

		// Then...
		assertThat(trainTimes, hasSize(1));
		assertThat(trainTimes.get(0).getScheduledDepartureTime(), is(LocalTime.of(15, 0)));
		assertThat(trainTimes.get(0).getExpectedDepartureTime(), is(LocalTime.of(15, 0)));
		assertThat(trainTimes.get(0).getMessage(), is("On time"));
	}

	@Test
	public void singleTrain_delayed_convertsSuccessfully() {
		// Given...
		ServiceItem serviceItem = createServiceItemForTimes("15:00", "15:25");
		StationBoardResponseType response = createStationBoardResponseType(serviceItem);

		DepartureBoardConverter converter = new DepartureBoardConverter();

		// When...
		List<TrainTime> trainTimes = converter.convert(response);

		// Then...
		assertThat(trainTimes, hasSize(1));
		assertThat(trainTimes.get(0).getScheduledDepartureTime(), is(LocalTime.of(15, 0)));
		assertThat(trainTimes.get(0).getExpectedDepartureTime(), is(LocalTime.of(15, 25)));
		assertThat(trainTimes.get(0).getMessage(), is(""));
	}

	@Test
	public void twoTrains_oneDelayedOneOnTime_convertsSuccessfully() {
		// Given...
		ServiceItem delayedServiceItem = createServiceItemForTimes("15:00", "15:25");
		ServiceItem onTimeServiceItem = createServiceItemForTimes("15:00", "On time");
		StationBoardResponseType response = createStationBoardResponseType(delayedServiceItem, onTimeServiceItem);

		DepartureBoardConverter converter = new DepartureBoardConverter();

		// When...
		List<TrainTime> trainTimes = converter.convert(response);

		// Then...
		assertThat(trainTimes, hasSize(2));
		assertThat(trainTimes.get(0).getScheduledDepartureTime(), is(LocalTime.of(15, 0)));
		assertThat(trainTimes.get(0).getExpectedDepartureTime(), is(LocalTime.of(15, 25)));
		assertThat(trainTimes.get(1).getScheduledDepartureTime(), is(LocalTime.of(15, 0)));
		assertThat(trainTimes.get(1).getExpectedDepartureTime(), is(LocalTime.of(15, 0)));
	}

	@Test
	public void singleTrain_cancelled_convertsSuccessfully() {
		// Given...
		ServiceItem serviceItem = createServiceItemForTimes("15:00", "Cancelled");
		StationBoardResponseType response = createStationBoardResponseType(serviceItem);

		DepartureBoardConverter converter = new DepartureBoardConverter();

		// When...
		List<TrainTime> trainTimes = converter.convert(response);

		// Then...
		assertThat(trainTimes, hasSize(1));
		assertThat(trainTimes.get(0).getScheduledDepartureTime(), is(LocalTime.of(15, 0)));
		assertThat(trainTimes.get(0).getExpectedDepartureTime(), is(LocalTime.of(15, 0)));
		assertThat(trainTimes.get(0).getMessage(), is("Cancelled"));
	}

	private ServiceItem createServiceItemForTimes(String std, String etd) {
		ServiceItem service = new ServiceItem();
		service.setStd(std);
		service.setEtd(etd);
		return service;
	}

	private StationBoardResponseType createStationBoardResponseType(ServiceItem... serviceItems) {
		ArrayOfServiceItems trainServices = new ArrayOfServiceItems();
		Stream.of(serviceItems).forEach(serviceItem -> trainServices.getService().add(serviceItem));

		StationBoard stationBoard = new StationBoard();
		stationBoard.setTrainServices(trainServices);

		StationBoardResponseType response = new StationBoardResponseType();
		response.setGetStationBoardResult(stationBoard);

		return response;
	}
}
