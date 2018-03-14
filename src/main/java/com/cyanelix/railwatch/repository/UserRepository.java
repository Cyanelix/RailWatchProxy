package com.cyanelix.railwatch.repository;

import com.cyanelix.railwatch.domain.UserState;
import com.cyanelix.railwatch.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.stream.Stream;

public interface UserRepository extends MongoRepository<User, String> {
    User findByUserId(String userId);

    User findByNotificationTarget(String notificationTarget);

    Stream<User> findByUserStateIs(UserState userState);
}
