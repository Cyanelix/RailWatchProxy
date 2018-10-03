package com.cyanelix.railwatch.dto;

import javax.validation.constraints.NotBlank;

public class UserDTO {
    private String userId;

    @NotBlank
    private String notificationTarget;

    public UserDTO() {
        // Default constructor required for Jackson.
    }

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
