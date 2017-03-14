package com.cyanelix.railwatch.domain;

public class Station {
    private final String stationCode;

    private Station(String stationCode) {
        this.stationCode = stationCode;
    }

    public static Station of(String stationCode) {
        return new Station(stationCode);
    }

    public String getStationCode() {
        return stationCode;
    }
}
