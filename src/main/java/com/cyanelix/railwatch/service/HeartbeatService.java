package com.cyanelix.railwatch.service;

import com.cyanelix.railwatch.domain.NotificationTarget;
import com.cyanelix.railwatch.entity.HeartbeatEntity;
import com.cyanelix.railwatch.repository.HeartbeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;

@Service
public class HeartbeatService {
    private final HeartbeatRepository heartbeatRepository;
    private final Clock clock;

    @Autowired
    public HeartbeatService(HeartbeatRepository heartbeatRepository, Clock clock) {
        this.heartbeatRepository = heartbeatRepository;
        this.clock = clock;
    }

    public void recordHeartbeat(NotificationTarget notificationTarget) {
        HeartbeatEntity heartbeatEntity = new HeartbeatEntity(notificationTarget, LocalDateTime.now(clock));
        heartbeatRepository.save(heartbeatEntity);
    }
}
