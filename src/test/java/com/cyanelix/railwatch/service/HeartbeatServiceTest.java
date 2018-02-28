package com.cyanelix.railwatch.service;

import com.cyanelix.railwatch.domain.*;
import com.cyanelix.railwatch.entity.HeartbeatEntity;
import com.cyanelix.railwatch.entity.ScheduleEntity;
import com.cyanelix.railwatch.entity.UserEntity;
import com.cyanelix.railwatch.repository.HeartbeatRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.*;
import java.util.stream.Stream;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class HeartbeatServiceTest {
    @Mock
    private HeartbeatRepository heartbeatRepository;

    @Mock
    private ScheduleService scheduleService;

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
    public void givenNoHeartbeatsForTenDaysForTwoSchedulesWithSameNotificationTarget_onlyCallDisableSchedulesOnce() {
        // Given...
        NotificationTarget notificationTarget = NotificationTarget.of("foo");
        UserEntity user = createUser(notificationTarget);
        ScheduleEntity schedule1 =
                new ScheduleEntity(LocalTime.MIN, LocalTime.MAX, DayRange.ALL, Station.of("FOO"), Station.of("BAR"), ScheduleState.ENABLED, notificationTarget.getTargetAddress(), user);
        ScheduleEntity schedule2 =
                new ScheduleEntity(LocalTime.NOON, LocalTime.MAX, DayRange.ALL, Station.of("AAA"), Station.of("BBB"), ScheduleState.ENABLED, notificationTarget.getTargetAddress(), user);
        HeartbeatEntity disablableHeartbeat =
                new HeartbeatEntity(notificationTarget, LocalDateTime.of(2016, 12, 22, 11, 59));

        given(scheduleService.getEnabledSchedules()).willReturn(Stream.of(schedule1, schedule2), Stream.empty());
        given(heartbeatRepository.findFirstByNotificationTargetEqualsOrderByDateTimeDesc(notificationTarget)).willReturn(disablableHeartbeat);

        // When...
        heartbeatService.checkHeartbeats();

        // Then...
        ArgumentCaptor<NotificationTarget> notificationTargetCaptor = ArgumentCaptor.forClass(NotificationTarget.class);
        verify(scheduleService, times(1)).disableSchedulesForNotificationTarget(notificationTargetCaptor.capture());

        NotificationTarget capturedNotificationTarget = notificationTargetCaptor.getValue();
        assertThat(capturedNotificationTarget, is(notificationTarget));
    }

    @Test
    public void givenAHeartbeatOneDayAgo_doNotDisable_doNotWarn() {
        // Given...
        NotificationTarget notificationTarget = NotificationTarget.of("foo");
        UserEntity user = createUser(notificationTarget);
        ScheduleEntity schedule =
                new ScheduleEntity(LocalTime.MIN, LocalTime.MAX, DayRange.ALL, Station.of("FOO"), Station.of("BAR"), ScheduleState.ENABLED, "remove-notification-target", user);
        HeartbeatEntity oneDayOldHeartbeat =
                new HeartbeatEntity(notificationTarget, LocalDateTime.of(2016, 12, 31, 12, 1));

        given(scheduleService.getEnabledSchedules()).willReturn(Stream.of(schedule), Stream.of(schedule));
        given(heartbeatRepository.findFirstByNotificationTargetEqualsOrderByDateTimeDesc(notificationTarget)).willReturn(oneDayOldHeartbeat);

        // When...
        heartbeatService.checkHeartbeats();

        // Then...
        verify(scheduleService, never()).disableSchedulesForNotificationTarget(any());
        verify(notificationService, never()).sendNotification(any(NotificationTarget.class), any());
    }

    @Test
    public void givenAHeartbeatSevenDaysAgo_doNotDisable_doWarn() {
        // Given...
        NotificationTarget notificationTarget = NotificationTarget.of("foo");
        UserEntity user = createUser(notificationTarget);
        ScheduleEntity schedule =
                new ScheduleEntity(LocalTime.MIN, LocalTime.MAX, DayRange.ALL, Station.of("FOO"), Station.of("BAR"), ScheduleState.ENABLED, notificationTarget.getTargetAddress(), user);
        HeartbeatEntity sevenDayOldHeartbeat =
                new HeartbeatEntity(notificationTarget, LocalDateTime.of(2016, 12, 25, 11, 59));

        given(scheduleService.getEnabledSchedules()).willReturn(Stream.of(schedule), Stream.of(schedule));
        given(heartbeatRepository.findFirstByNotificationTargetEqualsOrderByDateTimeDesc(notificationTarget)).willReturn(sevenDayOldHeartbeat);

        // When...
        heartbeatService.checkHeartbeats();

        // Then...
        verify(scheduleService, never()).disableSchedulesForNotificationTarget(any());
        verify(notificationService).sendNotification(notificationTarget, "Open the RailWatch app to keep your train time notifications coming!");
    }

    @Test
    public void givenAHeartbeatTenDaysAgo_doDisable_doNotWarn() {
        // Given...
        NotificationTarget notificationTarget = NotificationTarget.of("foo");
        UserEntity user = createUser(notificationTarget);
        ScheduleEntity schedule =
                new ScheduleEntity(LocalTime.MIN, LocalTime.MAX, DayRange.ALL, Station.of("FOO"), Station.of("BAR"), ScheduleState.ENABLED, notificationTarget.getTargetAddress(), user);
        HeartbeatEntity overTenDaysAgoHeartbeat =
                new HeartbeatEntity(notificationTarget, LocalDateTime.of(2016, 12, 22, 11, 59));

        given(scheduleService.getEnabledSchedules()).willReturn(Stream.of(schedule), Stream.empty());
        given(heartbeatRepository.findFirstByNotificationTargetEqualsOrderByDateTimeDesc(notificationTarget)).willReturn(overTenDaysAgoHeartbeat);

        // When...
        heartbeatService.checkHeartbeats();

        // Then...
        verify(scheduleService).disableSchedulesForNotificationTarget(notificationTarget);
        verify(notificationService, never()).sendNotification(any(NotificationTarget.class), any());
    }

    private UserEntity createUser(NotificationTarget notificationTarget) {
        return new UserEntity(UserId.generate().get(), notificationTarget.getTargetAddress(), UserState.ENABLED);
    }
}
