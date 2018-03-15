package com.cyanelix.railwatch.converter;

import com.cyanelix.railwatch.domain.DayRange;
import com.cyanelix.railwatch.dto.ScheduleDTO;
import com.cyanelix.railwatch.entity.Schedule;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Component
public class ScheduleToDTOConverter implements Converter<Schedule, ScheduleDTO> {
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    @Override
    public ScheduleDTO convert(Schedule schedule) {
        ScheduleDTO scheduleDTO = new ScheduleDTO();

        scheduleDTO.setStartTime(schedule.getStartTime().format(TIME_FORMATTER));
        scheduleDTO.setEndTime(schedule.getEndTime().format(TIME_FORMATTER));
        scheduleDTO.setDays(convertToDayNames(schedule.getDayRange()));
        scheduleDTO.setFromStation(schedule.getFromStation().getStationCode());
        scheduleDTO.setToStation(schedule.getToStation().getStationCode());
        scheduleDTO.setState(schedule.getState().name());
        scheduleDTO.setUserId(schedule.getUser().getUserId().get());

        return scheduleDTO;
    }

    private String[] convertToDayNames(DayRange dayRange) {
        List<String> names = dayRange.getDays().stream()
                .map(dayOfWeek -> dayOfWeek.getDisplayName(TextStyle.FULL, Locale.UK))
                .collect(Collectors.toList());

        return names.toArray(new String[dayRange.getDays().size()]);
    }
}
