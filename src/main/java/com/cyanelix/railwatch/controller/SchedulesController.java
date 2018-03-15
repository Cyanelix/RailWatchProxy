package com.cyanelix.railwatch.controller;

import com.cyanelix.railwatch.dto.ScheduleDTO;
import com.cyanelix.railwatch.entity.Schedule;
import com.cyanelix.railwatch.service.ScheduleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("schedules")
public class SchedulesController {
    private static final Logger LOG = LoggerFactory.getLogger(SchedulesController.class);

    private final ScheduleService scheduleService;
    private final ConversionService conversionService;

    @Autowired
    public SchedulesController(ScheduleService scheduleService, ConversionService conversionService) {
        this.scheduleService = scheduleService;
        this.conversionService = conversionService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<ScheduleDTO> get() {
        return scheduleService.getSchedules().stream()
                .map(schedule -> conversionService.convert(schedule, ScheduleDTO.class))
                .collect(Collectors.toList());
    }

    @RequestMapping(method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.CREATED)
    public void put(@RequestBody ScheduleDTO scheduleDTO) {
        if (LOG.isDebugEnabled()) {
            LOG.debug(String.format("Creating new schedule: %s -> %s @ %s -> %s", scheduleDTO.getFromStation(), scheduleDTO.getToStation(), scheduleDTO.getStartTime(), scheduleDTO.getEndTime()));
        }

        Schedule schedule = conversionService.convert(scheduleDTO, Schedule.class);
        scheduleService.createSchedule(schedule);
    }
}
