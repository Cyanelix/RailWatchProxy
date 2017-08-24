package com.cyanelix.railwatch.service;

import com.cyanelix.railwatch.domain.Schedule;
import com.cyanelix.railwatch.domain.TrainTime;
import com.cyanelix.railwatch.entity.SentNotificationEntity;
import com.cyanelix.railwatch.firebase.client.FirebaseClient;
import com.cyanelix.railwatch.firebase.client.entity.NotificationRequest;
import com.cyanelix.railwatch.repository.SentNotificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {
    private static final Logger LOG = LoggerFactory.getLogger(FirebaseClient.class);

    private final FirebaseClient firebaseClient;
    private final SentNotificationRepository sentNotificationRepository;
    private final Clock clock;

    @Autowired
    public NotificationService(FirebaseClient firebaseClient, SentNotificationRepository sentNotificationRepository, Clock clock) {
        this.firebaseClient = firebaseClient;
        this.sentNotificationRepository = sentNotificationRepository;
        this.clock = clock;
    }

    public void sendNotification(Schedule schedule, List<TrainTime> trainTimes) {
        String notificationMessage = buildNotificationMessage(schedule, trainTimes);
        NotificationRequest notificationRequest = new NotificationRequest(schedule.getNotificationTarget(), "RailWatch", notificationMessage);

        if (isDuplicateRequest(notificationRequest)) {
            LOG.debug("Not sending duplicate notification.");
            return;
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Sending request for schedule {}", schedule.toString());
        }

        boolean success = firebaseClient.sendNotification(notificationRequest);

        if (success) {
            sentNotificationRepository.save(SentNotificationEntity.of(notificationRequest, LocalDateTime.now(clock)));
        }
    }

    private boolean isDuplicateRequest(NotificationRequest notificationRequest) {
        return sentNotificationRepository.findBySentDateTimeAfter(LocalDate.now(clock).atStartOfDay()).parallelStream()
                .map(NotificationRequest::of)
                .anyMatch(sentRequest -> sentRequest.equals(notificationRequest));
    }

    private String buildNotificationMessage(Schedule schedule, List<TrainTime> trainTimes) {
        return trainTimes.parallelStream()
                .map(trainTime -> String.format("%s @ %s", schedule.getJourney().toString(), trainTime.toString()))
                .collect(Collectors.joining("\n"));
    }
}
