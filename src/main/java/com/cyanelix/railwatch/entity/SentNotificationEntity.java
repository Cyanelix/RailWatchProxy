package com.cyanelix.railwatch.entity;

import com.cyanelix.railwatch.firebase.client.entity.NotificationRequest;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

public class SentNotificationEntity {
    @Id
    private String id;

    private String to;
    private String title;
    private String body;
    private String priority;
    private LocalDateTime sentDateTime;

    public SentNotificationEntity(String to, String title, String body, String priority, LocalDateTime sentDateTime) {
        this.to = to;
        this.title = title;
        this.body = body;
        this.priority = priority;
        this.sentDateTime = sentDateTime;
    }

    public SentNotificationEntity(NotificationRequest notificationRequest, LocalDateTime sentDateTime) {
        this.to = notificationRequest.getTo();
        this.title = notificationRequest.getNotification().getTitle();
        this.body = notificationRequest.getNotification().getBody();
        this.priority = notificationRequest.getPriority();
        this.sentDateTime = sentDateTime;
    }

    public String getTo() {
        return to;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }
}
