package com.cyanelix.railwatch.domain;

public class Station {
	private final String stationCode;
	
	public Station(String stationCode) {
		this.stationCode = stationCode;
	}

	public String getStationCode() {
		return stationCode;
	}
}
