package com.cyanelix.railwatch.client.darwin.converter.response;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.core.convert.converter.Converter;

import com.cyanelix.railwatch.domain.TrainTime;
import com.thalesgroup.rtti._2016_02_16.ldb.StationBoardResponseType;
import com.thalesgroup.rtti._2016_02_16.ldb.types.ServiceItem;

public class DepartureBoardConverter implements Converter<StationBoardResponseType, List<TrainTime>> {
	@Override
	public List<TrainTime> convert(StationBoardResponseType response) {
		return response.getGetStationBoardResult().getTrainServices().getService().stream()
				.map(serviceItem -> convertServiceItemToTrainTime(serviceItem)).collect(Collectors.toList());
	}

	private TrainTime convertServiceItemToTrainTime(ServiceItem serviceItem) {
		String etd = serviceItem.getEtd();
		try {
			return TrainTime.of(LocalTime.parse(serviceItem.getStd()), LocalTime.parse(etd));
		} catch (DateTimeParseException ex) {
			return TrainTime.of(LocalTime.parse(serviceItem.getStd()), etd);
		}
	}
}
