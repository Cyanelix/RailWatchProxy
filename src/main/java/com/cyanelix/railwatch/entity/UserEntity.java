package com.cyanelix.railwatch.entity;

import com.cyanelix.railwatch.domain.User;
import com.cyanelix.railwatch.domain.UserState;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@Document
public final class UserEntity {
    @Id
    private String id;

    @Indexed(unique = true)
    private String userId;

    @Indexed(unique = true)
    private String notificationTarget;

    private UserState userState;

    public UserEntity(String userId, String notificationTarget, UserState userState) {
        this.userId = userId;
        this.notificationTarget = notificationTarget;
        this.userState = userState;
    }

    public static UserEntity of(User user) {
        return new UserEntity(
                user.getUserId().get(),
                user.getNotificationTarget().getTargetAddress(),
                user.getUserState());
    }

    public String getUserId() {
        return userId;
    }

    public String getNotificationTarget() {
        return notificationTarget;
    }

    public UserState getUserState() {
        return userState;
    }

    public void setUserState(UserState userState) {
        this.userState = userState;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity that = (UserEntity) o;
        return Objects.equals(userId, that.userId) &&
                Objects.equals(notificationTarget, that.notificationTarget) &&
                userState == that.userState;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, notificationTarget, userState);
    }
}
