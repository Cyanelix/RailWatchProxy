package com.cyanelix.railwatch.service;

import com.cyanelix.railwatch.domain.NotificationTarget;
import com.cyanelix.railwatch.domain.Schedule;
import com.cyanelix.railwatch.entity.HeartbeatEntity;
import com.cyanelix.railwatch.repository.HeartbeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.stream.Stream;

@Service
public class HeartbeatService {
    private static final Duration WARN_THRESHOLD = Duration.of(7L, ChronoUnit.DAYS);
    private static final Duration DISABLE_THRESHOLD = Duration.of(10L, ChronoUnit.DAYS);

    private final ScheduleService scheduleService;
    private final NotificationService notificationService;
    private final HeartbeatRepository heartbeatRepository;
    private final Clock clock;

    @Autowired
    public HeartbeatService(ScheduleService scheduleService, NotificationService notificationService, HeartbeatRepository heartbeatRepository, Clock clock) {
        this.scheduleService = scheduleService;
        this.notificationService = notificationService;
        this.heartbeatRepository = heartbeatRepository;
        this.clock = clock;
    }

    public void recordHeartbeat(NotificationTarget notificationTarget) {
        HeartbeatEntity heartbeatEntity = new HeartbeatEntity(notificationTarget, LocalDateTime.now(clock));
        heartbeatRepository.save(heartbeatEntity);
    }

    @Scheduled(fixedDelay = 86400000L)
    public void checkHeartbeats() {
        getNotificationTargetsFilteredByHeartbeat(DISABLE_THRESHOLD)
                .forEach(scheduleService::disableSchedulesForNotificationTarget);
        getNotificationTargetsFilteredByHeartbeat(WARN_THRESHOLD)
                .forEach(notificationTarget -> notificationService.sendNotification(notificationTarget, "Tap this notification to keep your train time notifications coming!"));
    }

    private Stream<NotificationTarget> getNotificationTargetsFilteredByHeartbeat(Duration threshold) {
        return scheduleService.getEnabledSchedules()
                .map(Schedule::getNotificationTarget)
                .map(heartbeatRepository::findFirstByNotificationTargetEqualsOrderByDateTimeDesc)
                .filter(heartbeatEntity -> heartbeatOlderThanThreshold(heartbeatEntity, threshold))
                .map(HeartbeatEntity::getNotificationTarget)
                .distinct();
    }

    private boolean heartbeatOlderThanThreshold(HeartbeatEntity heartbeat, Duration duration) {
        if (heartbeat == null) {
            return false;
        }

        LocalDateTime now = LocalDateTime.now(clock);
        LocalDateTime lastWeek = now.minus(duration);

        return heartbeat.getDateTime().isBefore(lastWeek);
    }
}
