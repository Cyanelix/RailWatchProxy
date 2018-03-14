package com.cyanelix.railwatch.controller;

import com.cyanelix.railwatch.controller.exception.ResourceNotFoundException;
import com.cyanelix.railwatch.domain.NotificationTarget;
import com.cyanelix.railwatch.domain.UserId;
import com.cyanelix.railwatch.dto.FullUserDetailsDTO;
import com.cyanelix.railwatch.dto.ScheduleDTO;
import com.cyanelix.railwatch.dto.UserDTO;
import com.cyanelix.railwatch.entity.Schedule;
import com.cyanelix.railwatch.entity.User;
import com.cyanelix.railwatch.service.ScheduleService;
import com.cyanelix.railwatch.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("users")
public class UsersController {
    private final UserService userService;
    private final ScheduleService scheduleService;
    private final ConversionService conversionService;

    @Autowired
    public UsersController(UserService userService, ScheduleService scheduleService, ConversionService conversionService) {
        this.userService = userService;
        this.scheduleService = scheduleService;
        this.conversionService = conversionService;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{userId}")
    public FullUserDetailsDTO getUser(@PathVariable("userId") String userId) {
        User user = userService.getUser(UserId.of(userId));
        if (user == null) {
            throw new ResourceNotFoundException();
        }

        UserDTO userDTO = conversionService.convert(user, UserDTO.class);

        List<Schedule> schedules = scheduleService.getSchedulesForUser(user);

        List<ScheduleDTO> scheduleDTOS = schedules.stream()
                .map(schedule -> conversionService.convert(schedule, ScheduleDTO.class))
                .collect(Collectors.toList());

        return new FullUserDetailsDTO(userDTO, scheduleDTOS);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> create(@RequestBody @Valid UserDTO userDTO, UriComponentsBuilder uriComponentsBuilder) {
        User user = userService.createUser(NotificationTarget.of(userDTO.getNotificationTarget()));

        UriComponents uriComponents = uriComponentsBuilder.path("/users/{id}").buildAndExpand(user.getUserId());

        return ResponseEntity.created(uriComponents.toUri()).build();
    }
}
