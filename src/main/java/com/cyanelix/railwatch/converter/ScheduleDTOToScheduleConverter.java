package com.cyanelix.railwatch.converter;

import com.cyanelix.railwatch.domain.DayRange;
import com.cyanelix.railwatch.domain.ScheduleState;
import com.cyanelix.railwatch.domain.Station;
import com.cyanelix.railwatch.dto.ScheduleRequestResponse;
import com.cyanelix.railwatch.entity.Schedule;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Component
public class ScheduleDTOToScheduleConverter implements Converter<ScheduleRequestResponse, Schedule> {
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    @Override
    public Schedule convert(ScheduleRequestResponse scheduleRequestResponse) {
        return new Schedule(
                LocalTime.parse(scheduleRequestResponse.getStartTime(), TIME_FORMATTER),
                LocalTime.parse(scheduleRequestResponse.getEndTime(), TIME_FORMATTER),
                DayRange.of(scheduleRequestResponse.getDays()),
                Station.of(scheduleRequestResponse.getFromStation()),
                Station.of(scheduleRequestResponse.getToStation()),
                ScheduleState.parse(scheduleRequestResponse.getState()),
                null);
    }
}
