package com.cyanelix.railwatch.firebase.client.entity;

import com.cyanelix.railwatch.domain.NotificationTarget;
import com.cyanelix.railwatch.entity.SentNotificationEntity;

import java.util.Objects;

public class NotificationRequest {
    private final String to;
    private final Notification notification;
    private final String priority;
    public NotificationRequest(NotificationTarget to, String title, String body) {
        this.to = to.getTargetAddress();
        this.notification = new Notification(title, body);
        priority = "high";
    }

    public static NotificationRequest of(SentNotificationEntity sentNotificationEntity) {
        return new NotificationRequest(
                NotificationTarget.of(sentNotificationEntity.getTo()),
                sentNotificationEntity.getTitle(),
                sentNotificationEntity.getBody());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotificationRequest request = (NotificationRequest) o;
        return Objects.equals(to, request.to) &&
                Objects.equals(notification, request.notification) &&
                Objects.equals(priority, request.priority);
    }

    @Override
    public int hashCode() {
        return Objects.hash(to, notification, priority);
    }

    public String getTo() {
        return to;
    }

    public String getPriority() {
        return priority;
    }

    public Notification getNotification() {
        return notification;
    }

    public class Notification {
        private final String title;
        private final String body;
        private final String sound;

        private Notification(String title, String body) {
            this.title = title;
            this.body = body;
            this.sound = "default";
        }

        public String getTitle() {
            return title;
        }

        public String getBody() {
            return body;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Notification that = (Notification) o;
            return Objects.equals(title, that.title) &&
                    Objects.equals(body, that.body);
        }

        @Override
        public int hashCode() {
            return Objects.hash(title, body);
        }
    }
}
