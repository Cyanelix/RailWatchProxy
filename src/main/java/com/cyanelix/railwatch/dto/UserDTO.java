package com.cyanelix.railwatch.dto;

import org.hibernate.validator.constraints.NotBlank;

public class UserDTO {
    private String userId;

    @NotBlank
    private String notificationTarget;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNotificationTarget() {
        return notificationTarget;
    }

    public void setNotificationTarget(String notificationTarget) {
        this.notificationTarget = notificationTarget;
    }
}
