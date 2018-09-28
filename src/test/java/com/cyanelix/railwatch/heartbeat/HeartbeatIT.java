package com.cyanelix.railwatch.heartbeat;

import com.cyanelix.railwatch.domain.NotificationTarget;
import com.cyanelix.railwatch.domain.UserId;
import com.cyanelix.railwatch.domain.UserState;
import com.cyanelix.railwatch.entity.Heartbeat;
import com.cyanelix.railwatch.entity.User;
import com.cyanelix.railwatch.firebase.client.FirebaseClient;
import com.cyanelix.railwatch.firebase.client.entity.NotificationRequest;
import com.cyanelix.railwatch.repository.HeartbeatRepository;
import com.cyanelix.railwatch.repository.ScheduleRepository;
import com.cyanelix.railwatch.repository.UserRepository;
import com.cyanelix.railwatch.service.HeartbeatService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HeartbeatIT {
    @MockBean
    private Clock clock;

    @MockBean
    private FirebaseClient firebaseClient;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private HeartbeatRepository heartbeatRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HeartbeatService heartbeatService;

    @Before
    public void setup() {
        heartbeatRepository.deleteAll();
        scheduleRepository.deleteAll();
        doReturn(Instant.parse("2017-01-01T12:00:00Z")).when(clock).instant();
        doReturn(ZoneId.systemDefault()).when(clock).getZone();
        when(firebaseClient.sendNotification(any())).thenReturn(true);
    }

    @Test
    public void enabledAndDisabledSchedulesWithNotificationTargetsThatDoAndDoNotExpire() {
        // Given...
        User expiringDisabled = new User(UserId.generate(), "expiring-disabled", UserState.DISABLED);
        User expiringEnabled = new User(UserId.generate(), "expiring-enabled", UserState.ENABLED);
        User notExpiringDisabled = new User(UserId.generate(), "not-expiring-disabled", UserState.DISABLED);
        User notExpiringEnabled = new User(UserId.generate(), "not-expiring-enabled", UserState.ENABLED);
        User noHeartbeatDisabled = new User(UserId.generate(), "no-heartbeat-disabled", UserState.DISABLED);
        User noHeartbeatEnabled = new User(UserId.generate(), "no-heartbeat-enabled", UserState.ENABLED);
        User warningDisabled = new User(UserId.generate(), "warning-disabled", UserState.DISABLED);
        User warningEnabled = new User(UserId.generate(), "warning-enabled", UserState.ENABLED);
        userRepository.saveAll(Arrays.asList(expiringDisabled, expiringEnabled, notExpiringDisabled, notExpiringEnabled, noHeartbeatDisabled, noHeartbeatEnabled, warningDisabled, warningEnabled));

        Heartbeat expiringDisabledHeartbeat = new Heartbeat(NotificationTarget.of("expiring-disabled"), LocalDateTime.of(2016, 12, 22, 11, 59));
        Heartbeat expiringEnabledHeartbeat = new Heartbeat(NotificationTarget.of("expiring-enabled"), LocalDateTime.of(2016, 12, 22, 11, 59));
        Heartbeat notExpiringDisabledHeartbeat = new Heartbeat(NotificationTarget.of("not-expiring-disabled"), LocalDateTime.of(2016, 12, 25, 12, 1));
        Heartbeat notExpiringEnabledHeartbeat = new Heartbeat(NotificationTarget.of("not-expiring-enabled"), LocalDateTime.of(2016, 12, 25, 12, 1));
        Heartbeat warningDisabledHeartbeat = new Heartbeat(NotificationTarget.of("warning-disabled"), LocalDateTime.of(2016, 12, 25, 11, 59));
        Heartbeat warningEnabledHeartbeat = new Heartbeat(NotificationTarget.of("warning-enabled"), LocalDateTime.of(2016, 12, 25, 11, 59));
        heartbeatRepository.saveAll(Arrays.asList(expiringDisabledHeartbeat, expiringEnabledHeartbeat, notExpiringDisabledHeartbeat,
                notExpiringEnabledHeartbeat, warningDisabledHeartbeat, warningEnabledHeartbeat));

        // When...
        heartbeatService.checkHeartbeats();

        // Then...
        assertThat(userRepository.findByNotificationTarget("expiring-disabled").getUserState(), is(UserState.DISABLED));
        assertThat(userRepository.findByNotificationTarget("expiring-enabled").getUserState(), is(UserState.DISABLED));

        assertThat(userRepository.findByNotificationTarget("not-expiring-disabled").getUserState(), is(UserState.DISABLED));
        assertThat(userRepository.findByNotificationTarget("not-expiring-enabled").getUserState(), is(UserState.ENABLED));

        assertThat(userRepository.findByNotificationTarget("no-heartbeat-disabled").getUserState(), is(UserState.DISABLED));
        assertThat(userRepository.findByNotificationTarget("no-heartbeat-enabled").getUserState(), is(UserState.ENABLED));

        assertThat(userRepository.findByNotificationTarget("warning-disabled").getUserState(), is(UserState.DISABLED));
        assertThat(userRepository.findByNotificationTarget("warning-enabled").getUserState(), is(UserState.ENABLED));

        verify(firebaseClient).sendNotification(
                new NotificationRequest(NotificationTarget.of("warning-enabled"), "RailWatch", "Open the RailWatch app to keep your train time notifications coming!"));
    }
}
