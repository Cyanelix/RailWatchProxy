package com.cyanelix.railwatch.service;

import com.cyanelix.railwatch.domain.*;
import com.cyanelix.railwatch.entity.HeartbeatEntity;
import com.cyanelix.railwatch.entity.ScheduleEntity;
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
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class HeartbeatServiceTest {
    @Mock
    private HeartbeatRepository heartbeatRepository;

    @Mock
    private ScheduleService scheduleService;

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
    public void givenNoHeartbeatsForAWeekForTwoSchedulesWithSameNotificationTarget_onlyCallDisableSchedulesOnce() {
        // Given...
        NotificationTarget notificationTarget = NotificationTarget.of("foo");
        Schedule schedule1 =
                Schedule.of(LocalTime.MIN, LocalTime.MAX, DayRange.ALL, Journey.of(Station.of("FOO"), Station.of("BAR")), notificationTarget, ScheduleState.ENABLED);
        Schedule schedule2 =
                Schedule.of(LocalTime.NOON, LocalTime.MAX, DayRange.ALL, Journey.of(Station.of("AAA"), Station.of("BBB")), notificationTarget, ScheduleState.ENABLED);
        HeartbeatEntity weekOldHeartbeat =
                new HeartbeatEntity(notificationTarget, LocalDateTime.of(2016, 12, 25, 11, 59));

        given(scheduleService.getEnabledSchedules()).willReturn(Stream.of(schedule1, schedule2));
        given(heartbeatRepository.findFirstByNotificationTargetEqualsOrderByDateTimeDesc(notificationTarget)).willReturn(weekOldHeartbeat);

        // When...
        heartbeatService.disableAbsentClients();

        // Then...
        ArgumentCaptor<NotificationTarget> notificationTargetCaptor = ArgumentCaptor.forClass(NotificationTarget.class);
        verify(scheduleService, times(1)).disableSchedulesForNotificationTarget(notificationTargetCaptor.capture());

        NotificationTarget capturedNotificationTarget = notificationTargetCaptor.getValue();
        assertThat(capturedNotificationTarget, is(notificationTarget));
    }

    @Test
    public void givenAHeartbeatLessThanAWeekAgo_doNotDisable() {
        // Given...
        NotificationTarget notificationTarget = NotificationTarget.of("foo");
        Schedule schedule =
                Schedule.of(LocalTime.MIN, LocalTime.MAX, DayRange.ALL, Journey.of(Station.of("FOO"), Station.of("BAR")), notificationTarget, ScheduleState.ENABLED);
        HeartbeatEntity lessThanAWeekOldHeartbeat =
                new HeartbeatEntity(notificationTarget, LocalDateTime.of(2016, 12, 25, 12, 1));

        given(scheduleService.getEnabledSchedules()).willReturn(Stream.of(schedule));
        given(heartbeatRepository.findFirstByNotificationTargetEqualsOrderByDateTimeDesc(notificationTarget)).willReturn(lessThanAWeekOldHeartbeat);

        // When...
        heartbeatService.disableAbsentClients();

        // Then...
        verify(scheduleService, never()).disableSchedulesForNotificationTarget(any());
    }
}
