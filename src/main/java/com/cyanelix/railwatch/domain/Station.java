package com.cyanelix.railwatch.domain;

public final class Station {
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((stationCode == null) ? 0 : stationCode.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Station other = (Station) obj;
        if (stationCode == null) {
            if (other.stationCode != null)
                return false;
        } else if (!stationCode.equals(other.stationCode))
            return false;
        return true;
    }
}
