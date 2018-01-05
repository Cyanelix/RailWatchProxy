package com.cyanelix.railwatch.domain;

public final class User {
    private final UserId userId;
    private final NotificationTarget notificationTarget;
    private final UserState userState;

    public User(UserId userId, NotificationTarget notificationTarget, UserState userState) {
        this.userId = userId;
        this.notificationTarget = notificationTarget;
        this.userState = userState;
    }

    public UserId getUserId() {
        return userId;
    }

    public NotificationTarget getNotificationTarget() {
        return notificationTarget;
    }

    public UserState getUserState() {
        return userState;
    }
}
