package com.cyanelix.railwatch.dto;

import java.util.Collections;
import java.util.List;

public final class FullUserDetailsDTO {
    private final String userId;
    private final String notificationTarget;
    private final List<ScheduleDTO> schedules;

    public FullUserDetailsDTO(UserDTO userDTO, List<ScheduleDTO> schedules) {
        this.userId = userDTO.getUserId();
        this.notificationTarget = userDTO.getNotificationTarget();
        this.schedules = Collections.unmodifiableList(schedules);
    }

    public String getUserId() {
        return userId;
    }

    public String getNotificationTarget() {
        return notificationTarget;
    }

    public List<ScheduleDTO> getSchedules() {
        return schedules;
    }
}
