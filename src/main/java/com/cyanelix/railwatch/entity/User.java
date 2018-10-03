package com.cyanelix.railwatch.entity;

import com.cyanelix.railwatch.domain.UserId;
import com.cyanelix.railwatch.domain.UserState;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@Document
public final class User {
    @Id
    private String id;

    @Indexed(unique = true)
    private UserId userId;

    @Indexed(unique = true)
    private String notificationTarget;

    private UserState userState;

    public User() {
        // Required by spring-data
    }

    public User(UserId userId, String notificationTarget, UserState userState) {
        this.userId = userId;
        this.notificationTarget = notificationTarget;
        this.userState = userState;
    }

    public UserId getUserId() {
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
        User that = (User) o;
        return Objects.equals(userId, that.userId) &&
                Objects.equals(notificationTarget, that.notificationTarget) &&
                userState == that.userState;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, notificationTarget, userState);
    }
}
