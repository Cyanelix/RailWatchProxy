package com.cyanelix.railwatch.domain;

public final class User {
    private final UserId userId;
    private final NotificationTarget notificationTarget;
    private final ScheduleState scheduleState;

    public User(UserId userId, NotificationTarget notificationTarget, ScheduleState scheduleState) {
        this.userId = userId;
        this.notificationTarget = notificationTarget;
        this.scheduleState = scheduleState;
    }

    public UserId getUserId() {
        return userId;
    }

    public NotificationTarget getNotificationTarget() {
        return notificationTarget;
    }

    public ScheduleState getScheduleState() {
        return scheduleState;
    }
}
