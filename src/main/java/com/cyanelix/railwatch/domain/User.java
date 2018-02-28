package com.cyanelix.railwatch.domain;

import com.cyanelix.railwatch.entity.UserEntity;

import java.util.Objects;

public final class User {
    private final UserId userId;
    private final NotificationTarget notificationTarget;
    private final UserState userState;

    public User(UserId userId, NotificationTarget notificationTarget, UserState userState) {
        this.userId = userId;
        this.notificationTarget = notificationTarget;
        this.userState = userState;
    }

    public static User of(UserEntity userEntity) {
        return new User(
                UserId.of(userEntity.getUserId()),
                NotificationTarget.of(userEntity.getNotificationTarget()),
                userEntity.getUserState());
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(userId, user.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }
}
