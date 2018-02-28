package com.cyanelix.railwatch.heartbeat;

import com.cyanelix.railwatch.converter.ScheduleDTOToEntityConverter;
import com.cyanelix.railwatch.converter.ScheduleEntityToDTOConverter;
import com.cyanelix.railwatch.domain.*;
import com.cyanelix.railwatch.entity.HeartbeatEntity;
import com.cyanelix.railwatch.entity.ScheduleEntity;
import com.cyanelix.railwatch.entity.UserEntity;
import com.cyanelix.railwatch.firebase.client.FirebaseClient;
import com.cyanelix.railwatch.firebase.client.entity.NotificationRequest;
import com.cyanelix.railwatch.repository.HeartbeatRepository;
import com.cyanelix.railwatch.repository.ScheduleRepository;
import com.cyanelix.railwatch.repository.UserRepository;
import com.cyanelix.railwatch.service.HeartbeatService;
import com.cyanelix.railwatch.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.*;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("it")
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
        ScheduleEntity expiringDisabled = createSchedule("ST1", "expiring", ScheduleState.DISABLED);
        ScheduleEntity expiringEnabled = createSchedule("ST2", "expiring", ScheduleState.ENABLED);
        ScheduleEntity notExpiringDisabled = createSchedule("ST3", "not-expiring", ScheduleState.DISABLED);
        ScheduleEntity notExpiringEnabled = createSchedule("ST4", "not-expiring", ScheduleState.ENABLED);
        ScheduleEntity noHeartbeatDisabled = createSchedule("ST5", "no-heartbeat", ScheduleState.DISABLED);
        ScheduleEntity noHeartbeatEnabled = createSchedule("ST5", "no-heartbeat", ScheduleState.ENABLED);
        ScheduleEntity warningDisabled = createSchedule("ST6", "warning", ScheduleState.DISABLED);
        ScheduleEntity warningEnabled = createSchedule("ST7", "warning", ScheduleState.ENABLED);
        scheduleRepository.save(Arrays.asList(expiringDisabled, expiringEnabled, notExpiringDisabled, notExpiringEnabled, noHeartbeatDisabled, noHeartbeatEnabled, warningDisabled, warningEnabled));

        HeartbeatEntity expiringHeartbeat = new HeartbeatEntity(NotificationTarget.of("expiring"), LocalDateTime.of(2016, 12, 22, 11, 59));
        HeartbeatEntity warningHeartbeat = new HeartbeatEntity(NotificationTarget.of("warning"), LocalDateTime.of(2016, 12, 25, 11, 59));
        HeartbeatEntity notExpiringHeartbeat = new HeartbeatEntity(NotificationTarget.of("not-expiring"), LocalDateTime.of(2016, 12, 25, 12, 1));
        heartbeatRepository.save(Arrays.asList(expiringHeartbeat, warningHeartbeat, notExpiringHeartbeat));

        // When...
        heartbeatService.checkHeartbeats();

        // Then...
        List<ScheduleEntity> expiringSchedules = scheduleRepository.findByNotificationTarget("expiring");
        expiringSchedules.forEach(scheduleEntity -> assertThat(scheduleEntity.getState(), is(ScheduleState.DISABLED)));

        ScheduleEntity retrievedNoHeartbeatDisabled = scheduleRepository.findOne(noHeartbeatDisabled.getId());
        assertThat(retrievedNoHeartbeatDisabled.getState(), is(ScheduleState.DISABLED));

        ScheduleEntity retrievedNoHeartbeatEnabled = scheduleRepository.findOne(noHeartbeatEnabled.getId());
        assertThat(retrievedNoHeartbeatEnabled.getState(), is(ScheduleState.ENABLED));

        ScheduleEntity retrievedNotExpiringDisabled = scheduleRepository.findOne(notExpiringDisabled.getId());
        assertThat(retrievedNotExpiringDisabled.getState(), is(ScheduleState.DISABLED));

        ScheduleEntity retrievedNotExpiringEnabled = scheduleRepository.findOne(notExpiringEnabled.getId());
        assertThat(retrievedNotExpiringEnabled.getState(), is(ScheduleState.ENABLED));

        ScheduleEntity retrievedWarningDisabled = scheduleRepository.findOne(warningDisabled.getId());
        assertThat(retrievedWarningDisabled.getState(), is(ScheduleState.DISABLED));

        ScheduleEntity retrievedWarningEnabled = scheduleRepository.findOne(warningEnabled.getId());
        assertThat(retrievedWarningEnabled.getState(), is(ScheduleState.ENABLED));

        verify(firebaseClient).sendNotification(
                new NotificationRequest(NotificationTarget.of("warning"), "RailWatch", "Open the RailWatch app to keep your train time notifications coming!"));
    }

    private ScheduleEntity createSchedule(String fromStation, String notificationTarget, ScheduleState scheduleState) {
        UserEntity user = new UserEntity(UserId.generate().get(), notificationTarget, UserState.ENABLED);
        userRepository.save(user);
        return new ScheduleEntity(LocalTime.MIN, LocalTime.MAX, DayRange.ALL, fromStation, "BAR", scheduleState, notificationTarget, user);
    }
}
