package com.cyanelix.railwatch.repository;

import com.cyanelix.railwatch.domain.NotificationTarget;
import com.cyanelix.railwatch.entity.HeartbeatEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface HeartbeatRepository extends MongoRepository<HeartbeatEntity, String> {
    HeartbeatEntity findFirstByNotificationTargetEqualsOrderByDateTimeDesc(NotificationTarget notificationTarget);
}
