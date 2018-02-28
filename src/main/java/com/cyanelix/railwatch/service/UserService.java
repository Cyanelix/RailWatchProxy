package com.cyanelix.railwatch.service;

import com.cyanelix.railwatch.domain.NotificationTarget;
import com.cyanelix.railwatch.domain.User;
import com.cyanelix.railwatch.domain.UserId;
import com.cyanelix.railwatch.domain.UserState;
import com.cyanelix.railwatch.entity.UserEntity;
import com.cyanelix.railwatch.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(NotificationTarget notificationTarget) {
        Objects.requireNonNull(notificationTarget, "A notification target is required for a User");

        User user = new User(UserId.generate(), notificationTarget, UserState.ENABLED);
        userRepository.save(UserEntity.of(user));

        return user;
    }

    public UserEntity getUser(UserId userId) {
        return userRepository.findByUserId(userId.get());
    }
}
