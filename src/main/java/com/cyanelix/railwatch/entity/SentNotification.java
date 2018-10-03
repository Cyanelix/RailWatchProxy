package com.cyanelix.railwatch.entity;

import com.cyanelix.railwatch.firebase.client.entity.NotificationRequest;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document
public class SentNotification {
    @Id
    private String id;

    private String to;
    private String title;
    private String body;
    private String priority;
    private LocalDateTime sentDateTime;

    public SentNotification(String to, String title, String body, String priority, LocalDateTime sentDateTime) {
        this.to = to;
        this.title = title;
        this.body = body;
        this.priority = priority;
        this.sentDateTime = sentDateTime;
    }

    public static SentNotification of(NotificationRequest notificationRequest, LocalDateTime sentDateTime) {
        return new SentNotification(
                notificationRequest.getTo(),
                notificationRequest.getNotification().getTitle(),
                notificationRequest.getNotification().getBody(),
                notificationRequest.getPriority(),
                sentDateTime);
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }
}
