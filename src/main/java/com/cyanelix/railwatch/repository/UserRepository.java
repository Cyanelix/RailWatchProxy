package com.cyanelix.railwatch.repository;

import com.cyanelix.railwatch.entity.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<UserEntity, String> {
    UserEntity findByUserId(String userId);
}
