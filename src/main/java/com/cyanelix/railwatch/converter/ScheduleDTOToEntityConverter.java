package com.cyanelix.railwatch.converter;

import com.cyanelix.railwatch.domain.*;
import com.cyanelix.railwatch.dto.ScheduleDTO;
import com.cyanelix.railwatch.entity.ScheduleEntity;
import com.cyanelix.railwatch.entity.UserEntity;
import com.cyanelix.railwatch.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Component
public class ScheduleDTOToEntityConverter implements Converter<ScheduleDTO, ScheduleEntity> {
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    private final UserService userService;

    @Autowired
    public ScheduleDTOToEntityConverter(UserService userService) {
        this.userService = userService;
    }

    @Override
    public ScheduleEntity convert(ScheduleDTO scheduleDTO) {
        UserEntity user = userService.getUser(UserId.of(scheduleDTO.getUserId()));

        return new ScheduleEntity(
                LocalTime.parse(scheduleDTO.getStartTime(), TIME_FORMATTER),
                LocalTime.parse(scheduleDTO.getEndTime(), TIME_FORMATTER),
                DayRange.of(scheduleDTO.getDays()),
                scheduleDTO.getFromStation(),
                scheduleDTO.getToStation(),
                ScheduleState.parse(scheduleDTO.getState()),
                "replace-notification-target",
                user);
    }
}
