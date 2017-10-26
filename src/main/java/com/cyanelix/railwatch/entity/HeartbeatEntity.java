package com.cyanelix.railwatch.entity;

import com.cyanelix.railwatch.domain.NotificationTarget;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

public final class HeartbeatEntity {
    @Id
    private String id;

    private NotificationTarget notificationTarget;
    private LocalDateTime dateTime;

    public HeartbeatEntity(NotificationTarget notificationTarget, LocalDateTime dateTime) {
        this.notificationTarget = notificationTarget;
        this.dateTime = dateTime;
    }

    public NotificationTarget getNotificationTarget() {
        return notificationTarget;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }
}
