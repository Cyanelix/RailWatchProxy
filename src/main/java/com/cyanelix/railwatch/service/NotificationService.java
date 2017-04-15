package com.cyanelix.railwatch.service;

import com.cyanelix.railwatch.domain.Schedule;
import com.cyanelix.railwatch.domain.TrainTime;
import com.cyanelix.railwatch.firebase.client.FirebaseClient;
import com.cyanelix.railwatch.firebase.client.entity.NotificationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {
    private final FirebaseClient firebaseClient;

    @Autowired
    public NotificationService(FirebaseClient firebaseClient) {
        this.firebaseClient = firebaseClient;
    }

    public void sendNotification(Schedule schedule, List<TrainTime> trainTimes) {
        String notificationMessage = buildNotificationMessage(schedule, trainTimes);
        NotificationRequest notificationRequest = new NotificationRequest(schedule.getNotificationTarget(), "RailWatch", notificationMessage);
        firebaseClient.sendNotification(notificationRequest);
    }

    private String buildNotificationMessage(Schedule schedule, List<TrainTime> trainTimes) {
        return trainTimes.parallelStream()
                .map(trainTime -> String.format("%s -> %s @ %s", schedule.getFromStation(), schedule.getToStation(), trainTime.toString()))
                .collect(Collectors.joining("\n"));
    }
}
