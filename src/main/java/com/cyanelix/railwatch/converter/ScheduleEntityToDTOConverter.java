package com.cyanelix.railwatch.converter;

import com.cyanelix.railwatch.domain.DayRange;
import com.cyanelix.railwatch.dto.ScheduleDTO;
import com.cyanelix.railwatch.entity.ScheduleEntity;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Component
public class ScheduleEntityToDTOConverter implements Converter<ScheduleEntity, ScheduleDTO> {
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    @Override
    public ScheduleDTO convert(ScheduleEntity scheduleEntity) {
        ScheduleDTO scheduleDTO = new ScheduleDTO();

        scheduleDTO.setStartTime(scheduleEntity.getStartTime().format(TIME_FORMATTER));
        scheduleDTO.setEndTime(scheduleEntity.getEndTime().format(TIME_FORMATTER));
        scheduleDTO.setDays(convertToDayNames(scheduleEntity.getDayRange()));
        scheduleDTO.setFromStation(scheduleEntity.getFromStation().getStationCode());
        scheduleDTO.setToStation(scheduleEntity.getToStation().getStationCode());
        scheduleDTO.setState(scheduleEntity.getState().name());
        scheduleDTO.setUserId(scheduleEntity.getUser().getUserId());

        return scheduleDTO;
    }

    private String[] convertToDayNames(DayRange dayRange) {
        List<String> names = dayRange.getDays().stream()
                .map(dayOfWeek -> dayOfWeek.getDisplayName(TextStyle.FULL, Locale.UK))
                .collect(Collectors.toList());

        return names.toArray(new String[dayRange.getDays().size()]);
    }
}
