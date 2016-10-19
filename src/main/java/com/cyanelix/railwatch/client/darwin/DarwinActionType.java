package com.cyanelix.railwatch.client.darwin;

import java.util.List;

import org.springframework.core.convert.converter.Converter;

import com.cyanelix.railwatch.client.darwin.converter.response.DepartureBoardConverter;
import com.cyanelix.railwatch.domain.TrainTime;
import com.thalesgroup.rtti._2016_02_16.ldb.StationBoardResponseType;

public class DarwinActionType<S, T> {
	public static final DarwinActionType<StationBoardResponseType, List<TrainTime>> GET_DEPARTURE_BOARD =
			new DarwinActionType<>("http://thalesgroup.com/RTTI/2012-01-13/ldb/GetDepartureBoard", new DepartureBoardConverter());
	
	private final String action;
	private final Converter<S, T> responseConverter;
	
	private DarwinActionType(String action, Converter<S, T> darwinResponseToDomain) {
		this.action = action;
		this.responseConverter = darwinResponseToDomain;
	}

	public String getAction() {
		return action;
	}
	
	public T convertResponse(S source) {
		return responseConverter.convert(source);
	}
}
