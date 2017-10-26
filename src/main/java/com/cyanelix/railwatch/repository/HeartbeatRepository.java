package com.cyanelix.railwatch.repository;

import com.cyanelix.railwatch.entity.HeartbeatEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface HeartbeatRepository extends MongoRepository<HeartbeatEntity, String> {
}
