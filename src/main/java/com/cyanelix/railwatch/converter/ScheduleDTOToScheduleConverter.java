package com.cyanelix.railwatch.converter;

import com.cyanelix.railwatch.domain.*;
import com.cyanelix.railwatch.dto.ScheduleDTO;
import com.cyanelix.railwatch.entity.Schedule;
import com.cyanelix.railwatch.entity.User;
import com.cyanelix.railwatch.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Component
public class ScheduleDTOToScheduleConverter implements Converter<ScheduleDTO, Schedule> {
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    private final UserService userService;

    @Autowired
    public ScheduleDTOToScheduleConverter(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Schedule convert(ScheduleDTO scheduleDTO) {
        User user = userService.getUser(UserId.of(scheduleDTO.getUserId()));

        return new Schedule(
                LocalTime.parse(scheduleDTO.getStartTime(), TIME_FORMATTER),
                LocalTime.parse(scheduleDTO.getEndTime(), TIME_FORMATTER),
                DayRange.of(scheduleDTO.getDays()),
                Station.of(scheduleDTO.getFromStation()),
                Station.of(scheduleDTO.getToStation()),
                ScheduleState.parse(scheduleDTO.getState()),
                user);
    }
}
