package com.cyanelix.railwatch.service;

import com.cyanelix.railwatch.domain.*;
import com.cyanelix.railwatch.entity.SentNotificationEntity;
import com.cyanelix.railwatch.firebase.client.FirebaseClient;
import com.cyanelix.railwatch.firebase.client.entity.NotificationRequest;
import com.cyanelix.railwatch.repository.SentNotificationRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class NotificationServiceTest {
    @Mock
    private FirebaseClient firebaseClient;

    @Mock
    private SentNotificationRepository sentNotificationRepository;

    private NotificationService notificationService;

    @Before
    public void setup() {
        notificationService = new NotificationService(firebaseClient, sentNotificationRepository, Clock.fixed(Instant.parse("2017-01-01T10:30:00Z"), ZoneId.systemDefault()));
    }

    @Test
    public void singleUniqueTrainTime_sendNotification() {
        // Given...
        given(sentNotificationRepository.findBySentDateTimeAfter(any())).willReturn(Collections.emptyList());

        Schedule schedule = Schedule.of(
                LocalTime.of(9, 0), LocalTime.of(10, 0), DayRange.ALL,
                Journey.of(Station.of("FOO"), Station.of("BAR")), NotificationTarget.of("notification-to"),
                ScheduleState.ENABLED);
        List<TrainTime> trainTimes = Collections.singletonList(
                new TrainTime.Builder(LocalTime.NOON)
                        .withExpectedDepartureTime(LocalTime.NOON)
                        .build());

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

    @Test
    public void doNotSendDuplicateNotification() {
        // Given...
        Schedule schedule = Schedule.of(
                null, null, DayRange.ALL, Journey.of(Station.of("FOO"), Station.of("BAR")),
                NotificationTarget.of("notification-to"), ScheduleState.ENABLED);
        List<TrainTime> trainTimes = Collections.singletonList(
                new TrainTime.Builder(LocalTime.NOON)
                        .withExpectedDepartureTime(LocalTime.NOON)
                        .build());

        SentNotificationEntity sentNotificationEntity = new SentNotificationEntity("notification-to", "RailWatch", "FOO -> BAR @ 12:00", "high", null);
        given(sentNotificationRepository.findBySentDateTimeAfter(any())).willReturn(Collections.singletonList(sentNotificationEntity));

        // When...
        notificationService.sendNotification(schedule, trainTimes);

        // Then...
        verify(firebaseClient, never()).sendNotification(any());
    }

    @Test
    public void notificationSentPreviously_sendDifferentNotification() {
        // Given...
        Schedule schedule = Schedule.of(
                LocalTime.of(9, 0), LocalTime.of(10, 0), DayRange.ALL,
                Journey.of(Station.of("FOO"), Station.of("BAR")), NotificationTarget.of("notification-to"),
                ScheduleState.ENABLED);
        List<TrainTime> trainTimes = Collections.singletonList(
                new TrainTime.Builder(LocalTime.NOON)
                        .withExpectedDepartureTime(LocalTime.NOON)
                        .build());

        SentNotificationEntity sentNotificationEntity = new SentNotificationEntity("notification-to", "RailWatch", "Different message body", "high", null);
        given(sentNotificationRepository.findBySentDateTimeAfter(any())).willReturn(Collections.singletonList(sentNotificationEntity));

        // When...
        notificationService.sendNotification(schedule, trainTimes);

        // Then...
        verify(firebaseClient, times(1)).sendNotification(any());
    }
}
