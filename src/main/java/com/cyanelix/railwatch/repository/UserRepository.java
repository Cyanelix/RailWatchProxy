package com.cyanelix.railwatch.repository;

import com.cyanelix.railwatch.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
}
