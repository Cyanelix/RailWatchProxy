package com.cyanelix.railwatch.dto;

import java.util.Collections;
import java.util.List;

public final class FullUserDetailsResponse {
    private final String userId;
    private final String notificationTarget;
    private final List<ScheduleRequestResponse> schedules;

    public FullUserDetailsResponse(UserRequestResponse userRequestResponse, List<ScheduleRequestResponse> schedules) {
        this.userId = userRequestResponse.getUserId();
        this.notificationTarget = userRequestResponse.getNotificationTarget();
        this.schedules = Collections.unmodifiableList(schedules);
    }

    public String getUserId() {
        return userId;
    }

    public String getNotificationTarget() {
        return notificationTarget;
    }

    public List<ScheduleRequestResponse> getSchedules() {
        return schedules;
    }
}
