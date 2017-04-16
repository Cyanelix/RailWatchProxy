package com.cyanelix.railwatch.service;

import com.cyanelix.railwatch.domain.NotificationTarget;
import com.cyanelix.railwatch.domain.Schedule;
import com.cyanelix.railwatch.domain.Station;
import com.cyanelix.railwatch.domain.TrainTime;
import com.cyanelix.railwatch.firebase.client.FirebaseClient;
import com.cyanelix.railwatch.firebase.client.entity.NotificationRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class NotificationServiceTest {
    @Mock
    private FirebaseClient firebaseClient;

    @InjectMocks
    private NotificationService notificationService;

    @Test
    public void singleTrainTime_sendNotification() {
        // Given...
        Schedule schedule = Schedule.of(null, null, Station.of("FOO"), Station.of("BAR"), NotificationTarget.of("notification-to"));
        List<TrainTime> trainTimes = Collections.singletonList(TrainTime.of(LocalTime.NOON, Optional.of(LocalTime.NOON), ""));

        // When...
        notificationService.sendNotification(schedule, trainTimes);

        // Then...
        ArgumentCaptor<NotificationRequest> notificationRequestCaptor = ArgumentCaptor.forClass(NotificationRequest.class);
        verify(firebaseClient).sendNotification(notificationRequestCaptor.capture());

        NotificationRequest notificationRequest = notificationRequestCaptor.getValue();
        assertThat(notificationRequest.getTo(), is("notification-to"));
        assertThat(notificationRequest.getNotification().getTitle(), is("RailWatch"));
        assertThat(notificationRequest.getNotification().getBody(), is("FOO -> BAR @ 12:00"));
    }

}
