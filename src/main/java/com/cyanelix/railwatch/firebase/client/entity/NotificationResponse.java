package com.cyanelix.railwatch.firebase.client.entity;

public class NotificationResponse {
    private int success;

    public NotificationResponse() {
        // Default constructor required for Jackson.
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }
}
