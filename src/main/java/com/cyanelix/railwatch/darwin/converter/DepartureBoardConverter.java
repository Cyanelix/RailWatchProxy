package com.cyanelix.railwatch.darwin.converter;

import com.cyanelix.railwatch.domain.Formation;
import com.cyanelix.railwatch.domain.TrainTime;
import com.thalesgroup.rtti._2016_02_16.ldb.StationBoardResponseType;
import com.thalesgroup.rtti._2016_02_16.ldb.types.ServiceItem;
import org.springframework.core.convert.converter.Converter;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class DepartureBoardConverter implements Converter<StationBoardResponseType, List<TrainTime>> {
    private static final String ON_TIME_ETD = "On time";

    @Override
    public List<TrainTime> convert(StationBoardResponseType response) {
        if (trainTimesReturned(response)) {
            return response.getGetStationBoardResult().getTrainServices().getService().stream()
                    .map(this::convertServiceItemToTrainTime)
                    .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }

    private boolean trainTimesReturned(StationBoardResponseType response) {
        return response != null
                && response.getGetStationBoardResult() != null
                && response.getGetStationBoardResult().getTrainServices() != null
                && response.getGetStationBoardResult().getTrainServices().getService() != null
                && !response.getGetStationBoardResult().getTrainServices().getService().isEmpty();
    }

    private TrainTime convertServiceItemToTrainTime(ServiceItem serviceItem) {
        LocalTime scheduledDepartureTime = LocalTime.parse(serviceItem.getStd());

        String etd = serviceItem.getEtd();

        String message = "";
        LocalTime expectedDepartureTime = null;

        if (ON_TIME_ETD.equals(etd)) {
            expectedDepartureTime = scheduledDepartureTime;
        } else {
            try {
                expectedDepartureTime = LocalTime.parse(etd);
            } catch (DateTimeParseException ex) {
                message = etd;
            }
        }

        return new TrainTime.Builder(scheduledDepartureTime)
                .withExpectedDepartureTime(expectedDepartureTime)
                .withMessage(message)
                .withFormation(parseFormation(serviceItem))
                .build();
    }

    private Formation parseFormation(ServiceItem serviceItem) {
        Formation formation;
        if (serviceItem.isIsReverseFormation() == null) {
            formation = Formation.UNSPECIFIED;
        } else if (serviceItem.isIsReverseFormation()) {
            formation = Formation.REVERSE;
        } else {
            formation = Formation.NORMAL;
        }
        return formation;
    }
}
