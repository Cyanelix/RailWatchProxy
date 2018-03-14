package com.cyanelix.railwatch.repository;

import com.cyanelix.railwatch.entity.SentNotification;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface SentNotificationRepository extends MongoRepository<SentNotification, String> {
    List<SentNotification> findBySentDateTimeAfter(LocalDateTime filter);
}
