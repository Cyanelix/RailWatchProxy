package com.cyanelix.railwatch.converter;

import com.cyanelix.railwatch.domain.DayRange;
import com.cyanelix.railwatch.dto.ScheduleRequestResponse;
import com.cyanelix.railwatch.entity.Schedule;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Component
public class ScheduleToDTOConverter implements Converter<Schedule, ScheduleRequestResponse> {
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    @Override
    public ScheduleRequestResponse convert(Schedule schedule) {
        ScheduleRequestResponse scheduleRequestResponse = new ScheduleRequestResponse();

        scheduleRequestResponse.setStartTime(schedule.getStartTime().format(TIME_FORMATTER));
        scheduleRequestResponse.setEndTime(schedule.getEndTime().format(TIME_FORMATTER));
        scheduleRequestResponse.setDays(convertToDayNames(schedule.getDayRange()));
        scheduleRequestResponse.setFromStation(schedule.getFromStation().getStationCode());
        scheduleRequestResponse.setToStation(schedule.getToStation().getStationCode());
        scheduleRequestResponse.setState(schedule.getState().name());
        scheduleRequestResponse.setUserId(schedule.getUser().getUserId().get());

        return scheduleRequestResponse;
    }

    private String[] convertToDayNames(DayRange dayRange) {
        List<String> names = dayRange.getDays().stream()
                .map(dayOfWeek -> dayOfWeek.getDisplayName(TextStyle.FULL, Locale.UK))
                .collect(Collectors.toList());

        return names.toArray(new String[dayRange.getDays().size()]);
    }
}
