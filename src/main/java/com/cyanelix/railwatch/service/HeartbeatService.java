package com.cyanelix.railwatch.service;

import com.cyanelix.railwatch.domain.NotificationTarget;
import com.cyanelix.railwatch.domain.Schedule;
import com.cyanelix.railwatch.entity.HeartbeatEntity;
import com.cyanelix.railwatch.repository.HeartbeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class HeartbeatService {
    private final ScheduleService scheduleService;
    private final HeartbeatRepository heartbeatRepository;
    private final Clock clock;

    @Autowired
    public HeartbeatService(ScheduleService scheduleService, HeartbeatRepository heartbeatRepository, Clock clock) {
        this.scheduleService = scheduleService;
        this.heartbeatRepository = heartbeatRepository;
        this.clock = clock;
    }

    public void recordHeartbeat(NotificationTarget notificationTarget) {
        HeartbeatEntity heartbeatEntity = new HeartbeatEntity(notificationTarget, LocalDateTime.now(clock));
        heartbeatRepository.save(heartbeatEntity);
    }

    @Scheduled(fixedDelay = 86400000L)
    public void disableAbsentClients() {
        scheduleService.getEnabledSchedules()
                .map(Schedule::getNotificationTarget)
                .map(heartbeatRepository::findFirstByNotificationTargetEqualsOrderByDateTimeDesc)
                .filter(this::heartbeatOlderThanThreshold)
                .map(HeartbeatEntity::getNotificationTarget)
                .distinct()
                .forEach(scheduleService::disableSchedulesForNotificationTarget);
    }

    private boolean heartbeatOlderThanThreshold(HeartbeatEntity heartbeat) {
        if (heartbeat == null) {
            return false;
        }

        LocalDateTime now = LocalDateTime.now(clock);
        LocalDateTime lastWeek = now.minus(1L, ChronoUnit.WEEKS);

        return heartbeat.getDateTime().isBefore(lastWeek);
    }
}
