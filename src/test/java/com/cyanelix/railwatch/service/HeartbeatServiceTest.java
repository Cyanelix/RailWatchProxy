package com.cyanelix.railwatch.service;

import com.cyanelix.railwatch.domain.NotificationTarget;
import com.cyanelix.railwatch.domain.UserId;
import com.cyanelix.railwatch.domain.UserState;
import com.cyanelix.railwatch.entity.HeartbeatEntity;
import com.cyanelix.railwatch.entity.UserEntity;
import com.cyanelix.railwatch.repository.HeartbeatRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.stream.Stream;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class HeartbeatServiceTest {
    @Mock
    private HeartbeatRepository heartbeatRepository;

    @Mock
    private ScheduleService scheduleService;

    @Mock
    private UserService userService;

    @Mock
    private NotificationService notificationService;

    @Mock
    private Clock clock;

    @InjectMocks
    private HeartbeatService heartbeatService;

    @Before
    public void setTestClock() {
        given(clock.instant()).willReturn(Instant.parse("2017-01-01T12:00:00Z"));
        given(clock.getZone()).willReturn(ZoneId.of("Europe/London"));
    }

    @Test
    public void givenADeviceId_saveIt() {
        // Given...
        NotificationTarget notificationTarget = NotificationTarget.of("notification-id");

        // When...
        heartbeatService.recordHeartbeat(notificationTarget);

        // Then...
        ArgumentCaptor<HeartbeatEntity> heartbeatEntityArgumentCaptor = ArgumentCaptor.forClass(HeartbeatEntity.class);
        verify(heartbeatRepository).save(heartbeatEntityArgumentCaptor.capture());

        HeartbeatEntity heartbeatEntity = heartbeatEntityArgumentCaptor.getValue();
        assertThat(heartbeatEntity.getNotificationTarget(), is(notificationTarget));
        assertThat(heartbeatEntity.getDateTime(), is(LocalDateTime.of(2017, 1, 1, 12, 0, 0)));
    }

    @Test
    public void givenAHeartbeatOneDayAgo_doNotDisable_doNotWarn() {
        // Given...
        NotificationTarget notificationTarget = NotificationTarget.of("foo");
        UserEntity user = createUser(notificationTarget);
        HeartbeatEntity oneDayOldHeartbeat =
                new HeartbeatEntity(notificationTarget, LocalDateTime.of(2016, 12, 31, 12, 1));

        given(userService.getEnabledUsers()).willReturn(Stream.of(user), Stream.of(user));
        given(heartbeatRepository.findFirstByNotificationTargetEqualsOrderByDateTimeDesc(notificationTarget)).willReturn(oneDayOldHeartbeat);

        // When...
        heartbeatService.checkHeartbeats();

        // Then...
        verify(userService, never()).disableUserByNotificationTarget(any());
        verify(notificationService, never()).sendNotification(any(NotificationTarget.class), any());
    }

    @Test
    public void givenAHeartbeatSevenDaysAgo_doNotDisable_doWarn() {
        // Given...
        NotificationTarget notificationTarget = NotificationTarget.of("foo");
        UserEntity user = createUser(notificationTarget);
        HeartbeatEntity sevenDayOldHeartbeat =
                new HeartbeatEntity(notificationTarget, LocalDateTime.of(2016, 12, 25, 11, 59));

        given(userService.getEnabledUsers()).willReturn(Stream.of(user), Stream.of(user));
        given(heartbeatRepository.findFirstByNotificationTargetEqualsOrderByDateTimeDesc(notificationTarget)).willReturn(sevenDayOldHeartbeat);

        // When...
        heartbeatService.checkHeartbeats();

        // Then...
        verify(userService, never()).disableUserByNotificationTarget(any());
        verify(notificationService).sendNotification(notificationTarget, "Open the RailWatch app to keep your train time notifications coming!");
    }

    @Test
    public void givenAHeartbeatTenDaysAgo_doDisable_doNotWarn() {
        // Given...
        NotificationTarget notificationTarget = NotificationTarget.of("foo");
        UserEntity user = createUser(notificationTarget);
        HeartbeatEntity overTenDaysAgoHeartbeat =
                new HeartbeatEntity(notificationTarget, LocalDateTime.of(2016, 12, 22, 11, 59));

        given(userService.getEnabledUsers()).willReturn(Stream.of(user), Stream.empty());
        given(heartbeatRepository.findFirstByNotificationTargetEqualsOrderByDateTimeDesc(notificationTarget)).willReturn(overTenDaysAgoHeartbeat);

        // When...
        heartbeatService.checkHeartbeats();

        // Then...
        verify(userService).disableUserByNotificationTarget(notificationTarget);
        verify(notificationService, never()).sendNotification(any(NotificationTarget.class), any());
    }

    private UserEntity createUser(NotificationTarget notificationTarget) {
        return new UserEntity(UserId.generate().get(), notificationTarget.getTargetAddress(), UserState.ENABLED);
    }
}
