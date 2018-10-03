package com.cyanelix.railwatch.service;

import com.cyanelix.railwatch.domain.NotificationTarget;
import com.cyanelix.railwatch.domain.UserId;
import com.cyanelix.railwatch.domain.UserState;
import com.cyanelix.railwatch.entity.User;
import com.cyanelix.railwatch.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.stream.Stream;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(NotificationTarget notificationTarget) {
        Objects.requireNonNull(notificationTarget, "A notification target is required for a User");

        User user = new User(UserId.generate(), notificationTarget.getTargetAddress(), UserState.ENABLED);
        userRepository.save(user);

        return user;
    }

    public User getUser(UserId userId) {
        return userRepository.findByUserId(userId);
    }

    public void disableUserByNotificationTarget(NotificationTarget notificationTarget) {
        User user = userRepository.findByNotificationTarget(notificationTarget.getTargetAddress());
        user.setUserState(UserState.DISABLED);
        userRepository.save(user);
    }

    public Stream<User> getEnabledUsers() {
        return userRepository.findByUserStateIs(UserState.ENABLED).parallel();
    }
}
