package com.cyanelix.railwatch.repository;

import com.cyanelix.railwatch.domain.NotificationTarget;
import com.cyanelix.railwatch.entity.Heartbeat;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface HeartbeatRepository extends MongoRepository<Heartbeat, String> {
    Heartbeat findFirstByNotificationTargetEqualsOrderByDateTimeDesc(NotificationTarget notificationTarget);
}
