package com.cyanelix.railwatch.repository;

import com.cyanelix.railwatch.domain.UserState;
import com.cyanelix.railwatch.entity.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.stream.Stream;

public interface UserRepository extends MongoRepository<UserEntity, String> {
    UserEntity findByUserId(String userId);

    UserEntity findByNotificationTarget(String notificationTarget);

    Stream<UserEntity> findByUserStateIs(UserState userState);
}
