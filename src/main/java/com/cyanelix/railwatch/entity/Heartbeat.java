package com.cyanelix.railwatch.entity;

import com.cyanelix.railwatch.domain.NotificationTarget;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document
public final class Heartbeat {
    @Id
    private String id;

    private NotificationTarget notificationTarget;
    private LocalDateTime dateTime;

    public Heartbeat(NotificationTarget notificationTarget, LocalDateTime dateTime) {
        this.notificationTarget = notificationTarget;
        this.dateTime = dateTime;
    }

    public String getId() {
        return id;
    }

    public NotificationTarget getNotificationTarget() {
        return notificationTarget;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }
}
