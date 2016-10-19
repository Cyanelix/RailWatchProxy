package com.cyanelix.railwatch.client.darwin.converter.response;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.core.convert.converter.Converter;

import com.cyanelix.railwatch.domain.TrainTime;
import com.thalesgroup.rtti._2016_02_16.ldb.StationBoardResponseType;

public class DepartureBoardConverter implements Converter<StationBoardResponseType, List<TrainTime>> {
	@Override
	public List<TrainTime> convert(StationBoardResponseType response) {
		return response.getGetStationBoardResult().getTrainServices().getService().stream()
				.map(TrainTime::new)
				.collect(Collectors.toList());
	}
}
