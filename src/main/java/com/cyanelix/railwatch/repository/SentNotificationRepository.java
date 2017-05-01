package com.cyanelix.railwatch.repository;

import com.cyanelix.railwatch.entity.SentNotificationEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface SentNotificationRepository extends MongoRepository<SentNotificationEntity, String> {
    List<SentNotificationEntity> findBySentDateTimeAfter(LocalDateTime filter);
}
