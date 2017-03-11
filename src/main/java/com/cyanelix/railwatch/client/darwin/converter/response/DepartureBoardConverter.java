package com.cyanelix.railwatch.client.darwin.converter.response;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.core.convert.converter.Converter;

import com.cyanelix.railwatch.domain.TrainTime;
import com.thalesgroup.rtti._2016_02_16.ldb.StationBoardResponseType;
import com.thalesgroup.rtti._2016_02_16.ldb.types.ServiceItem;

public class DepartureBoardConverter implements Converter<StationBoardResponseType, List<TrainTime>> {
	private static final String ON_TIME_ETD = "On time";

	@Override
	public List<TrainTime> convert(StationBoardResponseType response) {
		return response.getGetStationBoardResult().getTrainServices().getService().stream()
				.map(serviceItem -> convertServiceItemToTrainTime(serviceItem)).collect(Collectors.toList());
	}

	private TrainTime convertServiceItemToTrainTime(ServiceItem serviceItem) {
		LocalTime scheduledDepatureTime = LocalTime.parse(serviceItem.getStd());

		String etd = serviceItem.getEtd();

		String message = "";
		LocalTime expectedDepatureTime = null;

		if (ON_TIME_ETD.equals(etd)) {
			expectedDepatureTime = scheduledDepatureTime;
		} else {
			try {
				expectedDepatureTime = LocalTime.parse(etd);
			} catch (DateTimeParseException ex) {
				message = etd;
			}
		}

		return TrainTime.of(scheduledDepatureTime, Optional.ofNullable(expectedDepatureTime), message);
	}
}
