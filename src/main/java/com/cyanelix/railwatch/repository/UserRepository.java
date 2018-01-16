package com.cyanelix.railwatch.repository;

import com.cyanelix.railwatch.domain.User;
import com.cyanelix.railwatch.entity.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<UserEntity, String> {
}
