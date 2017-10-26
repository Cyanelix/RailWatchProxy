package com.cyanelix.railwatch.service;

import com.cyanelix.railwatch.domain.NotificationTarget;
import com.cyanelix.railwatch.entity.HeartbeatEntity;
import com.cyanelix.railwatch.repository.HeartbeatRepository;
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

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class HeartbeatServiceTest {
    @Mock
    private HeartbeatRepository heartbeatRepository;

    @Mock
    private Clock clock;

    @InjectMocks
    private HeartbeatService heartbeatService;

    @Test
    public void givenADeviceId_saveIt() {
        // Given...
        NotificationTarget notificationTarget = NotificationTarget.of("notification-id");
        given(clock.instant()).willReturn(Instant.parse("2017-01-01T12:00:00Z"));
        given(clock.getZone()).willReturn(ZoneId.of("Europe/London"));

        // When...
        heartbeatService.recordHeartbeat(notificationTarget);

        // Then...
        ArgumentCaptor<HeartbeatEntity> heartbeatEntityArgumentCaptor = ArgumentCaptor.forClass(HeartbeatEntity.class);
        verify(heartbeatRepository).save(heartbeatEntityArgumentCaptor.capture());

        HeartbeatEntity heartbeatEntity = heartbeatEntityArgumentCaptor.getValue();
        assertThat(heartbeatEntity.getNotificationTarget(), is(notificationTarget));
        assertThat(heartbeatEntity.getDateTime(), is(LocalDateTime.of(2017, 1, 1, 12, 0, 0)));
    }
}
