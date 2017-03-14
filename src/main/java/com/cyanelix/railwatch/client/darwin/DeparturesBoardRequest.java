package com.cyanelix.railwatch.client.darwin;

import javax.xml.bind.JAXBElement;

import com.cyanelix.railwatch.domain.Station;
import com.thalesgroup.rtti._2007_10_10.ldb.commontypes.FilterType;
import com.thalesgroup.rtti._2016_02_16.ldb.GetBoardRequestParams;

public class DeparturesBoardRequest extends DarwinRequest<JAXBElement<GetBoardRequestParams>> {
    private final Station fromStation;
    private final Station toStation;

    public DeparturesBoardRequest(Station fromStation, Station toStation) {
        this.fromStation = fromStation;
        this.toStation = toStation;
    }

    @Override
    public JAXBElement<GetBoardRequestParams> getSoapRequest() {
        GetBoardRequestParams getBoardRequestParams = new GetBoardRequestParams();
        getBoardRequestParams.setCrs(fromStation.getStationCode());
        getBoardRequestParams.setFilterCrs(toStation.getStationCode());
        getBoardRequestParams.setFilterType(FilterType.TO);

        return objectFactory.createGetDepartureBoardRequest(getBoardRequestParams);
    }
}
