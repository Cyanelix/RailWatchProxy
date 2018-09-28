package com.cyanelix.railwatch.converter;

import com.cyanelix.railwatch.domain.DayRange;
import com.cyanelix.railwatch.domain.ScheduleState;
import com.cyanelix.railwatch.domain.Station;
import com.cyanelix.railwatch.domain.UserId;
import com.cyanelix.railwatch.dto.ScheduleDTO;
import com.cyanelix.railwatch.entity.Schedule;
import com.cyanelix.railwatch.entity.User;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Component
public class ScheduleDTOToScheduleConverter implements Converter<ScheduleDTO, Schedule> {
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    @Override
    public Schedule convert(ScheduleDTO scheduleDTO) {
        return new Schedule(
                LocalTime.parse(scheduleDTO.getStartTime(), TIME_FORMATTER),
                LocalTime.parse(scheduleDTO.getEndTime(), TIME_FORMATTER),
                DayRange.of(scheduleDTO.getDays()),
                Station.of(scheduleDTO.getFromStation()),
                Station.of(scheduleDTO.getToStation()),
                ScheduleState.parse(scheduleDTO.getState()),
                null);
    }
}
