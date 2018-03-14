package com.cyanelix.railwatch.service;

import com.cyanelix.railwatch.domain.NotificationTarget;
import com.cyanelix.railwatch.domain.UserId;
import com.cyanelix.railwatch.domain.UserState;
import com.cyanelix.railwatch.entity.UserEntity;
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

    public UserEntity createUser(NotificationTarget notificationTarget) {
        Objects.requireNonNull(notificationTarget, "A notification target is required for a User");

        UserEntity userEntity = new UserEntity(UserId.generate().get(), notificationTarget.getTargetAddress(), UserState.ENABLED);
        userRepository.save(userEntity);

        return userEntity;
    }

    public UserEntity getUser(UserId userId) {
        return userRepository.findByUserId(userId.get());
    }

    public void disableUserByNotificationTarget(NotificationTarget notificationTarget) {
        UserEntity user = userRepository.findByNotificationTarget(notificationTarget.getTargetAddress());
        user.setUserState(UserState.DISABLED);
        userRepository.save(user);
    }

    public Stream<UserEntity> getEnabledUsers() {
        return userRepository.findByUserStateIs(UserState.ENABLED).parallel();
    }
}
