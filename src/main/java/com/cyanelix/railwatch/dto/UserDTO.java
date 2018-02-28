package com.cyanelix.railwatch.dto;

import com.cyanelix.railwatch.domain.User;
import org.hibernate.validator.constraints.NotBlank;

public class UserDTO {
    private String userId;

    @NotBlank
    private String notificationTarget;

    public UserDTO() {
        // Default constructor required for Jackson.
    }

    public UserDTO(User user) {
        this.userId = user.getUserId().get();
        this.notificationTarget = user.getNotificationTarget().getTargetAddress();
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
