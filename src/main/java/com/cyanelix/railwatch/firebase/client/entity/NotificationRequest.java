package com.cyanelix.railwatch.firebase.client.entity;

public class NotificationRequest {
    private final String to;
    private final Notification notification;
    private final String priority;

    public NotificationRequest(String to, String title, String body) {
        this.to = to;
        this.notification = new Notification(title, body);
        priority = "high";
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

        private Notification(String title, String body) {
            this.title = title;
            this.body = body;
        }

        public String getTitle() {
            return title;
        }

        public String getBody() {
            return body;
        }
    }
}
