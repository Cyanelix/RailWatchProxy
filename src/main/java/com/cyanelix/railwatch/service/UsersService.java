package com.cyanelix.railwatch.service;

import com.cyanelix.railwatch.domain.NotificationTarget;
import com.cyanelix.railwatch.domain.ScheduleState;
import com.cyanelix.railwatch.domain.User;
import com.cyanelix.railwatch.domain.UserId;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UsersService {
    public User createUser(NotificationTarget notificationTarget) {
        Objects.requireNonNull(notificationTarget, "A notification target is required for a User");
        return new User(UserId.generate(), notificationTarget, ScheduleState.ENABLED);
    }
}
